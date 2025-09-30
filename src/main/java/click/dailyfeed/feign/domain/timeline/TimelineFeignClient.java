package click.dailyfeed.feign.domain.timeline;

import click.dailyfeed.code.domain.content.post.dto.PostDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface TimelineFeignClient {
    ///  bulk 조회 (특정 id list 에 해당하는 글의 목록 조회)
    @RequestLine("POST /api/timeline/posts/query/list")
    @Headers({"Authorization: Bearer {token}", "Content-Type: application/json"})
    Response getPostList(PostDto.PostsBulkRequest request,
                         @Param("token") String token);
}
