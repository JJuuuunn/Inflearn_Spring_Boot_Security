package Infleard.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 메소드 리턴되는 오브젝트를 Ioc로 등록
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // 해당 주소는 주소값이 들어오면 인증이 필요
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 해당 주소는 ADMIN 또는 MANAGER 권한이 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN')") //// 해당 주소는 ADMIN 권한이 필요
                .anyRequest().permitAll() // 나머지 주소는 전부 허용
                .and()
                .formLogin()
                .loginPage("/loginForm");
    }
}
