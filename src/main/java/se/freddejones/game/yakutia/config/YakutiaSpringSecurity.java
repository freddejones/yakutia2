package se.freddejones.game.yakutia.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebSecurity
@EnableWebMvcSecurity
@Import(ScanningConfig.class)
public class YakutiaSpringSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    FeedSuccessHandler feedSuccessHandler;

    @Autowired
    YakutiaUserProvider userProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .anyRequest().hasRole("USER")
                .and()
                .csrf().disable()
                .openidLogin()
                    .attributeExchange("https://www.google.com/.*")
                    .attribute("email")
                    .type("http://axschema.org/contact/email")
                    .required(true)
                    .count(1)
                    .and()
                .and()
                    .permitAll()
                    .loginPage("/js/auth/login.html")
                    .failureUrl("/access_denied.html")
                    .loginProcessingUrl("/authentication/login/process")
                    .authenticationUserDetailsService(userProvider)
                    .successHandler(feedSuccessHandler)
                .and()
                    .logout()
                    .logoutUrl("/auth/logout");
    }

}
