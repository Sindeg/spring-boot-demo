package ru.alishev.firstsecurityapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alishev.firstsecurityapp.service.PersonDetailsService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    // Настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(authProvider);
        auth.userDetailsService(personDetailsService);
        // Spring сам найдет человека и сравнит его данные с данными из формы
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Конфигурируем сам Spring Security
        // Сюда поступает HTTP запрос
        // Сами данные с формы мы не обрабатываем, за нас это сделает Spring Security

        // Конфигурируем авторизацию - считается сверху - вниз, от более детальных к менее

        http.csrf().disable() // Отключаем защиту от межсайтовой подделки запросов
                .authorizeRequests() // Включаем авторизацию на доступ к страницам
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll() // Запросы на эти страницы доступны всем
                .anyRequest().authenticated()
                .and() // Была настройка авторизации, теперь настройка страницы логина
                .formLogin().loginPage("/auth/login") // Страница формы авторизации
                .loginProcessingUrl("/process_login") // Указываем адрес по которому Spring будет ждать данные
                .defaultSuccessUrl("/", true)
                .failureUrl("/auth/login?error");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
