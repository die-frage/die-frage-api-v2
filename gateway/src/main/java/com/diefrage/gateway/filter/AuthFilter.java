package com.diefrage.gateway.filter;

import com.diefrage.exceptions.TypicalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RoutValidator validator;

    @Autowired
    private RestTemplate restTemplate;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            System.out.println(exchange.getRequest().getPath());
            System.out.println(exchange.getRequest().getHeaders());
            System.out.println(exchange.getRequest().getCookies());
            System.out.println(exchange.getRequest().getId());
            System.out.println(exchange.getRequest().getLocalAddress());
            System.out.println(exchange.getRequest().getQueryParams());
            System.out.println(exchange.getRequest().getRemoteAddress());
            System.out.println(exchange.getRequest().getSslInfo());
            System.out.println(exchange.getRequest().getBody());
            System.out.println(exchange.getRequest().getMethod());

            if (validator.isSecured.test(exchange.getRequest())) {
                // header contains or not token
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    TypicalServerException.USER_NOT_FOUND.throwException();
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try{
                    String username = restTemplate.getForObject("http://localhost:8010/auth/validate?token=" + authHeader, String.class);
                    System.out.println(username);

                    // Добавляем имя пользователя в заголовок
                    ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                            .header("X-Username", username) // здесь "X-Username" - имя заголовка, в котором будет храниться имя пользователя
                            .build();

                    // Передаем измененный запрос в цепочку фильтров
                    return chain.filter(exchange.mutate().request(modifiedRequest).build());
                }
                catch (Exception e){
                    TypicalServerException.USER_NOT_FOUND.throwException();
                }

            }
            return chain.filter(exchange.mutate().build());
        }));
    }

    public static class Config { }
}