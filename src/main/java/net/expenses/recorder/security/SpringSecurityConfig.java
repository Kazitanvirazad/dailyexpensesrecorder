package net.expenses.recorder.security;

import lombok.RequiredArgsConstructor;
import net.expenses.recorder.security.filter.JWTAuthenticationFilter;
import net.expenses.recorder.utils.CommonApiConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Kazi Tanvir Azad
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@PropertySource(value = {"classpath:security-config-${spring.profiles.active}.properties"})
public class SpringSecurityConfig implements CommonApiConstants {
    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Value("#{'${reactapp.url}'.split(',')}")
    private String[] REACTAPP_URLS;

    @Bean
    @Order(value = 1)
    public SecurityFilterChain jwtSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .securityMatcher(SPRING_SECURITY_JWT_ENDPOINTS)
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers(SPRING_SECURITY_JWT_ENDPOINTS).authenticated();
                    authorizationManagerRequestMatcherRegistry.anyRequest().permitAll();
                })
                .httpBasic(HttpBasicConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public FilterRegistrationBean<JWTAuthenticationFilter> jwtAuthenticationFilterRegistrationBean() {
        FilterRegistrationBean<JWTAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>(jwtAuthenticationFilter);
        filterRegistrationBean.setFilter(jwtAuthenticationFilter);
        filterRegistrationBean.addUrlPatterns(SPRING_SECURITY_JWT_ENDPOINTS);
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping(FILTER_MATCHER_PATTERN).allowedOrigins(REACTAPP_URLS)
                        .allowedMethods(ALLOWED_HTTP_METHODS);
            }
        };
    }
}
