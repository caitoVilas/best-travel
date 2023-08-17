package com.bestTravel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author claudio.vilas
 * date: 08/2023
 */

@Configuration
@PropertySource(value = "classpath:configs/api_currency.properties")
public class PropertyConfig {
}
