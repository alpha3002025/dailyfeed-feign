package click.dailyfeed.feign.domain.post;

import click.dailyfeed.code.domain.content.post.dto.PostDto;
import click.dailyfeed.code.domain.member.member.code.MemberHeaderCode;
import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import click.dailyfeed.code.domain.member.member.exception.MemberApiConnectionErrorException;
import click.dailyfeed.code.domain.member.member.exception.MemberNotFoundException;
import click.dailyfeed.code.global.web.response.DailyfeedServerResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// TODO : feign response 처리 로직 공통화 작업 필요 🫡
@Slf4j
@RequiredArgsConstructor
@Service
public class PostFeignHelper {
    private final PostFeignClient postFeignClient;

    @Qualifier("feignObjectMapper")
    private final ObjectMapper feignObjectMapper;


    public List<PostDto.Post> getPostList(PostDto.PostsBulkRequest request ,String token, HttpServletResponse httpResponse) {
        Response feignResponse = postFeignClient.getPostList(request, token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<PostDto.Post>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<List<PostDto.Post>>>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);

            return apiResponse.getData();
        }
        catch (Exception e){
            throw new MemberApiConnectionErrorException();
        }
        finally {
            if( feignResponse.body() != null ){
                try {
                    feignResponse.body().close();
                }
                catch (Exception e){
                    log.error("feign response close error", e);
                }
            }
        }
    }

    public Map<Long, PostDto.Post> getPostMap(PostDto.PostsBulkRequest request ,String token, HttpServletResponse httpResponse) {
        List<PostDto.Post> postList = getPostList(request, token, httpResponse);
        return postList.stream().collect(Collectors.toMap(PostDto.Post::getId, post -> post));
    }

    public void propagateTokenRefreshHeader(Response feignResponse, HttpServletResponse httpResponse) {
        final String headerKey = MemberHeaderCode.X_TOKEN_REFRESH_NEEDED.getHeaderKey();
        Collection<String> headers = feignResponse.headers().get(headerKey);

        if(headers != null && !headers.isEmpty()){
            String headerValue = headers.iterator().next();
            if(headerValue != null && !headerValue.isEmpty()){
                if ("true".equalsIgnoreCase(headerValue)){
                    httpResponse.setHeader(headerKey, "true");
                }
            }
        }
    }
}
