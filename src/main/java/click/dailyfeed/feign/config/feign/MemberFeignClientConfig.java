package click.dailyfeed.feign.config.feign;

import click.dailyfeed.feign.domain.member.MemberFeignClient;
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
public class MemberFeignClientConfig {
    @Value("${dailyfeed.services.member.feign.url}")
    private String memberServiceUrl;

    @Bean
    public MemberFeignClient memberFeignClient(
            ErrorDecoder customErrorDecoder,
            @Qualifier("feignObjectMapper") ObjectMapper feignObjectMapper,
            Logger.Level feignLoggerLevel,
            Request.Options requestOptions
    ) {
        FeignDecorators feignDecorators = FeignDecorators.builder()
//                .withCircuitBreaker()
//                .withRateLimiter()
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .encoder(new JacksonEncoder(feignObjectMapper))
                .decoder(new JacksonDecoder(feignObjectMapper))
                .errorDecoder(customErrorDecoder)
                .logger(new Slf4jLogger(MemberFeignClient.class))
                .logLevel(feignLoggerLevel)
                .options(requestOptions)
                .requestInterceptor(template -> {
                    if ("POST".equals(template.method()) && !template.headers().containsKey("Content-Type")) {
                        template.header("Content-Type", "application/json");
                    }
                })
                .target(MemberFeignClient.class, memberServiceUrl);
    }
}
