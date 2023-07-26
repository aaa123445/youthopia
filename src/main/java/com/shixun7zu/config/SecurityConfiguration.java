package com.shixun7zu.config;

import com.alibaba.fastjson2.JSONObject;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.AuthorizeService;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private AuthorizeService authorizeService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   PersistentTokenRepository repository) throws Exception {
        return http
                .authorizeHttpRequests(aut->{
                    aut.requestMatchers("/api/auth/**").permitAll();
                    aut.anyRequest().authenticated();
                })
                .formLogin(conf->{
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler((request, response, authentication) ->{
                        response.setCharacterEncoding("utf-8");
                        response.getWriter().write(JSONObject.toJSONString(ResponseResult.okResult(200,"登陆成功")));
                    });
                    conf.failureHandler((request, response, exception) -> {
                        response.setCharacterEncoding("utf-8");
                        response.getWriter().write(JSONObject.toJSONString(ResponseResult.errorResult(501,exception.getMessage())));
                    });
                    conf.permitAll();
                })
                .logout(conf->{
                    conf.logoutUrl("/api/account/logout");
                    conf.permitAll();
                })
                .rememberMe(conf->{
                    conf.rememberMeParameter("remember");
                    conf.tokenRepository(repository);
                    conf.tokenValiditySeconds(3600 * 24 * 7);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(authorizeService)
                .exceptionHandling(conf-> conf.authenticationEntryPoint((request, response, authException) -> {
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(JSONObject.toJSONString(ResponseResult.errorResult(302,"内部错误，请联系管理员")));
                }))
                .build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository=new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

}
