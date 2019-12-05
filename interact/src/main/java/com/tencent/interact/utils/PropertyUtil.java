package com.tencent.interact.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
                e.printStackTrace();
            } catch (IOException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            return properties.getProperty(key);
        }
        return properties.getProperty(key);
    }
}
