package click.dailyfeed.feign.config.health;

import click.dailyfeed.feign.domain.health.*;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 서비스 Health Check 스케줄러
 *
 * 주기적으로 각 마이크로서비스의 /healthcheck/live 엔드포인트를 호출하여
 * 서비스 상태를 모니터링합니다.
 *
 * - 비즈니스 API와 분리된 Health Check 전용 Circuit Breaker 사용
 * - 안전한 엔드포인트만 호출 (DELETE, UPDATE 등 위험한 API 호출 안 함)
 * - Circuit Breaker 상태 변화를 로그로 기록
 * - 자기 자신에 대한 Health Check는 수행하지 않음
 *
 * 설정:
 * - dailyfeed.scheduling.health-check.enabled=true 로 활성화
 * - 기본값: true
 */
@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(
    name = "dailyfeed.scheduling.health-check.enabled",
    havingValue = "true",
    matchIfMissing = true  // 설정이 없으면 기본적으로 활성화
)
public class ServiceHealthCheckScheduler {

    private final TimelineHealthCheckClient timelineHealthCheckClient;
    private final ContentHealthCheckClient contentHealthCheckClient;
    private final ActivityHealthCheckClient activityHealthCheckClient;
    private final ImageHealthCheckClient imageHealthCheckClient;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Timeline 서비스 Health Check
     * 5초마다 실행
     * 자기 자신(dailyfeed-timeline)인 경우 실행하지 않음
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void checkTimelineServiceHealth() {
        if (isSelf("dailyfeed-timeline")) {
            return;
        }
        checkServiceHealth(
            "timelineService",
            "timelineServiceHealth",
            timelineHealthCheckClient::checkHealth
        );
    }

    /**
     * Content 서비스 Health Check
     * 5초마다 실행
     * 자기 자신(dailyfeed-content)인 경우 실행하지 않음
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void checkContentServiceHealth() {
        if (isSelf("dailyfeed-content")) {
            return;
        }
        checkServiceHealth(
            "contentService",
            "contentServiceHealth",
            contentHealthCheckClient::checkHealth
        );
    }

    /**
     * Activity 서비스 Health Check
     * 5초마다 실행
     * 자기 자신(dailyfeed-activity)인 경우 실행하지 않음
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void checkActivityServiceHealth() {
        if (isSelf("dailyfeed-activity")) {
            return;
        }
        checkServiceHealth(
            "activityService",
            "activityServiceHealth",
            activityHealthCheckClient::checkHealth
        );
    }

    /**
     * Image 서비스 Health Check
     * 5초마다 실행
     * 자기 자신(dailyfeed-image)인 경우 실행하지 않음
     */
    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public void checkImageServiceHealth() {
        if (isSelf("dailyfeed-image")) {
            return;
        }
        checkServiceHealth(
            "imageService",
            "imageServiceHealth",
            imageHealthCheckClient::checkHealth
        );
    }

    /**
     * 현재 애플리케이션이 지정한 서비스인지 확인
     *
     * @param targetServiceName 확인할 서비스 이름
     * @return 자기 자신이면 true, 아니면 false
     */
    private boolean isSelf(String targetServiceName) {
        return targetServiceName.equals(applicationName);
    }

    /**
     * 서비스 Health Check 공통 로직
     *
     * @param serviceName 서비스 이름 (로깅용)
     * @param circuitBreakerName Health Check Circuit Breaker 이름
     * @param healthCheckSupplier Health Check 실행 함수
     */
    private void checkServiceHealth(
        String serviceName,
        String circuitBreakerName,
        HealthCheckSupplier healthCheckSupplier
    ) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        CircuitBreaker.State state = circuitBreaker.getState();

        try {
            // Health Check Circuit Breaker로 감싸서 호출
            String health = circuitBreaker.executeSupplier(() -> {
                try {
                    return healthCheckSupplier.check();
                } catch (Exception e) {
                    // 체크 예외를 런타임 예외로 변환
                    throw new RuntimeException("Health check failed for " + serviceName, e);
                }
            });

            // 성공 시 DEBUG 레벨로만 기록 (로그 과다 방지)
            if ("OK".equals(health)) {
                log.debug("[Health Check] {} is OK (CB State: {})", serviceName, state);
            } else {
                log.warn("[Health Check] {} returned non-OK status: {} (CB State: {})",
                    serviceName, health, state);
            }

        } catch (Exception e) {
            // 실패 시 WARN 레벨로 기록
            CircuitBreaker.State newState = circuitBreaker.getState();

            if (state != newState) {
                // Circuit Breaker 상태 변화가 있으면 ERROR 레벨로 기록
                log.error("[Health Check] {} health check FAILED - Circuit Breaker state changed: {} -> {}",
                    serviceName, state, newState, e);
            } else {
                // 상태 변화가 없으면 WARN 레벨
                log.warn("[Health Check] {} health check FAILED (CB State: {}): {}",
                    serviceName, newState, e.getMessage());
            }
        }
    }

    /**
     * Health Check 실행 함수형 인터페이스
     * 체크 예외를 던질 수 있도록 정의
     */
    @FunctionalInterface
    private interface HealthCheckSupplier {
        String check() throws Exception;
    }
}