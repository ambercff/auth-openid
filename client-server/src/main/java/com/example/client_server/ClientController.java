package com.example.client_server;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {

    WebClient webClient;

    public ClientController(WebClient.Builder builder){
        this.webClient = builder
                .baseUrl("http://127.0.0.1:9090")
                .build();
    }

    @GetMapping("/home")
    public Mono<String> home(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client,
                             @AuthenticationPrincipal OidcUser oidcUser) {
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();
        String idToken = oidcUser.getIdToken().getTokenValue();
        String claims = oidcUser.getClaims().toString();

        return Mono.just(String.format("""
        <h2> Access Token: %s </h2>
        <h2> Refresh Token: %s </h2>
        <h2> Id Token: %s </h2>
        <h2> Claims: %s </h2>
          """, accessToken, refreshToken, idToken, claims));
    }

    @GetMapping("/tasks")
    public Mono<String> getTasks(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client){
        String accesToken = client.getAccessToken().getTokenValue();

        return webClient
                .get()
                .uri("tasks")
                .header("Authorization", "Bearer %s"
                        .formatted(accesToken))
                .retrieve()
                .bodyToMono(String.class);
    }
}