package com.example.chatapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DBConfig {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//        dataSource.setUrl("jdbc:mysql://116.42.242.39:9906/study?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        dataSource.setUrl("jdbc:mysql://localhost:3306/study?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        return dataSource;
    }
}
