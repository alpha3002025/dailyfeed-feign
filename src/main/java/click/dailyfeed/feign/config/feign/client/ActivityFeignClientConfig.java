package click.dailyfeed.feign.config.feign.client;

import click.dailyfeed.feign.domain.activity.ActivityFeignClient;
import click.dailyfeed.feign.domain.timeline.TimelineFeignClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Logger;
import feign.Request;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivityFeignClientConfig {
    @Value("${dailyfeed.services.timeline.feign.url}")
    private String timelineUrl;

    @Bean
    public ActivityFeignClient activityFeignClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions,
            CircuitBreakerRegistry circuitBreakerRegistry,
            RetryRegistry retryRegistry,
            RateLimiterRegistry rateLimiterRegistry
    ) {
        // YAML 설정에서 정의된 인스턴스를 Registry에서 가져옴
        // feign-config.yaml: resilience4j.circuitbreaker.instances.timelineService
        // feign-config.yaml: resilience4j.retry.instances.timelineService
        // feign-config.yaml: resilience4j.ratelimiter.instances.timelineService
        FeignDecorators feignDecorators = FeignDecorators.builder()
                .withRetry(retryRegistry.retry("activityService"))
                .withCircuitBreaker(circuitBreakerRegistry.circuitBreaker("activityService"))
                .withRateLimiter(rateLimiterRegistry.rateLimiter("activityService"))
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .encoder(new JacksonEncoder(feignObjectMapper))
                .decoder(new JacksonDecoder(feignObjectMapper))
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(TimelineFeignClient.class))
                .logLevel(feignLoggerLevel)
                .options(requestOptions)
                .requestInterceptor(template -> {
                    // Content-Type 처리
                    if ("POST".equals(template.method()) && !template.headers().containsKey("Content-Type")) {
                        template.header("Content-Type", "application/json");
                    }

                    // Authorization 헤더에 Bearer 접두사 추가
                    if (template.headers().containsKey("Authorization")) {
                        template.headers().get("Authorization").forEach(value -> {
                            if (!value.startsWith("Bearer ")) {
                                template.removeHeader("Authorization");
                                template.header("Authorization", "Bearer " + value);
                            }
                        });
                    }
                })
                .target(ActivityFeignClient.class, timelineUrl);
    }
}
