package com.tencent.interact;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;

public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Value("${file.location.imagePath}")
    private String mImagesPath;
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大KB,MB
        factory.setMaxFileSize("1024MB");
        //设置总上传数据总大小
        factory.setMaxRequestSize("1024MB");
        return factory.createMultipartConfig();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(mImagesPath.equals("") || mImagesPath.equals("${file.location.imagePath}")){
               String imagesPath = WebAppConfig.class.getClassLoader().getResource("").getPath();
               if(imagesPath.indexOf(".jar")>0){
                imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
               }else if(imagesPath.indexOf("classes")>0){
                imagesPath = "file:"+imagesPath.substring(0, imagesPath.indexOf("classes"));
               }
               imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/"))+"/images/";
               mImagesPath = imagesPath;
              }
              LoggerFactory.getLogger(WebAppConfig.class).info("imagesPath="+mImagesPath);
              registry.addResourceHandler("/images/hand/**").addResourceLocations("file:/"+mImagesPath);

        // TODO Auto-generated method stub
        super.addResourceHandlers(registry);
    }
}