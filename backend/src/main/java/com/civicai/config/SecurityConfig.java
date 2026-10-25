package com.civicai.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
@Configuration public class SecurityConfig {
 @Bean PasswordEncoder encoder(){return new BCryptPasswordEncoder();}
 @Bean UserDetailsService users(@Value("${civicai.admin.username}") String user,@Value("${civicai.admin.password}") String pass,PasswordEncoder encoder){return new InMemoryUserDetailsManager(User.withUsername(user).password(encoder.encode(pass)).roles("OFFICER").build());}
 @Bean SecurityFilterChain security(HttpSecurity http,@Value("${civicai.allowed-origin}") String origin) throws Exception {
  CorsConfiguration cors=new CorsConfiguration(); cors.setAllowedOrigins(List.of(origin));cors.setAllowedMethods(List.of("GET","POST","PUT","OPTIONS"));cors.setAllowedHeaders(List.of("Authorization","Content-Type"));
  UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();source.registerCorsConfiguration("/**",cors);
  return http.csrf(csrf->csrf.disable()).cors(c->c.configurationSource(source)).authorizeHttpRequests(a->a.requestMatchers(HttpMethod.OPTIONS,"/**").permitAll().requestMatchers(HttpMethod.POST,"/api/cases").permitAll().requestMatchers(HttpMethod.GET,"/api/cases/track/**").permitAll().requestMatchers("/api/officer/**").hasRole("OFFICER").anyRequest().denyAll()).httpBasic(Customizer.withDefaults()).build();
 }
}
