package com.chaos.taco.config;

import com.chaos.taco.service.UserRepositoryUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Autowired
//    private DataSource dataSource;
//
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        //使用jdbc存储
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
//        manager.setDataSource(dataSource);
//        if (!manager.userExists("zhangsan")){
//            manager.createUser(User.withUsername("zhangsan").password(passwordEncoder().encode("123")).roles("admin").build());
//        }
//        if (!manager.userExists("lisi")){
//            manager.createUser(User.withUsername("lisi").password(passwordEncoder().encode("456")).roles("admin").build());
//        }
//        return manager;
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/design","/orders")
                .hasRole("USER")
                .antMatchers("/","/**").permitAll()
                .and().csrf().ignoringAntMatchers("/","/**") // /h2-console/添加到csrf防护忽略的路径集合内
                .and().headers().frameOptions().sameOrigin() //修改spring security对iframe/frame的安全策略
                .and().formLogin().loginPage("/login").defaultSuccessUrl("/design",true)
                .loginProcessingUrl("/authenticate").usernameParameter("username").passwordParameter("password")
                ;
//                .and().logout().logoutSuccessUrl("/");
    }

    @Autowired
    private UserRepositoryUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用内存存储
//        auth.inMemoryAuthentication()
//                .withUser("Tom")
//                .password(passwordEncoder().encode("cat"))
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("Jerry")
//                .password(passwordEncoder().encode("race"))
//                .authorities("ROLE_USER");

        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());


    }
}
