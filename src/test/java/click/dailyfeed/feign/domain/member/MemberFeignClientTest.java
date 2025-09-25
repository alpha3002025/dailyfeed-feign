package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import click.dailyfeed.feign.config.feign.FeignCommonConfig;
import click.dailyfeed.feign.config.feign.FeignObjectMapperConfig;
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
import java.util.List;

@ActiveProfiles("test")
@TestPropertySource(properties = {
        "dailyfeed.services.member.feign.url=http://localhost:8084",
        "dailyfeed.services.content.feign.timeout.connect=5",
        "dailyfeed.services.content.feign.timeout.read=10"
})
@SpringBootTest(classes = {MemberFeignClientConfig.class, FeignCommonConfig.class, FeignObjectMapperConfig.class})
@DisplayName("MemberFeignClient API 통합 테스트")
public class MemberFeignClientTest {

    @Autowired
    private MemberFeignClient memberFeignClient;

    private final String TEST_TOKEN = "eyJraWQiOiI5MzdmOGVhOC0yYWU4LTRhNDctOWMzZS01ZjdkMjU4ZTZkMmEiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhYzkwMTk1Ni02MDI4LTRkMGUtYWQxZC03NWY1MTg3NzFkMDMiLCJzdWIiOiJjYXNlM19BQGdtYWlsLmNvbSIsImV4cCI6MTc1ODg3NDk0NSwiaWQiOjEsImVtYWlsIjoiY2FzZTNfQUBnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYSQxMCREdmRiS0lJc1NWMS44S04zVWNOQTVPZXhxSllyRWZKYUJLRGt1REI4RGg0cUJ6SVJkSmM1aSJ9.NnoD_9iBEMJcfdwFJTwjKL6hY-mzifDFuHAnmCJHyEk";


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
    @DisplayName("(feign client) id 리스트에 대한 프로필 조회")
    void getMember_id_list_by_query() throws IOException {
        MemberDto.MembersIdsQuery query = MemberDto.MembersIdsQuery.builder().ids(List.of(3L)).build();

        System.out.println("=== Request Query Object ===");
        System.out.println("Query: " + query);
        System.out.println("IDs: " + query.getIds());

        Response response = memberFeignClient.getMemberList(query, TEST_TOKEN);

        System.out.println("=== Response Details ===");
        System.out.println("Status: " + response.status());
        System.out.println("Headers: " + response.headers());

        String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("POST /api/members/query/in Response: " + body);
    }
}
