package click.dailyfeed.feign.config;

import click.dailyfeed.feign.domain.post.PostFeignClient;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostFeignClientConfig {
    @Value("${dailyfeed.services.content.feign.url}")
    private String memberServiceUrl;

    @Bean
    public PostFeignClient postFeignClient() {
        FeignDecorators feignDecorators = FeignDecorators.builder()
//                .withCircuitBreaker()
//                .withRateLimiter()
                .build();

        return Resilience4jFeign
                .builder(feignDecorators)
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .target(PostFeignClient.class, memberServiceUrl);
    }
}
