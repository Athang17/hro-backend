package com.eos.admin.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.eos.admin.serviceImpl.OurUserDetailsServiceImpl;
import com.eos.admin.serviceImpl.VendorDetailsServiceImpl;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	

	private OurUserDetailsServiceImpl ourUserDetailsServiceImpl;
	private VendorDetailsServiceImpl vendorDetailsService;
	private JWTAuthFilter jwtAuthFilter;
	
	
	public SecurityConfig(OurUserDetailsServiceImpl ourUserDetailsServiceImpl, JWTAuthFilter jwtAuthFilter,VendorDetailsServiceImpl vendorDetailsService) {
		super();
		this.ourUserDetailsServiceImpl = ourUserDetailsServiceImpl;
		this.vendorDetailsService= vendorDetailsService;
		this.jwtAuthFilter = jwtAuthFilter;
	}

	private static final String ROLE_ADMIN = "ADMIN";
	private static final String ROLE_USER = "USER";
	private static final String ROLE_MANAGER = "MANAGER";
	private static final String ROLE_VENDOR = "Vendor";
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf(AbstractHttpConfigurer::disable)
		.cors(Customizer.withDefaults())
		.authorizeHttpRequests(Request -> Request.requestMatchers("/auth/**", "/public/**","/auth/vendor/**").permitAll().
				 requestMatchers("/api/employees/createEmployee").permitAll()
				.requestMatchers("/admin/**").hasAnyAuthority(ROLE_ADMIN)
				.requestMatchers("/user/**").hasAnyAuthority(ROLE_USER,ROLE_MANAGER)
				.requestMatchers("/api/employees/**").hasAnyAuthority(ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
				.requestMatchers("/api/loi/**").hasAnyAuthority(ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
				.requestMatchers("/adminuser/**").hasAnyAuthority(ROLE_USER,ROLE_ADMIN,ROLE_MANAGER)
				.requestMatchers("/api/candi/**").permitAll()
				.requestMatchers("/api/vendorInfo/**").permitAll()
				.requestMatchers("/api/bankdetails/create/**").permitAll()
				.requestMatchers("/api/directors/**").permitAll()
				.anyRequest().authenticated())
		.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.authenticationProvider(authenticationProvider())
		.authenticationProvider(vendorAuthenticationProvider())
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(List.of("http://localhost:5173"));
	    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(List.of("*"));
	    configuration.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
	
   @Bean
   public AuthenticationProvider authenticationProvider() {
	   DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	   daoAuthenticationProvider.setUserDetailsService(ourUserDetailsServiceImpl);
	   daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
	   return daoAuthenticationProvider;
   }
	@Bean
	public AuthenticationProvider vendorAuthenticationProvider() {
     DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
     provider.setUserDetailsService(vendorDetailsService); 
     provider.setPasswordEncoder(passwordEncoder());
     return provider;
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
//		return authenticationConfiguration.getAuthenticationManager();
//	}
	@Bean
	@Primary
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(authenticationProvider())
				.authenticationProvider(vendorAuthenticationProvider())
				.build();
	}
	
	@Bean
	@Qualifier("vendorAuthenticationManager")
	public AuthenticationManager vendorAuthenticationManager() {
		return new ProviderManager(vendorAuthenticationProvider());
	}
}