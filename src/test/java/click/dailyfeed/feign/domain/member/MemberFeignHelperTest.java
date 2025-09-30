package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import click.dailyfeed.feign.config.feign.FeignCommonConfig;
import click.dailyfeed.feign.config.feign.FeignObjectMapperConfig;
import click.dailyfeed.feign.config.feign.client.MemberFeignClientConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


@ActiveProfiles("test")
@TestPropertySource(properties = {
        "dailyfeed.services.member.feign.url=http://localhost:8084",
        "dailyfeed.services.content.feign.timeout.connect=5",
        "dailyfeed.services.content.feign.timeout.read=10"
})
@SpringBootTest(classes = {
        FeignCommonConfig.class,
        FeignObjectMapperConfig.class,
        MemberFeignClientConfig.class,
        MemberFeignClient.class,
        MemberFeignHelper.class,
})
@DisplayName("MemberFeignClient API 통합 테스트")
public class MemberFeignHelperTest {
    @Autowired
    private MemberFeignHelper memberFeignHelper;

    private final String TEST_TOKEN = "eyJraWQiOiJlMmU4OTVhOC01ZTI5LTQ3NGYtYmMyMS0xZWU3YjZkNTJlYWIiLCJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkNmRmNDI1My0yM2RhLTQxOTEtYjRlOS01YTU0MjJmMWFlMmUiLCJzdWIiOiJjYXNlM19DQGdtYWlsLmNvbSIsImV4cCI6MTc1ODcwNTAxNCwiaWQiOjMsImVtYWlsIjoiY2FzZTNfQ0BnbWFpbC5jb20iLCJwYXNzd29yZCI6IiQyYSQxMCRzNDdvQ3hITkY0VTNBQTZYS0NySy51aVVvc2ZJVDBPOWdJOU1SSHA1YUc4RWg5NnNwN2hsZSJ9.YK1oQQ0rmEwNWhWSvj-I_etkDn06IqyjIyR6zpdZHHo";

    @Test
    void getMemberSummaryById_사용자정보조회_by_memberId(){
        MemberProfileDto.Summary memberSummaryById = memberFeignHelper.getMemberSummaryById(2L, TEST_TOKEN, null);
        System.out.println(memberSummaryById.toString());
    }

    @Test
    void getMemberMap_사용자정보조회_by_memberId(){

    }
}
