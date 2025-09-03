package com.preclinical.platform.preclinicaldataplatform.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;  // This is the User import you need
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;  // Import for Customizer

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/studies/**").hasRole("VIEWER")
                .requestMatchers(HttpMethod.POST, "/api/studies/**").hasRole("STUDY_MANAGER")
                .requestMatchers("/api/patients/**").hasRole("CLINICAL_COORDINATOR")
                .requestMatchers("/api/analysis/**").hasRole("DATA_ANALYST")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults()); // Now this will work
            
        // Allow H2 console frames - corrected method name
        http.headers(headers -> headers.frameOptions().sameOrigin());
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        // In production, this would connect to your user management system
        UserDetails studyManager = User.builder()
                .username("study_manager")
                .password(passwordEncoder().encode("password"))
                .roles("STUDY_MANAGER", "VIEWER")
                .build();
                
        UserDetails clinicalCoordinator = User.builder()
                .username("clinical_coord")
                .password(passwordEncoder().encode("password"))
                .roles("CLINICAL_COORDINATOR", "VIEWER")
                .build();
                
        UserDetails dataAnalyst = User.builder()
                .username("data_analyst")
                .password(passwordEncoder().encode("password"))
                .roles("DATA_ANALYST", "VIEWER")
                .build();
        
        return new InMemoryUserDetailsManager(studyManager, clinicalCoordinator, dataAnalyst);
    }
}