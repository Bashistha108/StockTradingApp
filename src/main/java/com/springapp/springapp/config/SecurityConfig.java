package com.springapp.springapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// Configuration is used to define beans. Spring search for Beans in Configuration
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Used to handle successful authentication events
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(AuthenticationSuccessHandler customAuthenticationSuccessHandler, CustomUserDetailsService customUserDetailsService){
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    // Method will return a bean that should be managed by Spring context
    // Security FilterChain configures the securtiy filter chain using HttpSecurity Object for Http requests
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(configurer -> configurer
                        .requestMatchers("/admin/**","/manage-users","/users/delete-user/**").hasRole("Admin")

                        //to allow the css files to render properly
                        .requestMatchers( "/js/**", "/images/**","/css/**", "/static/**", "/templates/**").permitAll()

                        .requestMatchers("/portfolio/**","/stocks/**","/users/**","/", "/search/**").hasAnyRole("Admin", "Trader")

                        .requestMatchers("/login-page", "/access-denied","/transactions/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login-page")
                        .loginProcessingUrl("/authenticateTheUser")
                        .successHandler(customAuthenticationSuccessHandler)  // When authentication successfull go the customAuthenticationSuccessHandler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Define logout URL (default is "/logout")
                        .logoutSuccessUrl("/login-page?logout") // Redirect after successful logout
                        .invalidateHttpSession(true) // Invalidate session
                        .deleteCookies("JSESSIONID") // Remove session cookie
                        .permitAll() // Allow all users to log out
                )
                .exceptionHandling(configurer -> configurer .accessDeniedPage("/access-denied"))
                .csrf(csrf->csrf.disable());

        return http.build();
    }

    /**
     * Autowired is automatically called by Spring Security during initialization process
     * auth.userDetailsService(CustomUserDetailsService) registers CustomUserDetailsService with Spring Security
     * Ensures Spring uses custom logic to load user details
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(customUserDetailsService);
    }


    //Creating a bean so that can be used just by dependency injection
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

