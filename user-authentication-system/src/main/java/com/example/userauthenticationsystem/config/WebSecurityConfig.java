package com.example.userauthenticationsystem.config;

import com.example.userauthenticationsystem.service.JpaUserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    private final JpaUserDetailsServiceImpl jpaUserDetailsServiceImpl;

    public WebSecurityConfig(JpaUserDetailsServiceImpl jpaUserDetailsServiceImpl) {
        this.jpaUserDetailsServiceImpl = jpaUserDetailsServiceImpl;
    }



    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(11);
    }




    /*@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()

                .antMatchers("/**").permitAll();
        return http.build();
    }*/

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                //.csrf(csrf -> csrf.ignoringAntMatchers("/api/v1/**"))
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests(auth -> auth
                        .antMatchers("/api/v1/**").permitAll()
                        .antMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .userDetailsService(jpaUserDetailsServiceImpl)
                .headers(headers->headers.frameOptions().sameOrigin())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
