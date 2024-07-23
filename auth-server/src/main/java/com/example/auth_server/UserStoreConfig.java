package com.example.auth_server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class UserStoreConfig {
    @Bean
    // Consultar usuários no processo de autenticação
    UserDetailsService userDetailsService(){
        // Configuração simples de usuário, apenas para poder logar
        var userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("user").password("{noop}password").roles("USER").build());
        return userDetailsManager;
    }
}
