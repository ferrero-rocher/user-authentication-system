package com.example.userauthenticationsystem;

import com.example.userauthenticationsystem.entity.UserEntity;
import com.example.userauthenticationsystem.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class UserAuthenticationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthenticationSystemApplication.class, args);
	}



}
