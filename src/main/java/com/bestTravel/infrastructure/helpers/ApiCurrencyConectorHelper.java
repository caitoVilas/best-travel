package com.bestTravel.infrastructure.helpers;

import com.bestTravel.infrastructure.dto.CurrencyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Currency;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Component
@Slf4j
public class ApiCurrencyConectorHelper {

    private final WebClient webClient;
    @Value("${api.base-currency}")
    private String baseCurrency;
    private static final String BASE_CURRENCY_QUERY_PARAMETER = "base={baseCurrency}";
    private static final String SYMBOL_CURRENCY_QUERY_PARAMETER = "&symbols={symbol}";
    private static final String CURRENCY_PATH = "/fixer/latest";

    public ApiCurrencyConectorHelper(@Qualifier("currency") WebClient webClient) {
        this.webClient = webClient;
    }

    public CurrencyDTO getCurrency(Currency currency){
        return this.webClient
                .get()
                .uri(uri-> uri.path(CURRENCY_PATH).query(BASE_CURRENCY_QUERY_PARAMETER)
                        .query(SYMBOL_CURRENCY_QUERY_PARAMETER)
                        .build(baseCurrency, currency.getCurrencyCode()))
                .retrieve()
                .bodyToMono(CurrencyDTO.class)
                .block();
    }
}
