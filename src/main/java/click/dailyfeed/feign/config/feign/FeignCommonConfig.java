package click.dailyfeed.feign.config.feign;

import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignCommonConfig {

    @Value("${feign.client.config.default.connect-timeout:5000}")
    private int connectTimeoutMillis;

    @Value("${feign.client.config.default.read-timeout:10000}")
    private int readTimeoutMillis;

    /**
     * Feign Logger Level 설정
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Request 옵션 설정 (연결 타임아웃, 읽기 타임아웃)
     * YAML 설정: feign.client.config.default.connect-timeout, read-timeout
     * 환경별로 다른 타임아웃 값을 설정할 수 있습니다.
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                connectTimeoutMillis, TimeUnit.MILLISECONDS,  // 연결 타임아웃
                readTimeoutMillis, TimeUnit.MILLISECONDS,  // 읽기 타임아웃
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

    /**
     * CircuitBreaker Registry
     * application.yaml에서 설정을 로드하여 CircuitBreaker 인스턴스를 관리합니다.
     *
     * YAML 설정 위치: resilience4j.circuitbreaker
     * - configs: 재사용 가능한 설정 프리셋 정의 (default, critical, bulk)
     * - instances: 각 서비스별 CircuitBreaker 인스턴스 및 base-config 매핑
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        // Spring Boot Auto-configuration이 YAML 설정을 자동으로 로드
        // 빈을 명시적으로 선언하여 의존성 주입 가능하도록 함
        return CircuitBreakerRegistry.ofDefaults();
    }

    /**
     * Retry Registry
     * application.yaml에서 설정을 로드하여 Retry 인스턴스를 관리합니다.
     *
     * YAML 설정 위치: resilience4j.retry
     * - configs: 재사용 가능한 설정 프리셋 정의 (default, fast, conservative)
     * - instances: 각 서비스별 Retry 인스턴스 및 base-config 매핑
     */
    @Bean
    public RetryRegistry retryRegistry() {
        // Spring Boot Auto-configuration이 YAML 설정을 자동으로 로드
        return RetryRegistry.ofDefaults();
    }

    /**
     * RateLimiter Registry
     * application.yaml에서 설정을 로드하여 RateLimiter 인스턴스를 관리합니다.
     *
     * YAML 설정 위치: resilience4j.ratelimiter
     * - configs: 재사용 가능한 설정 프리셋 정의 (default, critical, conservative)
     * - instances: 각 서비스별 RateLimiter 인스턴스 및 base-config 매핑
     */
    @Bean
    public RateLimiterRegistry rateLimiterRegistry() {
        // Spring Boot Auto-configuration이 YAML 설정을 자동으로 로드
        return RateLimiterRegistry.ofDefaults();
    }
}
