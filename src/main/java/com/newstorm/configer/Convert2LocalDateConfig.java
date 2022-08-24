package com.newstorm.configer;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Configuration
public class Convert2LocalDateConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(localDateConvert());
    }

    public Converter<String, LocalDate> localDateConvert() {
        // 不能替换为lambda表达式
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if (StringUtils.hasText(source)) {
                    System.out.println("source: " + source);
                    try {
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern(
                                "yyyy-MM-dd"));
                    } catch (DateTimeParseException e) {
                        return LocalDate.parse(source, DateTimeFormatter.ofPattern(
                                "yyyy/MM/dd"));
                    }

                }
                return null;
            }
        };
    }
}
