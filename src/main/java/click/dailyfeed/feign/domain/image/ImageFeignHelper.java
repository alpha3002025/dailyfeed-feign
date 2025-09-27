package click.dailyfeed.feign.domain.image;

import click.dailyfeed.code.domain.image.exception.ImageDeletingFailException;
import click.dailyfeed.code.domain.member.member.code.MemberHeaderCode;
import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import click.dailyfeed.code.domain.member.member.exception.MemberNotFoundException;
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
public class ImageFeignHelper {
    private final ImageFeignClient imageFeignClient;
    @Qualifier("feignObjectMapper") private final ObjectMapper feignObjectMapper;

    public Boolean deleteImages(MemberProfileDto.ImageDeleteBulkRequest request, String token) {
        Response feignResponse = imageFeignClient.deleteImagesBulkRequest(request, token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<Boolean> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
//            propagateTokenRefreshHeader(feignResponse, httpResponse);

            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new FeignApiSerializationFailException();
        }
        catch (Exception e){
            throw new ImageDeletingFailException();
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
