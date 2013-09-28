package cn.ohyeah.gameserver.protocol;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Map;

public class ProcessContext {
	private HeadWrapper head;
    private int resultCode;
    private String errorMessage;
    private ByteBuf request;
    private ByteBuf response;
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        return params;
    }

    public HeadWrapper getHead() {
		return head;
	}

	public void setHead(HeadWrapper head) {
		this.head = head;
	}

	public ByteBuf getRequest() {
		return request;
	}

	public void setRequest(ByteBuf request) {
		this.request = request;
	}

	public ByteBuf createResponse(int size) {
        if (response == null) {
            response = Unpooled.buffer(size);
        }
        return response;
    }

    public ByteBuf getResponse() {
        return response;
    }

    public void setResponse(ByteBuf response) {
        this.response = response;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
