package click.dailyfeed.feign.domain.health;

import feign.RequestLine;

/**
 * Activity 서비스 Health Check 전용 Feign Client
 * 안전한 /healthcheck/live 엔드포인트만 호출합니다.
 */
public interface ActivityHealthCheckClient {

    /**
     * Activity 서비스의 Health Check 엔드포인트 호출
     * @return Health 상태 정보 (응답: "OK")
     */
    @RequestLine("GET /healthcheck/live")
    String checkHealth();
}