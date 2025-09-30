package click.dailyfeed.feign.domain.post;

import click.dailyfeed.code.domain.content.post.dto.PostDto;
import click.dailyfeed.feign.config.feign.FeignCommonConfig;
import click.dailyfeed.feign.config.feign.FeignObjectMapperConfig;
import click.dailyfeed.feign.config.feign.client.PostFeignClientConfig;
import feign.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@ActiveProfiles("test")
@TestPropertySource(properties = {
        "dailyfeed.services.content.feign.url=http://localhost:8081",
        "dailyfeed.services.content.feign.timeout.connect=5",
        "dailyfeed.services.content.feign.timeout.read=10"
})
@SpringBootTest(classes = {PostFeignClientConfig.class, FeignCommonConfig.class, FeignObjectMapperConfig.class})
@DisplayName("MemberFeignClient API 통합 테스트")
public class PostFeignClientTest {

    @Autowired
    private PostFeignClient postFeignClient;

    private final String TEST_TOKEN = "eyJraWQiOiI5MzdmOGVhOC0yYWU4LTRhNDctOWMzZS01ZjdkMjU4ZTZkMmEiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhYzkwMTk1Ni02MDI4LTRkMGUtYWQxZC03NWY1MTg3NzFkMDMiLCJzdWIiOiJjYXNlM19BQGdtYWlsLmNvbSIsImV4cCI6MTc1ODg3NDk0NSwiaWQiOjEsImVtYWlsIjoiY2FzZTNfQUBnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYSQxMCREdmRiS0lJc1NWMS44S04zVWNOQTVPZXhxSllyRWZKYUJLRGt1REI4RGg0cUJ6SVJkSmM1aSJ9.NnoD_9iBEMJcfdwFJTwjKL6hY-mzifDFuHAnmCJHyEk";


    @Test
    @DisplayName("(feign client) postId 리스트에 대한 글 목록 조회")
    void read_post_list__by_post_ids() throws IOException {
        PostDto.PostsBulkRequest query = PostDto.PostsBulkRequest.builder().ids(Set.of(1L, 2L, 3L, 4L, 5L)).build();

        System.out.println("=== Request Query Object ===");
        System.out.println("Query: " + query);
        System.out.println("IDs: " + query.getIds());

        Response response = postFeignClient.getPostList(query, TEST_TOKEN);

        System.out.println("=== Response Details ===");
        System.out.println("Status: " + response.status());
        System.out.println("Headers: " + response.headers());

        String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("POST /api/members/query/in Response: " + body);
    }
}
