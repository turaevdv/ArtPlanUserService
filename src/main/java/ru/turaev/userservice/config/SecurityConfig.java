package ru.turaev.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.turaev.userservice.enums.Permission;
import ru.turaev.userservice.securityutil.JwtConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String beginUrl = "/userservice/api/";
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()

                .antMatchers("/").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()

                .antMatchers(beginUrl + "auth/login").permitAll()
                .antMatchers(beginUrl + "auth/token-verification").permitAll()

                .antMatchers(beginUrl + "registration/any").hasAuthority(Permission.USERS_WRITE.getPermission())
                .antMatchers(beginUrl + "registration").permitAll()

                .antMatchers(HttpMethod.GET, beginUrl + "users", beginUrl + "users/{id}").hasAuthority(Permission.USERS_READ.getPermission())
                .antMatchers(HttpMethod.GET, beginUrl + "users/any/{id}", beginUrl + "users/any").hasAuthority(Permission.USERS_READ.getPermission())
                .antMatchers(HttpMethod.PUT, beginUrl + "users/{id}", beginUrl + "users/block/{id}", beginUrl + "users/unblock/{id}").hasAuthority(Permission.USERS_UPDATE.getPermission())
                .antMatchers(HttpMethod.DELETE, beginUrl + "users/{id}").hasAuthority(Permission.USERS_DELETE.getPermission())

                .and()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
