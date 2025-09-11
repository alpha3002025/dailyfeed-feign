package click.dailyfeed.feign.global.mapper;

import click.dailyfeed.code.domain.member.member.code.MemberHeaderCode;
import click.dailyfeed.code.global.feign.exception.FeignApiCommunicationFailException;
import click.dailyfeed.code.global.web.response.DailyfeedServerResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ResponseMapper {
    ///  server response
    public static <T> DailyfeedServerResponse<T> toServerResponse(Response feignResponse, HttpServletResponse httpResponse, ObjectMapper objectMapper) {
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<T> apiResponse = objectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);
            return apiResponse;
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
                    e.printStackTrace();
                }
            }
        }
    }

    ///  page response


    ///  helper
    public static void propagateTokenRefreshHeader(Response feignResponse, HttpServletResponse httpResponse) {
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
