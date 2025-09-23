package click.dailyfeed.feign.global.advice;

import click.dailyfeed.code.domain.member.member.exception.MemberException;
import click.dailyfeed.code.global.jwt.exception.JwtException;
import click.dailyfeed.code.global.web.code.ResponseSuccessCode;
import click.dailyfeed.code.global.web.response.DailyfeedErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE) // 가장 낮은 우선순위
public class FeignGlobalControllerAdvice {

    @ExceptionHandler(JwtException.class)
    public DailyfeedErrorResponse handleJwtException(
            JwtException e,
            HttpServletRequest request) {

        log.warn("Comment exception occurred: {}, path: {}",
                e.getJwtExceptionCode().getReason(),
                request.getRequestURI());

        return DailyfeedErrorResponse.of(
                e.getJwtExceptionCode().getCode(),
                ResponseSuccessCode.FAIL,
                e.getJwtExceptionCode().getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MemberException.class)
    public DailyfeedErrorResponse handleMemberException(
            MemberException e,
            HttpServletRequest request) {

        log.warn("Comment exception occurred: {}, path: {}",
                e.getMemberExceptionCode().getReason(),
                request.getRequestURI());

        return DailyfeedErrorResponse.of(
                e.getMemberExceptionCode().getCode(),
                ResponseSuccessCode.FAIL,
                e.getMemberExceptionCode().getMessage(),
                request.getRequestURI()
        );
    }

    // 일반적인 RuntimeException 처리 (예상치 못한 오류)
    @ExceptionHandler(RuntimeException.class)
    public DailyfeedErrorResponse handleRuntimeException(
            RuntimeException e,
            HttpServletRequest request) {

        log.error("Unexpected runtime exception occurred", e);

        return DailyfeedErrorResponse.of(
                500,
                ResponseSuccessCode.FAIL,
                "서버 내부 오류가 발생했습니다.",
                request.getRequestURI()
        );
    }

//    @ExceptionHandler(Exception.class)
//    public DailyfeedErrorResponse handleException(
//            Exception e,
//            HttpServletRequest request
//    ){
//        log.error("Unexpected exception occurred", e);
//
//        return DailyfeedErrorResponse.of(
//                500,
//                ResponseSuccessCode.FAIL,
//                "서버 내부 오류가 발생했습니다.",
//                request.getRequestURI()
//        );
//    }
}
