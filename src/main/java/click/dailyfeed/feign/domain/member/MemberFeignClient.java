package click.dailyfeed.feign.domain.member;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface MemberFeignClient {
    @RequestLine("GET /api/members")
    @Headers("Authorization: {token}")
    Response getMember(@Param("token") String token);

//    @RequestLine("POST /api/members/list")
//    Response getMemberList(MemberDto.MembersBulkRequest request);

    @RequestLine("GET /api/members/{memberId}")
    Response getMemberById(@Param("memberId") Long memberId);

//    @RequestLine("GET /api/follow/recent-activities")
//    @Headers("Authorization: {token}")
//    Response getRecentActivitiesFromFollowing(@Param("token") String token);
}
