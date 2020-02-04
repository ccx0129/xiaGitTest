package cn.melina.license;

import java.util.HashMap;
import java.util.Map;

public class LicenseVerifyUtil {
	public static void main(String[] args){
		try {
			String path = "D:\\BusinessSystem\\license\\vparam.properties";
			Map<String, Object> verify = install("D:\\BusinessSystem\\license\\vparam.properties");
			Map<String, Object> verifyUtil = verify(path);
			System.out.println(verifyUtil.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * chenchunbao
	 * 2016年7月15日
	 * 安装证书
	 * @return
	 */
	public static Map<String,Object> install(String path){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		boolean result = false;
		String msg = "";
		VerifyLicense vLicense = new VerifyLicense();
		try {
			vLicense.install(path);
			result = true;
		} catch (LicenseException e) {
			result = false;
			msg = e.getMsg();
		} catch (Exception e){
			result = false;
			msg = "安装证书失败";
		}
		resultMap.put("errorMsg", msg);
		resultMap.put("result", result);
		return resultMap;
	}
	
	/**
	 * chenchunbao
	 * 2016年6月29日
	 * @param paramPath param.properties文件路径
	 */
	public static Map<String,Object> verify(String paramPath){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		boolean result = true;
		String msg = "";
		try {
			VerifyLicense vLicense = new VerifyLicense();
			Map<String,Object> map = vLicense.verify(paramPath);
			resultMap.putAll(map);
		} catch (LicenseException e) {
			result = false;
			msg = e.getMsg();
		} catch (Exception e){
			result = false;
			msg = "许可证书校验失败";
		}
		resultMap.put("errorMsg", msg);
		resultMap.put("result", result);
//		if(resultMap.containsKey("errorMsg")){
//			System.out.println(resultMap.get("errorMsg"));
//		}else{
//			System.out.println("有效期截止"+resultMap.get("endTime")+"，还剩余"+resultMap.get("remainTime")+"分钟");
//		}
		return resultMap;
	}
}
