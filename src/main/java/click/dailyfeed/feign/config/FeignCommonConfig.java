package click.dailyfeed.feign.config;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignCommonConfig {
    @Value("${dailyfeed.services.content.feign.timeout.connect}")
    private int connectTimeout;

    @Value("${dailyfeed.services.content.feign.timeout.read}")
    private int readTimeout;

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
     * Retryer 설정 (Resilience 4j Retry와 별도)
     */
    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY; // Resilience4j Retry를 사용하므로 Feign 자체 재시도는 비활성화
    }
}
