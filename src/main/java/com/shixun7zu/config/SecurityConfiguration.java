package com.shixun7zu.config;

import com.alibaba.fastjson2.JSONObject;
import com.shixun7zu.entity.tool.ResponseResult;
import com.shixun7zu.service.AuthorizeService;
import com.shixun7zu.uilit.JwtToken;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@EnableWebSecurity
@Configuration
@Slf4j
public class SecurityConfiguration {

    @Resource
    private DataSource dataSource;

    @Resource
    private AuthorizeService authorizeService;



    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   PersistentTokenRepository repository) throws Exception {
        //添加转码
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        http.addFilterBefore(encodingFilter, CsrfFilter.class);

        return http
                .authorizeHttpRequests(aut -> {
                    aut.requestMatchers("/api/article/article-list",
                            "/api/article/article-images",
                            "/api/auth/**").permitAll();
                    aut.anyRequest().authenticated();
                })
                .formLogin(conf -> {
                    conf.loginProcessingUrl("/api/auth/login");
                    conf.successHandler((request, response, authentication) -> {
                        System.out.println(Arrays.toString(request.getCookies()));
                        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        log.info(user.getUsername()+":登陆成功"+new Date());
                        request.setCharacterEncoding("UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        String token = JwtToken.creatToken(user.getUsername());
                        response.getWriter().write(JSONObject.toJSONString(ResponseResult.okResult(200,"Login successful")));
                    });
                    conf.failureHandler((request, response, exception) -> {
                        request.setCharacterEncoding("UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(JSONObject.toJSONString(ResponseResult.errorResult(501, "username or password error")));
                    });
                    conf.permitAll();
                })
                .logout(conf -> {
                    conf.logoutUrl("/api/account/logout");
                    conf.logoutSuccessUrl(null);
                    conf.invalidateHttpSession(true);
                    conf.clearAuthentication(true);
                    conf.logoutSuccessHandler((request, response, exception) -> {
                        request.setCharacterEncoding("UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(JSONObject.toJSONString(ResponseResult.errorResult(200, "Logout successful")));
                    });
                    conf.permitAll();
                })
                .rememberMe(conf -> {
                    conf.rememberMeParameter("remember");
                    conf.tokenRepository(repository);
                    conf.tokenValiditySeconds(3600 * 24 * 7);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(authorizeService)
                .exceptionHandling(conf -> conf.authenticationEntryPoint((request, response, authException) -> {
                    request.setCharacterEncoding("UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter()
                            .write(JSONObject.toJSONString(ResponseResult.errorResult(302, "must login")));
                }))
                .cors(conf->{
//                    Customizer.withDefaults();
                    conf.configurationSource(corsConfigurationSource());
                })
                .build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);
        return jdbcTokenRepository;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setMaxAge(Duration.ofHours(1));
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
}
