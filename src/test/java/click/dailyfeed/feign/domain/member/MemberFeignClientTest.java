package click.dailyfeed.feign.domain.member;

import click.dailyfeed.feign.config.feign.FeignCommonConfig;
import click.dailyfeed.feign.config.feign.MemberFeignClientConfig;
import feign.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@ActiveProfiles("test")
@TestPropertySource(properties = {
        "dailyfeed.services.member.feign.url=http://localhost:8084",
        "dailyfeed.services.content.feign.timeout.connect=5",
        "dailyfeed.services.content.feign.timeout.read=10"
})
@SpringBootTest(classes = {MemberFeignClientConfig.class, FeignCommonConfig.class})
@DisplayName("MemberFeignClient API 통합 테스트")
public class MemberFeignClientTest {

    @Autowired
    private MemberFeignClient memberFeignClient;

    private final String TEST_TOKEN = "eyJraWQiOiJlMmU4OTVhOC01ZTI5LTQ3NGYtYmMyMS0xZWU3YjZkNTJlYWIiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkNmRmNDI1My0yM2RhLTQxOTEtYjRlOS01YTU0MjJmMWFlMmUiLCJzdWIiOiJjYXNlM19DQGdtYWlsLmNvbSIsImV4cCI6MTc1ODcwNTAxNCwiaWQiOjMsImVtYWlsIjoiY2FzZTNfQ0BnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYSQxMCRzNDdvQ3hITkY0VTNBQTZYS0NySy51aVVvc2ZJVDBPOWdJOU1SSHA1YUc4RWg5NnNwN2hsZSJ9.YK1oQQ0rmEwNWhWSvj-I_etkDn06IqyjIyR6zpdZHHo";


    @Test
    @DisplayName("(feign client) 내 정보 조회 API 테스트")
    void getMember_토큰기반_내정보조회() throws IOException {
        // given & when
        Response response = memberFeignClient.getMember(TEST_TOKEN);

        // then
//        assertThat(response.status()).isEqualTo(200);
//        assertThat(response.headers()).containsKey("content-type");

        String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
//        assertThat(body).isNotEmpty();

        System.out.println("GET /api/members Response: " + body);
    }

    @Test
    @DisplayName("(feign client) 내 정보 조회 API 테스트")
    void getMember_member_id_에_대해_실제_summary_데이터조회() throws IOException {
        // given & when
        Response response = memberFeignClient.getSummaryById(TEST_TOKEN, 2L);

        // then
//        assertThat(response.status()).isEqualTo(200);
//        assertThat(response.headers()).containsKey("content-type");

        String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
//        assertThat(body).isNotEmpty();

        System.out.println("GET /api/members Response: " + body);
    }

    @Test
    @DisplayName("(feign helper) 내 정보 조회 API 테스트 ")
    void getMember_feignHelper() throws IOException {
//        memberFeignHelper.getMemberById()
    }
}
