
package com.proasecal.software.web.config;


import com.proasecal.software.web.service.seguridad.CustomUserDetailsService;
import com.proasecal.software.web.service.seguridad.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class
SecurityConfig extends WebSecurityConfigurerAdapter {

    public SecurityConfig() {
        super();
    }

    @Autowired
    DataSource dataSource;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    PermisoService permisoService;


    @Configuration
    @Order(1)
    public static class SpecialSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and();
            //special
            //@formatter:off
            http
                    .csrf().disable()
                    .antMatcher("/web/**")
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/web/login")
                    .defaultSuccessUrl("/web/index")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/web/logout").permitAll().
                    logoutSuccessUrl("/web/login?logout").
                    deleteCookies("JSESSIONID").
                    invalidateHttpSession(true).
                    and().
                    headers().frameOptions().disable().and().
                    sessionManagement().
                    maximumSessions(1).
                    maxSessionsPreventsLogin(false).//El segundo saca al primero
                    expiredUrl("/401");
            //@formatter:on
        }
    }

    @Configuration
    @Order(2)
    public static class RegularSecurityConfig extends WebSecurityConfigurerAdapter {
        //regular
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and();
            //@formatter:off
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/images/**", "/css/**", "/js/**", "/controlexterno/images/**", "/controlexterno/css/**", "/controlexterno/js/**", "/vendors/**", "/build/**", "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/controlexterno/login")
                    .defaultSuccessUrl("/controlexterno/")
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/controlexterno/logout").permitAll().
                    logoutSuccessUrl("/controlexterno/login?logout").
                    deleteCookies("JSESSIONID").
                    invalidateHttpSession(true).
                    and().
                    headers().frameOptions().disable().and().
                    sessionManagement().
                    maximumSessions(2).
                    maxSessionsPreventsLogin(false).//El segundo saca al primero
                    expiredUrl("/401");
            //@formatter:on
        }
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {



        /*auth.inMemoryAuthentication()
                .withUser("admin").password("$2a$10$2Bh/CFmuoofz2uPdDsuw4.FgvlDwk10t905WhUZgkD.EFtLrx6gAO").roles("ADMIN")
                .and()
                .passwordEncoder(new BCryptPasswordEncoder());*/

        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers();
    }

/*    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        *//*auth.inMemoryAuthentication()
                .withUser("admin").password("$2a$10$2Bh/CFmuoofz2uPdDsuw4.FgvlDwk10t905WhUZgkD.EFtLrx6gAO").roles("ADMIN")
                .and()
                .passwordEncoder(new BCryptPasswordEncoder());*//*



    }*/

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


}
