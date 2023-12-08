package com.example.tt.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private JwtAuthorizationTokenFilter authorizationTokenFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //省略HttpSecurity的配置
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/chat/**").permitAll()
//                .antMatchers("/hello/**").permitAll()
                .antMatchers("/NewChats/**").permitAll()
                .antMatchers("/UpdateChats/**").permitAll()
                .antMatchers("/sendChat/**").permitAll()
                .antMatchers("/changeTaskStatus/**").permitAll()
                .antMatchers("/checkTaskStatus/**").permitAll()
                .antMatchers("/robotBet/**").permitAll()
                .antMatchers("/hello").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->web.ignoring().anyRequest();
    }



//    @Override
//    public void configure(WebSecurity web) throws Exception {
////        super.configure(web);
//        web.ignoring().anyRequest();
////        web.ignoring().antMatchers( "/public/**","/**.ico","/chat/**","/**.html","/**.css","/**.js");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .cors().and()
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .antMatchers("/chat/**").permitAll()
////                .antMatchers("/hello/**").permitAll()
//                .antMatchers("/NewChats/**").permitAll()
//                .antMatchers("/UpdateChats/**").permitAll()
//                .antMatchers("/sendChat/**").permitAll()
//                .antMatchers("/changeTaskStatus/**").permitAll()
//                .antMatchers("/checkTaskStatus/**").permitAll()
//                .antMatchers("/robotBet/**").permitAll()
//                .antMatchers("/hello").permitAll()
//                .anyRequest().authenticated();
//    }


}
