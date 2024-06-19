package cn.ninghan.common.utils;

import org.apache.logging.log4j.util.PropertySource;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;

//public class YamlSourceFactory extends DefaultPropertySourceFactory {
//        @Override
//        public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
//            if (resource == null) {
//                return super.createPropertySource(name, resource);
//            }
//
//            // 这里使用Yaml配置加载类来读取yml文件信息
//            List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
//            return sources.get(0);
//        }
//}
