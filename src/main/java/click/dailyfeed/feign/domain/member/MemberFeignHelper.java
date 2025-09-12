package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.follow.dto.FollowDto;
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
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberFeignHelper {
    private final MemberFeignClient memberClient;

    @Qualifier("feignObjectMapper")
    private final ObjectMapper feignObjectMapper;

    public MemberDto.Member getMember(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMember(token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberDto.Member> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
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

    public MemberDto.Member getMemberById(Long id, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMemberById(id, token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberDto.Member> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberDto.Member>>() {});
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

    public MemberDto.MemberProfile getMemberFollowSummary(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyProfile(token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberDto.MemberProfile> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberDto.MemberProfile>>() {});
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

    // todo (페이징처리가 필요하다) 페이징
    public FollowDto.FollowPage getMyFollowersFollowings(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyFollowersFollowings(token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<FollowDto.FollowPage> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
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

    public List<FollowDto.Following> getMyFollowingMembers(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyFollowingMembers(token);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }
        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<FollowDto.Following>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
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

    // 참고)
    // allAuthorIds 는 /api/follow 로부터 Page 기반으로 받아온 일정 범위의 최신 사용자이므로 allAuthorIds 가 커져서
    // 요청이 비대해질 일은 없다.
    public List<MemberDto.Member> getMembersList(Set<Long> allAuthorIds, HttpServletResponse httpResponse) {
        MemberDto.MembersIdsQuery request = MemberDto.MembersIdsQuery.builder()
                .ids(new ArrayList<>(allAuthorIds))
                .build();

        Response feignResponse = memberClient.getMemberList(request);
        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<MemberDto.Member>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<List<MemberDto.Member>>>() {});
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

    public Map<Long, MemberDto.Member> getMemberMap(Set<Long> authorIds, HttpServletResponse httpResponse) {
        MemberDto.MembersIdsQuery request = MemberDto.MembersIdsQuery.builder()
                .ids(new ArrayList<>(authorIds))
                .build();

        Response feignResponse = memberClient.getMemberList(request);
        propagateTokenRefreshHeader(feignResponse, httpResponse);

        if (feignResponse.status() != 200) {
            throw new MemberNotFoundException();
        }

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<MemberDto.Member>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<List<MemberDto.Member>>>() {});
            propagateTokenRefreshHeader(feignResponse, httpResponse);

            List<MemberDto.Member> authors = apiResponse.getData();
            return authors.stream().collect(Collectors.toMap(MemberDto.Member::getId, author -> author));
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
//
//    public DailyfeedPage<FollowDto.FollowingActivity> getRecentlyActiveFollowingMembers(String token, HttpServletResponse httpResponse) {
//        Response feignResponse = memberClient.getRecentActivitiesFromFollowing(token);
//        if (feignResponse.status() != 200) {
//            throw new MemberNotFoundException();
//        }
//        try{
//            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
//            DailyfeedPageResponse<FollowDto.FollowingActivity> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedPageResponse<FollowDto.FollowingActivity>>() {});
//            propagateTokenRefreshHeader(feignResponse, httpResponse);
//
//            return apiResponse.getBody();
//        }
//        catch (Exception e){
//            throw new MemberApiConnectionErrorException();
//        }
//        finally {
//            if( feignResponse.body() != null ){
//                try {
//                    feignResponse.body().close();
//                }
//                catch (Exception e){
//                    log.error("feign response close error", e);
//                }
//            }
//        }
//    }

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
