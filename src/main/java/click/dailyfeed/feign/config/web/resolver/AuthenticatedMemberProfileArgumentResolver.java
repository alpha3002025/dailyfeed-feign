package click.dailyfeed.feign.config.web.resolver;

import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import click.dailyfeed.code.global.jwt.exception.BearerTokenMissingException;
import click.dailyfeed.feign.config.web.annotation.AuthenticatedMemberProfile;
import click.dailyfeed.feign.domain.member.MemberFeignHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticatedMemberProfileArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberFeignHelper memberFeignHelper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticatedMemberProfile.class) &&
                parameter.getParameterType().equals(MemberProfileDto.MemberProfile.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);

        String authHeader = request.getHeader("Authorization");
        String jwt = extractToken(authHeader);

        // JWT 검증 및 멤버 추출 (예외 발생 가능)
        return memberFeignHelper.getMyProfile(jwt, response);
    }

    public String extractToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            throw new BearerTokenMissingException();
        }

        return authHeader.replace("Bearer ", "");
    }
}
