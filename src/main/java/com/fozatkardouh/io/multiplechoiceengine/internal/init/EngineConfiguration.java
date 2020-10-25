package com.fozatkardouh.io.multiplechoiceengine.internal.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class EngineConfiguration {

    @Value("${engine.language}")
    private String language;
    @Value("${engine.country}")
    private String country;

    @Bean
    public Locale locale() {
        return new Locale(language, country);
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(locale());
        return slr;
    }

}
