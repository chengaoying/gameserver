package cn.ohyeah.gameserver.global;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class ThreadSafeOfConnectionManager implements Runnable{
	
	/**
	 * 请求超时30秒钟
	 */
	private static int REQUEST_TIMEOUT = 30000;		
	
	/**
	 * 等待数据超时时间60秒钟
	 */
	private static int SO_TIMEOUT = 60000;			
	
	/**
	 * 客户端总并行链接最大数
	 */
	private static int MAXTOTAL = 400;				
	
	/**
	 * 每个主机的最大并行链接数 
	 */
	private static int MAXPERROUTE = 400;		
	
	private static int EXPIRE_CHECK_PERIOD = 60000;
	
	private static boolean shutdown;
	
	private static Log log = LogFactory.getLog(ThreadSafeOfConnectionManager.class);
	private static PoolingClientConnectionManager pccm;
	private static DefaultHttpClient client;
	private static ThreadSafeOfConnectionManager instance;
	
	static{
		instance = new ThreadSafeOfConnectionManager();
		SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		pccm = new PoolingClientConnectionManager(schemeRegistry);
		pccm.setDefaultMaxPerRoute(MAXPERROUTE);	
		pccm.setMaxTotal(MAXTOTAL);	
		new Thread(instance).start();
	}
	
	/**
	 * @return DefaultHttpClient
	 */
	public static DefaultHttpClient buildDefaultHttpsClient(){  		
		HttpParams params = new BasicHttpParams(); 
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1); 
	    HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1"); 
	    HttpProtocolParams.setUseExpectContinue(params, true); 	  
	    params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, REQUEST_TIMEOUT);  
	    params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT); 
	    client = new DefaultHttpClient(pccm, params);  
	    
	    //请求重试处理
	    client.setHttpRequestRetryHandler(new HttpRequestRetryHandler() {
			public boolean retryRequest(IOException exception, int executionCount,
                    HttpContext context) {
				
				// 如果超过最大重试次数，那么就不要继续了
				if(executionCount >= 5){
                    return false;
                }
				
				// 如果服务器丢掉了连接，那么就重试
                if(exception instanceof NoHttpResponseException){
                    return true;
                } else if (exception instanceof ClientProtocolException){
                    return true;
                } 
                return false;
			}
		});
		return client;
	}

    
	public static ResponseHandler<String> stringHandler = new ResponseHandler<String>() {
		public String handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toString(entity);
			} else {
				return null;
			}
		}
	};
	
	public static ResponseHandler<byte[]> byteArrayHandler = new ResponseHandler<byte[]>() {
		public byte[] handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				return EntityUtils.toByteArray(entity);
			} else {
				return null;
			}
		}
	};
	
	@Override
    protected void finalize() throws Throwable {
    	super.finalize();
    	shutdown = true;
    }
	
	public static <T> T execute(
			final HttpClient httpClient,
            final HttpUriRequest request,
            final ResponseHandler<? extends T> responseHandler)
                throws Exception {
		try {
			return httpClient.execute(request, responseHandler);
		}
		catch (Exception e) {
			log.error("Error occured when execute request", e);
    		if (request != null) {
    			request.abort();
    		}
			throw e;
		}
	}
	
	public static String executeForBodyString(
			final HttpClient httpClient,
            final HttpUriRequest request)
                throws Exception {
		return execute(httpClient, request, stringHandler);
	}
	
	public static byte[] executeForBodyByteArray(
			final HttpClient httpClient,
            final HttpUriRequest request)
                throws Exception {
		return execute(httpClient, request, byteArrayHandler);
	}
    
	public void run() {
		long lastTime = System.currentTimeMillis();
		while(!shutdown){
			try{
				long currTime = System.currentTimeMillis();
				if((currTime - lastTime) >= EXPIRE_CHECK_PERIOD){
					pccm.closeExpiredConnections();
					pccm.closeIdleConnections(30, TimeUnit.SECONDS);
					lastTime = currTime;
					Thread.sleep(EXPIRE_CHECK_PERIOD);
					log.info("关闭过期连接");
				}else{
					Thread.sleep(EXPIRE_CHECK_PERIOD - (currTime - lastTime));
				}
			}catch(Throwable t){
				log.error("Error occured when loop for httpclient connection expire check", t);
			}
		}
		pccm.shutdown();
	}
    
}
