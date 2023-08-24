package com.bestTravel.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    private static final String[] PUBLIC_RESOURCES = {"/fly/**","/hotel/**","/swagger-ui/**", "/.well-known/**, ", "/v3/api-docs/**"};
    private static final String[] USER_RESOURCES = {"/tour/**","/ticket/**","/reservation/**"};
    private static final String[] ADMIN_RESOURCES = {"/user/**", "/report/**"};
    private static final String LOGIN_RESOURCE = "/login";

    private static final String APPLICATION_OWNER = "caito";

    @Value("S{app.client.id}")
    private String clientid;
    @Value("${app.client.secret}")
    private String clientSecret;
    @Value("${{app.client-scope-read}")
    private String scopeRead;
    @Value("${app.client-scope-write}")
    private String scopeWrite;
    @Value("${app.client-redirect-debugger}")
    private String redirectUri;
    @Value("${app.client-redirect-spring-doc}")
    private String redirectUri2;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());
        http.exceptionHandling(e -> e.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_RESOURCE)));
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(BCryptPasswordEncoder bCryptPasswordEncoder){
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .and()
                .authorizeHttpRequests()
                .requestMatchers(PUBLIC_RESOURCES).permitAll()
                .requestMatchers(USER_RESOURCES)
                .authenticated()
                .requestMatchers(ADMIN_RESOURCES)
                .hasRole("ADMIN")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }

    @Bean
    public RegisteredClientRepository repository(BCryptPasswordEncoder bCryptPasswordEncoder){
        var regsiteredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientid)
                .clientSecret(bCryptPasswordEncoder.encode(clientSecret))
                .scope(scopeRead)
                .scope(scopeWrite)
                .redirectUri(redirectUri)
                .redirectUri(redirectUri2)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
        return new InMemoryRegisteredClientRepository(regsiteredClient);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .refreshTokenTimeToLive(Duration.ofHours(8))
                .build();
    }

    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter(){
        var converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("");
        return converter;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(
            JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter){
        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return converter;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(){
        var rsaKeys = generateRSA();
        var jwkSet = new JWKSet(rsaKeys);
        return ((jwkSelector, securityContext)-> jwkSelector.select(jwkSet));
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(){
        return context -> {
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)){
                context.getClaims().claims(claim ->{
                    claim.putAll(Map.of(
                            "owner",APPLICATION_OWNER,
                            "date_request", LocalDateTime.now().toString()
                    ));
                });
            }
        };
    }

    private static RSAKey generateRSA(){
        var keyPair = generateKeys();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID()
                .toString()).build();
    }

    private static KeyPair generateKeys(){
        KeyPair keyPair = null;
        try {
            var keypairGenerator = KeyPairGenerator.getInstance("RSA");
            keypairGenerator.initialize(2048);
            keyPair = keypairGenerator.generateKeyPair();
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keyPair;
    }
}
