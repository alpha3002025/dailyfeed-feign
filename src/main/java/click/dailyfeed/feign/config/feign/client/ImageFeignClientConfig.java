package click.dailyfeed.feign.config.feign.client;

import click.dailyfeed.feign.domain.image.ImageFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageFeignClientConfig {
    @Value("${dailyfeed.services.image.feign.url}")
    private String imageServiceUrl;

    @Bean
    public ImageFeignClient imageFeignClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions
    ) {
        FeignDecorators feignDecorators = FeignDecorators.builder()
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .encoder(new JacksonEncoder(feignObjectMapper))
                .decoder(new JacksonDecoder(feignObjectMapper))
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(ImageFeignClient.class))
                .logLevel(feignLoggerLevel)
                .options(requestOptions)
                .requestInterceptor(template -> {
                    if ("POST".equals(template.method()) && !template.headers().containsKey("Content-Type")) {
                        template.header("Content-Type", "application/json");
                    }

                    if (template.headers().containsKey("Authorization")) {
                        template.headers().get("Authorization").forEach(value -> {
                            if (!value.startsWith("Bearer ")) {
                                template.removeHeader("Authorization");
                                template.header("Authorization", "Bearer " + value);
                            }
                        });
                    }
                })
                .target(ImageFeignClient.class, imageServiceUrl);
    }
}