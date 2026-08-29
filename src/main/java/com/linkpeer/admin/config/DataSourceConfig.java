package com.linkpeer.admin.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {

    @Bean
    public static BeanPostProcessor hikariDataSourceCustomizer() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof HikariDataSource hikari) {
                    String url = hikari.getJdbcUrl();
                    if (url != null && url.contains("postgresql")) {
                        if (!url.contains("prepareThreshold=")) {
                            url += (url.contains("?") ? "&" : "?") + "prepareThreshold=0";
                        }
                        if (!url.contains("preferQueryMode=")) {
                            url += (url.contains("?") ? "&" : "?") + "preferQueryMode=simple";
                        }
                        hikari.setJdbcUrl(url);
                    }
                    hikari.addDataSourceProperty("prepareThreshold", "0");
                    hikari.addDataSourceProperty("preferQueryMode", "simple");
                }
                return bean;
            }
        };
    }
}
