package click.dailyfeed.feign.config.feign.health;

import click.dailyfeed.feign.domain.health.ActivityHealthCheckClient;
import click.dailyfeed.feign.domain.health.ContentHealthCheckClient;
import click.dailyfeed.feign.domain.health.ImageHealthCheckClient;
import click.dailyfeed.feign.domain.health.TimelineHealthCheckClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Health Check 전용 Feign Client 설정
 * 각 서비스의 /healthcheck/live 엔드포인트만 호출하는 안전한 클라이언트들을 구성합니다.
 */
@Slf4j
@Configuration
public class HealthCheckFeignConfig {

    @Value("${dailyfeed.services.timeline.feign.url}")
    private String timelineServiceUrl;

    @Value("${dailyfeed.services.content.feign.url}")
    private String contentServiceUrl;

    @Value("${dailyfeed.services.activity.feign.url}")
    private String activityServiceUrl;

    @Value("${dailyfeed.services.image.feign.url}")
    private String imageServiceUrl;

    /**
     * Health Check 전용 String Decoder
     * text/plain 응답을 String으로 변환
     */
    private static class StringDecoder implements Decoder {
        @Override
        public Object decode(Response response, Type type) throws IOException {
            if (response.body() == null) {
                return null;
            }
            return new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    /**
     * Timeline 서비스 Health Check Client
     * timelineServiceHealth Circuit Breaker 사용
     */
    @Bean
    public TimelineHealthCheckClient timelineHealthCheckClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions,
            CircuitBreakerRegistry circuitBreakerRegistry
    ) {
        FeignDecorators feignDecorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker("timelineServiceHealth"))
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .decoder(new StringDecoder())  // text/plain 응답을 위한 String Decoder
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(TimelineHealthCheckClient.class))
                .logLevel(Logger.Level.BASIC)  // Health Check는 BASIC 레벨로
                .options(requestOptions)
                .target(TimelineHealthCheckClient.class, timelineServiceUrl);
    }

    /**
     * Content 서비스 Health Check Client
     * contentServiceHealth Circuit Breaker 사용
     */
    @Bean
    public ContentHealthCheckClient contentHealthCheckClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions,
            CircuitBreakerRegistry circuitBreakerRegistry
    ) {
        FeignDecorators feignDecorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker("contentServiceHealth"))
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .decoder(new StringDecoder())  // text/plain 응답을 위한 String Decoder
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(ContentHealthCheckClient.class))
                .logLevel(Logger.Level.BASIC)
                .options(requestOptions)
                .target(ContentHealthCheckClient.class, contentServiceUrl);
    }

    /**
     * Activity 서비스 Health Check Client
     * activityServiceHealth Circuit Breaker 사용
     */
    @Bean
    public ActivityHealthCheckClient activityHealthCheckClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions,
            CircuitBreakerRegistry circuitBreakerRegistry
    ) {
        FeignDecorators feignDecorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker("activityServiceHealth"))
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .decoder(new StringDecoder())  // text/plain 응답을 위한 String Decoder
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(ActivityHealthCheckClient.class))
                .logLevel(Logger.Level.BASIC)
                .options(requestOptions)
                .target(ActivityHealthCheckClient.class, activityServiceUrl);
    }

    /**
     * Image 서비스 Health Check Client
     * imageServiceHealth Circuit Breaker 사용
     */
    @Bean
    public ImageHealthCheckClient imageHealthCheckClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions,
            CircuitBreakerRegistry circuitBreakerRegistry
    ) {
        FeignDecorators feignDecorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker("imageServiceHealth"))
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .decoder(new StringDecoder())  // text/plain 응답을 위한 String Decoder
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(ImageHealthCheckClient.class))
                .logLevel(Logger.Level.BASIC)
                .options(requestOptions)
                .target(ImageHealthCheckClient.class, imageServiceUrl);
    }
}