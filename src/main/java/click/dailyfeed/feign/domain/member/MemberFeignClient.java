package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import click.dailyfeed.feign.config.feign.JacksonExpander;
import feign.*;

public interface MemberFeignClient {
    ///  my
    @RequestLine("GET /api/members/")
    @Headers("Authorization: {token}")
    Response getMember(@Param("token") String token);

    @RequestLine("GET /api/members/profile")
    @Headers("Authorization: {token}")
    Response getMyProfile(String token);

    @RequestLine("GET /api/members/followers-followings")
    @Headers("Authorization: {token}")
    Response getMyFollowersFollowings(@Param("token") String token);

    @RequestLine("GET /api/members/followings")
    @Headers("Authorization: {token}")
    Response getMyFollowingMembers(@Param("token") String token);


    ///  특정 용도
    // TODO : 작명 새로 다시!! 🫡
    @RequestLine("POST /api/members/query/in")
    @Headers("Authorization: {token}")
    @Body("{query}")
    Response getMemberList(@Param(value = "query", expander = JacksonExpander.class) MemberDto.MembersIdsQuery query);


    ///  {memberId}
    //  TODO (삭제 or 통합조회 API 검토) : member 하나만 달랑 들고오는 교과서적인 REST API 는 없다.
    //   member 처럼 다양한 특성을 가진 케이스의 경우 사실상 모든걸 때려박아서 가져오는 API 는 불가능하다고 보임
    @RequestLine("GET /api/members/{memberId}")
    @Headers("Authorization: {token}")
    Response getMemberById(@Param("memberId") Long memberId,  @Param("token") String token);

    @RequestLine("GET /api/members/{memberId}/profile")
    @Headers("Authorization: {token}")
    Response getProfileById(@Param("token") String token, @Param("memberId") Long memberId);

    // 특정 멤버의 팔로워,팔로잉
    // todo (페이징처리가 필요하다) 페이징, token 처리 AOP 적용 🫡
    @RequestLine("GET /api/members/{memberId}/followers-followings")
    @Headers("Authorization: {token}")
    Response getFollowersFollowingMembersById(@Param("token") String token, @Param("memberId") Long memberId);

}
