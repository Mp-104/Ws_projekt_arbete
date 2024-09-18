package com.example.projekt_arbete.config;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Component;
import io.github.resilience4j.ratelimiter.RateLimiter;

import java.time.Duration;

//@EnableWebSecurity
@Configuration
public class Security {


//    @Bean
//    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
//        http.csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(
//                    authorizeRequests -> authorizeRequests.anyRequest().permitAll());
//
//        return http.build();
//
//    }

    @Bean
    public RateLimiter rateLimiter () {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod (100) // Max requests per interval
                .limitRefreshPeriod (Duration.ofSeconds(10)) // Interval duration
                .timeoutDuration (Duration.ofSeconds(5)) // Timeout for acquiring permits
                .build();
        return RateLimiter.of("myRateLimiter" , config);
    }


}
