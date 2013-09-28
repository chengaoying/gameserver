package cn.ohyeah.gameserver.protocol;

/**
 * 协议头包装类
 * @author Administrator
 *
 */
public class HeadWrapper implements IHeadAccessor {

	private int head;

    public HeadWrapper() {}

    public HeadWrapper(int head) {
        this.head = head;
    }

	public int getHead() {
		return head;
	}
	public void setHead(int head) {
		this.head = head;
	}

    /**
     * 版本号
     * 长度3位
     * @return
     */
	public int getVersion() {
		return (head>>29)&0x07;
	}
	public void setVersion(int version) {
		head &= 0x1FFFFFFF;
		head |= version<<29;
	}

    /**
     * 对原数据或者加密前的数据进行填充（AES加密是16字节对齐）。
     * 长度1位
     * 0:无填充
     * 1:16字节填充
     * @return
     */
	public int getPadding() {
		return (head>>28)&0x01;
	}
	public void setPadding(int padding) {
		head &= 0xEFFFFFFF;
		head |= padding<<28;
	}

    /**
     * 表明本数据报所加载的数据是否经过分块发送。
     * 长度2位
     * 0:单数据报，未经分块
     * 1:分块，第一个数据报
     * 2:分块，其它数据报
     * 3:未定义
     * @return
     */
	public int getSplit() {
		return (head>>26)&0x03;
	}
	public void setSplit(int split) {
		head &= 0xF3FFFFFF;
		head |= split<<26;
	}

    /**
     * 标识本数据报是否经过加密。加密方法由系统配置指定。数据报所使用的加密方法由协议本身确定。
     * 长度1位
     * 0:非加密
     * 1:加密
     * @return
     */
	public int getCrypt() {
		return (head>>25)&0x01;
	}
	public void setCrypt(int crypt) {
		head &= 0xFDFFFFFF;
		head |= crypt<<25;
	}

    /**
     * 表示本数据报是否经过压缩。
     * 长度1位
     * 0:表示未压缩
     * 1:表示压缩
     * @return
     */
	public int getCompress() {
		return (head>>24)&0x01;
	}
	public void setCompress(int compress) {
		head &= 0XFEFFFFFF;
		head |= compress<<24;
	}

    /**
     * 1表示接收到此数据时应该返回应答(UDP有效)。
     * 长度1位
     * @return
     */
	public int getAck() {
		return (head>>23)&0x01;
	}
	public void setAck(int ack) {
		head &= 0XFF7FFFFF;
		head |= ack<<23;
	}

    /**
     * 1表示协议要求返回参数仅为应答包。
     * 长度1位
     * @return
     */
	public int getAckparam() {
		return (head>>22)&0x01;
	}
	public void setAckparam(int ackparam) {
		head &= 0XFFBFFFFF;
		head |= ackparam<<22;
	}

    /**
     * 当传输会话数据时表示会话类型。
     * 而当传输数据为媒体数据时表示数据帧类型。
     * 长度2位
     * @return
     */
	public int getType() {
		return (head>>20)&0x03;
	}
	public void setType(int type) {
		head &= 0XFFCFFFFF;
		head |= type<<20;
	}

    /**
     * 控制协议标识。
     * 长度4位
     * @return
     */
	public int getTag() {
		return (head>>16)&0x0f;
	}
	public void setTag(int tag) {
		head &= 0XFFF0FFFF;
		head |= tag<<16;
	}

    /**
     * 控制协议命令。
     * 长度8位
     * @return
     */
	public int getCommand() {
		return (head>>8)&0xff;
	}
	public void setCommand(int command) {
		head &= 0XFFFF00FF;
		head |= command<<8;
	}

    /**
     * 用户数据。
     * 长度8位
     * @return
     */
	public int getUserdata() {
		return head&0xff;
	}
	public void setUserdata(int userdata) {
		head &= 0XFFFFFF00;
		head |= userdata;
	}
	
	public static class Builder {
		private byte version;
		private byte padding;
		private byte split;
		private byte crypt;
		private byte compress;
		private byte ack;
		private byte ackparam;
		private byte type;
		private byte tag;
		private byte command;
		private byte userdata;
		
		public Builder version(int version) {
			this.version = (byte)version;
			return this;
		}
		
		public Builder padding(int padding) {
			this.padding = (byte)padding;
			return this;
		}
		
		public Builder split(int split) {
			this.split = (byte)split;
			return this;
		}
		
		public Builder crypt(int crypt) {
			this.crypt = (byte)crypt;
			return this;
		}
		
		public Builder compress(int compress) {
			this.compress = (byte)compress;
			return this;
		}
		
		public Builder ack(int ack) {
			this.ack = (byte)ack;
			return this;
		}
		
		public Builder ackparam(int ackparam) {
			this.ackparam = (byte)ackparam;
			return this;
		}
		
		public Builder type(int type) {
			this.type = (byte)type;
			return this;
		}
		
		public Builder tag(int tag) {
			this.tag = (byte)tag;
			return this;
		}
		
		public Builder command(int command) {
			this.command = (byte)command;
			return this;
		}
		
		public Builder userdata(int userdata) {
			this.userdata = (byte)userdata;
			return this;
		}
		
		public HeadWrapper build() {
			HeadWrapper head = new HeadWrapper();
			head.setVersion(version);
			head.setPadding(padding);
			head.setSplit(split);
			head.setCrypt(crypt);
			head.setCompress(compress);
			head.setAck(ack);
			head.setAckparam(ackparam);
			head.setType(type);
			head.setTag(tag);
			head.setCommand(command);
			head.setUserdata(userdata);
			return head;
		}
	}

}
