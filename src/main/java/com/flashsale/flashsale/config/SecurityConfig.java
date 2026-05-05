package com.flashsale.flashsale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/flash-sale/current").permitAll() // is public
                        .requestMatchers("/flash-sale/**").permitAll() // test
//                        .requestMatchers("/flash-sale/buy").authenticated() // data can change need auth in real case
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
