package com.ceas.springbootsecurtity.app;


import com.ceas.springbootsecurtity.app.models.service.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.sql.DataSource;


//@EnableGlobalAuthentication(securedEnabled=true)
//este se usa cuando se validad desde el controlador @Secured("USER")

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {
//   @Autowired
//    private LoginSuccesHandler successHandler;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JpaUserDetailsService userDetailService;

  
//@Autowired
//public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
//
//    build.jdbcAuthentication()
//            .dataSource(dataSource)
//            .passwordEncoder(passwordEncoder())
//            .usersByUsernameQuery("select username, password, enabled from users where username=?")
//            .authoritiesByUsernameQuery("select u.username, a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");
//}

//      @Bean
//      public AuthenticationManager authManager(){
//          DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
//          daoProvider.setUserDetailsService(userDetailService);
//          return new ProviderManager((daoProvider));
//      }

    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
    }

//    @Bean
//    public UserDetailsService userDetailsService()throws Exception{
//
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User
//                .withUsername("user")
//                .password(passwordEncoder().encode("user"))
//                .roles("USER")
//                .build());
//        manager.createUser(User
//                .withUsername("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN","USER")
//                .build());
//
//        return manager;
//    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        RequestMatcher publicMatchers = new OrRequestMatcher(
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/css/**"),
                new AntPathRequestMatcher("/js/**"),
                new AntPathRequestMatcher("/images/**"),
                new AntPathRequestMatcher("/listar**"),
                new AntPathRequestMatcher("/api/clientes/**"));


        http
                .authorizeHttpRequests(authorize -> {
                            try {
                                authorize
                                        .requestMatchers("/").permitAll()
                                        .requestMatchers("/css/**").permitAll()
                                        .requestMatchers("/js/**").permitAll()
                                        .requestMatchers("/images/**").permitAll()
                                        .requestMatchers("/listar").permitAll()
                                        .requestMatchers("/clientes").permitAll()
                                        .anyRequest().authenticated()
                                ;

                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                )
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(e ->{
                    e.accessDeniedPage("/error_403");
                })
                .rememberMe(Customizer.withDefaults());


        return http.build();
    }

//    @Bean
//    public LoginSuccesHandler successHandler() {
//        return new LoginSuccesHandler();
//    }
    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}