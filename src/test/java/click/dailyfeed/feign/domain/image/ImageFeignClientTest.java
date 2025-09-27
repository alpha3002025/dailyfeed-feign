package click.dailyfeed.feign.domain.image;

import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import click.dailyfeed.feign.config.feign.FeignCommonConfig;
import click.dailyfeed.feign.config.feign.FeignObjectMapperConfig;
import click.dailyfeed.feign.config.feign.ImageFeignClientConfig;
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
        "dailyfeed.services.image.feign.url=http://localhost:8085",
        "dailyfeed.services.image.feign.timeout.connect=5",
        "dailyfeed.services.image.feign.timeout.read=10"
})
@SpringBootTest(classes = {ImageFeignClientConfig.class, FeignCommonConfig.class, FeignObjectMapperConfig.class})
@DisplayName("ImageFeignClient API 통합 테스트")
public class ImageFeignClientTest {

    @Autowired
    private ImageFeignClient imageFeignClient;

    private final String TEST_TOKEN = "eyJraWQiOiI0NTRlYzMwZi1jZTkzLTRkYjAtODFmMi00MzkwMzc1NGU1ZTQiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiZjZlZDY4Mi0yZGZlLTRlMzItYjE0MC0zNWU3Yzc3NjhiYmUiLCJzdWIiOiJjYXNlM19EQGdtYWlsLmNvbSIsImV4cCI6MTc1OTA0NTcxMiwiaWQiOjQsImVtYWlsIjoiY2FzZTNfREBnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYSQxMCQ2ZkIyTkEzQnY0Sjh1dGNNQ2lhQUplOW1QV1NwdzlvaS5kb2JrWko3N1FjT1Bqc2U1Tmx4LiJ9.RifenyTkQcYo31mSnMW0l157ud8WFO_eMe4tY0Dxfcs";


    @Test
    @DisplayName("(feign client) image delete 요청 테스트")
    void delete_image_test() throws IOException {
        MemberProfileDto.ImageDeleteBulkRequest imageDeleteBulkRequest = MemberProfileDto.ImageDeleteBulkRequest.builder()
                .imageUrls(
                        List.of("http://localhost:8085/api/images/view/2d16294f-4394-46cc-bcda-20c2fe93d612")
                )
                .build();

        System.out.println("=== Request Query Object ===");
        System.out.println("Query: " + imageDeleteBulkRequest);
        System.out.println("IDs: " + imageDeleteBulkRequest.getImageUrls());

        Response response = imageFeignClient.deleteImagesBulkRequest(imageDeleteBulkRequest, TEST_TOKEN);

        System.out.println("=== Response Details ===");
        System.out.println("Status: " + response.status());
        System.out.println("Headers: " + response.headers());

        String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("DELETE /api/images/view/command/in Response: " + body);
    }
}
