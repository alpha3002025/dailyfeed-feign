package click.dailyfeed.feign.domain.activity;

import click.dailyfeed.code.domain.activity.dto.MemberActivityDto;
import click.dailyfeed.code.domain.member.member.code.MemberHeaderCode;
import click.dailyfeed.code.global.feign.exception.FeignApiCommunicationFailException;
import click.dailyfeed.code.global.feign.exception.FeignApiSerializationFailException;
import click.dailyfeed.code.global.web.response.DailyfeedServerResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberActivityFeignHelper {
    private final MemberActivityFeignClient memberActivityFeignClient;

    @Qualifier("feignObjectMapper")
    private final ObjectMapper feignObjectMapper;

    public MemberActivityDto.MemberActivity createPostsMemberActivity(MemberActivityDto.PostActivityRequest request, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberActivityFeignClient.createPostsMemberActivity(request, token);
        if (feignResponse.status() != 200 && feignResponse.status() != 201) {
            throw new FeignApiCommunicationFailException();
        }

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberActivityDto.MemberActivity> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);

            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new FeignApiSerializationFailException();
        }
        catch (Exception e){
            throw new FeignApiCommunicationFailException();
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

    public MemberActivityDto.MemberActivity createCommentsMemberActivity(MemberActivityDto.CommentActivityRequest request, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberActivityFeignClient.createCommentsMemberActivity(request, token);
        if (feignResponse.status() != 200 && feignResponse.status() != 201) {
            throw new FeignApiCommunicationFailException();
        }

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberActivityDto.MemberActivity> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);

            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new FeignApiSerializationFailException();
        }
        catch (Exception e){
            throw new FeignApiCommunicationFailException();
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

    public MemberActivityDto.MemberActivity createPostLikeMemberActivity(MemberActivityDto.PostLikeActivityRequest request, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberActivityFeignClient.createPostLikeActivity(request, token);
        if (feignResponse.status() != 200 && feignResponse.status() != 201) {
            throw new FeignApiCommunicationFailException();
        }

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberActivityDto.MemberActivity> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);

            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new FeignApiSerializationFailException();
        }
        catch (Exception e){
            throw new FeignApiCommunicationFailException();
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

    public MemberActivityDto.MemberActivity createCommentLikeMemberActivity(MemberActivityDto.CommentLikeActivityRequest request, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberActivityFeignClient.createCommentLikeActivity(request, token);
        if (feignResponse.status() != 200 && feignResponse.status() != 201) {
            throw new FeignApiCommunicationFailException();
        }

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberActivityDto.MemberActivity> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);

            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new FeignApiSerializationFailException();
        }
        catch (Exception e){
            throw new FeignApiCommunicationFailException();
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
