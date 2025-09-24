package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import click.dailyfeed.feign.config.feign.JacksonExpander;
import feign.*;

public interface MemberFeignClient {
    ///  my
    @RequestLine("GET /api/members")
    @Headers("Authorization: {token}")
    Response getMember(@Param("token") String token);

    @RequestLine("GET /api/members/profile")
    @Headers("Authorization: {token}")
    Response getMyProfile(@Param("token") String token);

    @RequestLine("GET /api/members/summary")
    @Headers("Authorization: {token}")
    Response getMyProfileSummary(@Param("token") String token);

    @RequestLine("GET /api/members/followers-followings?page={page}&size={size}&sort={sort}")
    @Headers("Authorization: {token}")
    Response getMyFollowersFollowings(
            @Param("token") String token,
            @Param("page") int page,
            @Param("size") int size,
            @Param("sort") String sort
    );

    @RequestLine("GET /api/members/followings/more?page={page}&size={size}&sort={sort}")
    @Headers("Authorization: {token}")
    Response getMyFollowingsMore(
            @Param("token") String token,
            @Param("page") int page,
            @Param("size") int size,
            @Param("sort") String sort
    );

    @RequestLine("GET /api/members/followers/more?page={page}&size={size}&sort={sort}")
    @Headers("Authorization: {token}")
    Response getMyFollowersMore(
            @Param("token") String token,
            @Param("page") int page,
            @Param("size") int size,
            @Param("sort") String sort
    );

    ///  특정 용도
    @RequestLine("POST /api/members/query/in")
    @Headers("Authorization: {token}")
    @Body("{query}")
    Response getMemberList(
            @Param(value = "query", expander = JacksonExpander.class) MemberDto.MembersIdsQuery query,
            @Param("token") String token
    );

    @RequestLine("GET /api/members/query/followings")
    @Headers("Authorization: {token}")
    Response getMyFollowingMembers(@Param("token") String token);

    ///  {memberId}
    @RequestLine("GET /api/members/{memberId}")
    @Headers("Authorization: {token}")
    Response getMemberById(@Param("memberId") Long memberId,  @Param("token") String token);

    @RequestLine("GET /api/members/{memberId}/profile")
    @Headers("Authorization: {token}")
    Response getProfileById(@Param("token") String token, @Param("memberId") Long memberId);

    @RequestLine("GET /api/members/{memberId}/summary")
    @Headers("Authorization: {token}")
    Response getSummaryById(@Param("token") String token, @Param("memberId") Long memberId);

    // 특정 멤버의 팔로워,팔로잉
    @RequestLine("GET /api/members/{memberId}/followers-followings?page={page}&size={size}&sort={sort}")
    @Headers("Authorization: {token}")
    Response getFollowersFollowingMembersById(
            @Param("token") String token,
            @Param("memberId") Long memberId,
            @Param("page") int page,
            @Param("size") int size,
            @Param("sort") String sort
    );

    @RequestLine("GET /api/members/{memberId}/followers/more?page={page}&size={size}&sort={sort}")
    @Headers("Authorization: {token}")
    Response getFollowersMoreById(
            @Param("token") String token,
            @Param("memberId") Long memberId,
            @Param("page") int page,
            @Param("size") int size,
            @Param("sort") String sort
    );

    @RequestLine("GET /api/members/{memberId}/followings/more?page={page}&size={size}&sort={sort}")
    @Headers("Authorization: {token}")
    Response getFollowingsMoreById(
            @Param("token") String token,
            @Param("memberId") Long memberId,
            @Param("page") int page,
            @Param("size") int size,
            @Param("sort") String sort
    );
}
