package cn.melina.license;
import java.lang.reflect.Method;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.xml.GenericCertificate;

/**
 * LicenseManager������
 * @author melina
 */
public class LicenseManagerHolder {
	
	private static LicenseManager licenseManager;
 
	public static synchronized LicenseManager getLicenseManager(LicenseParam licenseParams) {
    	if (licenseManager == null) {
    		licenseManager = new LicenseManager(licenseParams);
    	}
    	return licenseManager;
    }
	
	public static LicenseContent getLicenseContent() throws Exception{
		Method method = getMethod(LicenseManager.class, "getCertificate");
		method.setAccessible(true);
		Object obj = method.invoke(licenseManager);
		GenericCertificate g = (GenericCertificate)obj;
		LicenseContent content = (LicenseContent)g.getContent();
		return content;
	}
	
	private static Method getMethod(Class<?> clazz, String methodName) throws Exception {  
        Method method = null;  
        try {  
            method = clazz.getDeclaredMethod(methodName);  
        } catch (NoSuchMethodException e) {  
            try {  
                method = clazz.getMethod(methodName);  
            } catch (NoSuchMethodException ex) {  
                if (clazz.getSuperclass() == null) {  
                    return method;  
                } else {  
                    method = getMethod(clazz.getSuperclass(), methodName);  
                }  
            }  
        }  
        return method;  
    }  
}