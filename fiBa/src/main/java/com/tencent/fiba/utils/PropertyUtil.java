package com.tencent.fiba.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
* @Description TODO properties的工具类
* @Author healen
* @Date 2019/6/12 17:25
*/
public class PropertyUtil {

    private static final Logger log = LoggerFactory.getLogger(PropertyUtil.class);

    private static Properties properties;

    public static String getProperty(String key) {
        if (properties == null) {//双重锁模式  
            synchronized (PropertyUtil.class) {
                if (properties == null) {
                    properties = new Properties();
                }
            }
            try {
                InputStream stream = PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties");
                properties.load(stream);
                stream.close();
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
                //e.printStackTrace();
            } catch (IOException e) {
                log.error(e.getMessage());
                //e.printStackTrace();
            }
            return properties.getProperty(key);
        }
        return properties.getProperty(key);
    }
}
