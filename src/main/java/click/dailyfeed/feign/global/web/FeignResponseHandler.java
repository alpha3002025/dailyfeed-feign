package click.dailyfeed.feign.global.web;

import click.dailyfeed.code.domain.member.member.code.MemberHeaderCode;
import click.dailyfeed.code.domain.member.member.exception.MemberApiConnectionErrorException;
import click.dailyfeed.code.domain.member.member.exception.MemberForbiddenException;
import click.dailyfeed.code.domain.member.member.exception.MemberNotFoundException;
import click.dailyfeed.code.domain.member.member.exception.MemberUnauthorizedException;
import feign.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
public class FeignResponseHandler {
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

    public void checkResponseHeadersAndStatusOrThrow(Response feignResponse, HttpServletResponse httpResponse){
        final int status = feignResponse.status();
        if (status >= 200 && status < 300) {
            return;
        }

        if (status >= 400 && status < 500) {
            // HTTP 상태 코드에 따른 적절한 예외 처리
            if (status == 401) {
                log.error("Unauthorized request to member service - invalid or expired token");
                propagateTokenRefreshHeader(feignResponse, httpResponse);
                throw new MemberUnauthorizedException();
            } else if (status == 403) {
                log.error("Forbidden request to member service - insufficient permissions");
                throw new MemberForbiddenException();
            } else if (status == 404) {
                log.error("Member not found in member service");
                throw new MemberNotFoundException();
            } else if (status >= 500) {
                log.error("Member service internal error - status: {}", status);
                throw new MemberApiConnectionErrorException();
            } else {
                log.error("Unexpected member service error - status: {}", status);
                throw new MemberApiConnectionErrorException();
            }
        }
    }
}
