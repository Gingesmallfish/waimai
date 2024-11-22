package com.cqeec.waimai.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemConfig {
    //定义AES密钥，长度16位
    public static final String key = "abcdwxyz19876420";

    public static String getPropertiesByName(String name){
        Properties props = new Properties();
        InputStream in = SystemConfig.class.getResourceAsStream("/db.properties");
        try {
            props.load(in);
            in.close();
            // 读取特定属性
            return props.getProperty(name);
        } catch (IOException e) {
            return "";
        }
    }
}
