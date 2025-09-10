package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface MemberFeignClient {
    @RequestLine("GET /api/members/")
    @Headers("Authorization: {token}")
    Response getMember(@Param("token") String token);

    @RequestLine("POST /api/members/list")
    @Headers("Authorization: {token}")
    Response getMemberList(MemberDto.MembersBulkRequest request);

    ///  다른 멤버의 정보 조회시
    @RequestLine("GET /api/members/{memberId}")
    Response getMemberById(@Param("memberId") Long memberId);

    @RequestLine("GET /api/members/profile")
    @Headers("Authorization: {token}")
    Response getMyProfile(String token);

    @RequestLine("GET /api/members/profile/{memberId}")
    @Headers("Authorization: {token}")
    Response getMemberProfile(@Param("token") String token, @Param("memberId") Long memberId);

    @RequestLine("GET /api/members/followings")
    @Headers("Authorization: {token}")
    Response getMyFollowings(@Param("token") String token);

    @RequestLine("GET /api/members/{memberId}/followings")
    @Headers("Authorization: {token}")
    Response getMemberFollowingsById(@Param("token") String token);

}
