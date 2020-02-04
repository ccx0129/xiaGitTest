package cn.melina.license;

public class LicenseException extends Exception{
	private static final long serialVersionUID = 1L;
	private String msg;
	public LicenseException(String msg){
		this.msg = msg;
	}
	public String getMsg() {
		return msg;
	}
}
