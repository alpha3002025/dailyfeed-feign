package click.dailyfeed.feign.domain.image;

import click.dailyfeed.code.domain.image.exception.ImageDeletingFailException;
import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import click.dailyfeed.code.global.feign.exception.FeignApiSerializationFailException;
import click.dailyfeed.code.global.web.response.DailyfeedServerResponse;
import click.dailyfeed.feign.global.web.FeignResponseHandler;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageFeignHelper {
    private final ImageFeignClient imageFeignClient;
    private final FeignResponseHandler feignResponseHandler;

    @Qualifier("feignObjectMapper") private final ObjectMapper feignObjectMapper;

    public Boolean deleteImages(MemberProfileDto.ImageDeleteBulkRequest request, String token, HttpServletResponse httpResponse) {
        Response feignResponse = imageFeignClient.deleteImagesBulkRequest(request, token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<Boolean> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
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
}
