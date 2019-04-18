package ru.sua.security.resource.server;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * включаем ограничения доступа к аннотированным {@code @Secured} методам REST Controller согласно полученным
 * ограничениям(authorization, roles) из OAuth2 token, с которым клиентское приложение обращается к
 * rest ресурсу
 */
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class WebSecurityConfiguration {
}
