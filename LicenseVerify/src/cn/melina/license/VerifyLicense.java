package cn.melina.license;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * 
 * @author chenchunbao
 *
 */
public class VerifyLicense {
	private static String PUBLICALIAS = "";
	private static String STOREPWD = "";
	private static String SUBJECT = "";
	private static String licPath = "";
	private static String pubPath = "";
	private static LicenseManager licenseManager = null;
	
	/**
	 * chenchunbao
	 * 2016年6月29日
	 * @param propertiesPath
	 * 核实
	 */
	public Map<String,Object> verify(String propertiesPath) throws LicenseException{
		//init(propertiesPath);
		System.out.println("校验："+licenseManager.hashCode());
		return check();
	}
	
	public Map<String,Object> install(String propertiesPath)throws LicenseException{
		init(propertiesPath);
		//System.out.println("安装："+licenseManager.hashCode());
		installLicense();
		return null;
	}
	
	/**
	 * 初始化参数
	 * chenchunbao
	 * 2016年7月15日
	 * @param propertiesPath
	 * @throws LicenseException
	 */
	private void init(String propertiesPath)throws LicenseException{
		setParam(propertiesPath);
		LicenseParam param = initLicenseParams();
		licenseManager = (LicenseManager) LicenseManagerHolder.getLicenseManager(param);
	}
	
	
	
	/**
	 * 加载参数
	 * chenchunbao
	 * 2016年6月29日
	 * @param propertiesPath
	 */
	private void setParam(String propertiesPath) throws LicenseException{
		// 获取参数
		Properties prop = new Properties();
		try {
			InputStream is = new FileInputStream(new File(propertiesPath));
			prop.load(is);
		} catch (Exception e) {
			throw new LicenseException("许可证书参数加载出错，请检查参数配置");
		} 
		PUBLICALIAS = prop.getProperty("PUBLICALIAS");
		STOREPWD = prop.getProperty("STOREPWD");
		SUBJECT = prop.getProperty("SUBJECT");
		licPath = prop.getProperty("licPath");
		pubPath = prop.getProperty("pubPath");
	}
	
	/**
	 * chenchunbao
	 * 2016年6月29日
	 * 安装证书
	 */
	private void installLicense()
		throws LicenseException{
		try {
			licenseManager.install(new File(licPath));
		} catch (Throwable e) {
			//e.printStackTrace();
			String msg = e.getMessage();
			if("exc.licenseHasExpired".equals(msg)){
				msg = "许可证书已经过期";
			}else{
				msg = "许可证书安装失败";
			}
			throw new LicenseException(msg);
		}
	}
	
	/**
	 * chenchunbao
	 * 2016年6月29日
	 * 校验证书是否过期及有效所剩时间
	 * @return
	 */
	private Map<String,Object> check() 
		throws LicenseException{		
		Map<String,Object> result = new HashMap<String,Object>();
		
		// 验证证书
		try {
			licenseManager.verify();
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			if("exc.licenseHasExpired".equals(msg)){
				msg = "许可证书已经过期";
			}else{
				msg = "许可证书验证不通过";
			}
			throw new LicenseException(msg);
		}
			
		LicenseContent content = null;
		try {
			content = LicenseManagerHolder.getLicenseContent();
		} catch (Exception e) {
			
		}
		if(content!=null){
			if(!content.getExtra().equals("BH.139;qgl:uay-s78'11dh11")){
				throw new LicenseException("许可证书验证不通过,请使用正确的许可证书");
			}
		
			/**
			 * 计算剩余时间
			 */
			Date d = content.getNotAfter();
//			Date current = new Date();
//			long remainTime = (d.getTime()-current.getTime())/1000/60;
//			result.put("remainTime", remainTime);
			result.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d));
		}else{
			throw new LicenseException("验证失败：证书内容为空");
		}
		
		return result;
	}

	// 返回验证证书需要的参数
	private LicenseParam initLicenseParams() {
		Preferences preference = Preferences
				.userNodeForPackage(VerifyLicense.class);
		CipherParam cipherParam = new DefaultCipherParam(STOREPWD);

		KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
				VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
		LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
				preference, privateStoreParam, cipherParam);
		return licenseParams;
	}
}