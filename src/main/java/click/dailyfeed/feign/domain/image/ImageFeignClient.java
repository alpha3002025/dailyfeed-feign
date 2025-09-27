package click.dailyfeed.feign.domain.image;

import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface ImageFeignClient {
    @RequestLine("POST /api/images/view/command/delete/in")
    @Headers("Authorization: Bearer {token}")
    Response deleteImagesBulkRequest(
            MemberProfileDto.ImageDeleteBulkRequest request,
            @Param("token") String token
    );
}
