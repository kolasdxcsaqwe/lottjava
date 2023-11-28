package com.example.tt.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SiteConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    private JwtAuthorizationTokenFilter authorizationTokenFilter;


    @Override
    public void configure(WebSecurity web) throws Exception {
//        super.configure(web);
        web.ignoring().anyRequest();
//        web.ignoring().antMatchers( "/public/**","/**.ico","/chat/**","/**.html","/**.css","/**.js");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/chat/**").permitAll()
                .antMatchers("/lll/**").permitAll()
                .antMatchers("/NewChats/**").permitAll()
                .antMatchers("/UpdateChats/**").permitAll()
                .antMatchers("/sendChat/**").permitAll()
                .antMatchers("/changeTaskStatus/**").permitAll()
                .antMatchers("/checkTaskStatus/**").permitAll()
                .anyRequest().authenticated();
    }
}
