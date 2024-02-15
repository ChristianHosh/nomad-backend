package com.nomad.socialspring.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebCorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(@NotNull CorsRegistry corsRegistry) {
    corsRegistry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*")
            .maxAge(3600L)
            .allowedHeaders("*")
            .allowCredentials(false);

  }
}
