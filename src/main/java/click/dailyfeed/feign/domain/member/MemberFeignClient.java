package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import click.dailyfeed.feign.config.feign.JacksonExpander;
import feign.*;
import org.springframework.web.bind.annotation.RequestParam;

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
    Response getMyFollowersFollowings(
            @Param("token") String token,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    );

    @RequestLine("GET /api/members/followings/more")
    @Headers("Authorization: {token}")
    Response getMyFollowingsMore(
            @Param("token") String token,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    );

    @RequestLine("GET /api/members/followers/more")
    @Headers("Authorization: {token}")
    Response getMyFollowersMore(
            @Param("token") String token,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    );

    ///  특정 용도
    @RequestLine("POST /api/members/query/in")
    @Headers("Authorization: {token}")
    @Body("{query}")
    Response getMemberList(@Param(value = "query", expander = JacksonExpander.class) MemberDto.MembersIdsQuery query);

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

    // 특정 멤버의 팔로워,팔로잉
    @RequestLine("GET /api/members/{memberId}/followers-followings")
    @Headers("Authorization: {token}")
    Response getFollowersFollowingMembersById(
            @Param("token") String token,
            @Param("memberId") Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    );

    @RequestLine("GET /api/members/{memberId}/followers/more")
    @Headers("Authorization: {token}")
    Response getFollowersMoreById(
            @Param("token") String token,
            @Param("memberId") Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    );

    @RequestLine("GET /api/members/{memberId}/followings/more")
    @Headers("Authorization: {token}")
    Response getFollowingsMoreById(
            @Param("token") String token,
            @Param("memberId") Long memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", required = false) String sort
    );
}
