package click.dailyfeed.feign.config;

import click.dailyfeed.feign.domain.member.MemberFeignClient;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberFeignClientConfig {
    @Value("${dailyfeed.services.member.feign.url}")
    private String memberServiceUrl;

    @Bean
    public MemberFeignClient memberFeignClient() {
        FeignDecorators feignDecorators = FeignDecorators.builder()
//                .withCircuitBreaker()
//                .withRateLimiter()
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(MemberFeignClient.class, memberServiceUrl);
    }
}
