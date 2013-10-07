package cn.ohyeah.gameserver.model;

import cn.ohyeah.gameserver.util.BytesUtil;
import io.netty.buffer.ByteBuf;

public class User {


	private String name;

	private String password;

	private String tel;

	private String email;

	private String area;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void write(ByteBuf buff){
		BytesUtil.writeString(buff, name);
		BytesUtil.writeString(buff, password);
		BytesUtil.writeString(buff, tel);
		BytesUtil.writeString(buff, email);
		BytesUtil.writeString(buff, area);
	}
	
	public void read(ByteBuf buff){
		this.setName(BytesUtil.readString(buff));
		this.setPassword(BytesUtil.readString(buff));
		this.setTel(BytesUtil.readString(buff));
		this.setEmail(BytesUtil.readString(buff));
		this.setArea(BytesUtil.readString(buff));
	}

	@Override
	public String toString() {
		return "\nname:"+this.getName()+"\npassword:"+this.getPassword()+"\ntel:"
				+this.getTel()+"\nemail:"+this.getEmail()+"\narea:"+this.getArea();
	}
	
	
}
