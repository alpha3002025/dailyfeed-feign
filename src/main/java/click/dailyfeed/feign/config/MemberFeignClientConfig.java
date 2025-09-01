package click.dailyfeed.feign.config;

import click.dailyfeed.feign.domain.member.MemberFeignClient;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MemberFeignClientConfig {
    @Value("${app.services.member.url}")
    private String memberServiceUrl;

    @Value("${app.services.member.timeout.connect}")
    private int connectTimeout;

    @Value("${app.services.member.timeout.read}")
    private int readTimeout;

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

    /**
     * Feign Logger Level 설정
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Request 옵션 설정 (연결 타임아웃, 읽기 타임아웃)
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                connectTimeout, TimeUnit.SECONDS,  // 연결 타임아웃
                readTimeout, TimeUnit.SECONDS,  // 읽기 타임아웃
                true  // 리다이렉트 따라가기
        );
    }

    /**
     * 커스텀 에러 디코더
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }


    /**
     * Retryer 설정 (Resilience4j Retry와 별도)
     */
    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY; // Resilience4j Retry를 사용하므로 Feign 자체 재시도는 비활성화
    }
}
