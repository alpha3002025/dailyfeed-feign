package click.dailyfeed.feign.domain.post;

import click.dailyfeed.code.domain.content.post.dto.PostDto;
import feign.Headers;
import feign.RequestLine;
import feign.Response;

public interface PostFeignClient {

    @RequestLine("GET /api/posts/list")
    @Headers("Authorization: {token}")
    Response getPostList(PostDto.PostsBulkRequest request, String token);

}
