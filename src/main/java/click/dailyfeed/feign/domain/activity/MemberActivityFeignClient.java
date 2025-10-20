package click.dailyfeed.feign.domain.activity;

import click.dailyfeed.code.domain.activity.dto.MemberActivityDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface MemberActivityFeignClient {
    @RequestLine("POST /api/member-activities/posts")
    @Headers({"Authorization: Bearer {token}", "Content-Type: application/json"})
    Response createPostsMemberActivity(
            MemberActivityDto.PostActivityRequest postActivityRequest,
            @Param("token") String token
    );

    @RequestLine("POST /api/member-activities/comments")
    @Headers({"Authorization: Bearer {token}", "Content-Type: application/json"})
    Response createCommentsMemberActivity(
            MemberActivityDto.CommentActivityRequest commentActivityRequest,
            @Param("token") String token
    );

    @RequestLine("POST /api/member-activities/posts/likes")
    @Headers({"Authorization: Bearer {token}", "Content-Type: application/json"})
    Response createPostLikeActivity(
            MemberActivityDto.PostLikeActivityRequest postLikeActivityRequest,
            @Param("token") String token
    );

    @RequestLine("POST /api/member-activities/comments/likes")
    @Headers({"Authorization: Bearer {token}", "Content-Type: application/json"})
    Response createCommentLikeActivity(
            MemberActivityDto.CommentLikeActivityRequest commentLikeActivityRequest,
            @Param("token") String token
    );
}
