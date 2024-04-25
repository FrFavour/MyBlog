package com.my.blog.config;

import com.my.blog.exception.handler.AccessDeniedHandlerImpl;
import com.my.blog.exception.handler.AuthenticationEntryPointImpl;
import com.my.blog.filter.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 允许跨域
                .cors().and()
                // 关闭csrf
                .csrf().disable()
                // 不通过Session获取SecurityContext

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 对于登录接口 允许匿名访问
                .antMatchers("/login").anonymous()
                //注销接口需要认证才能访问
                .antMatchers("/logout").authenticated()
                //发表评论需要认证才能访问
                .antMatchers("/comment").authenticated()
                //个人信息接口必须登录后才能访问
                .antMatchers("/user/userInfo").authenticated()
                // 除上面外的所有请求全部不需要认证即可访问
                .anyRequest().permitAll();

        // 禁用默认登出界面
        http.logout().disable();

        // 注册jwt过滤器
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 注册自定义异常处理
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler);

        http.logout().disable();
        return http.build();
    }
}
