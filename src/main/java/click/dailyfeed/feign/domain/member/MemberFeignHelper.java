package click.dailyfeed.feign.domain.member;

import click.dailyfeed.code.domain.member.follow.dto.FollowDto;
import click.dailyfeed.code.domain.member.member.dto.MemberDto;
import click.dailyfeed.code.domain.member.member.dto.MemberProfileDto;
import click.dailyfeed.code.domain.member.member.exception.MemberApiConnectionErrorException;
import click.dailyfeed.code.domain.member.member.exception.MemberFeignSerializeFailException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberFeignHelper {
    private final MemberFeignClient memberClient;
    private final FeignResponseHandler feignResponseHandler;

    @Qualifier("feignObjectMapper")
    private final ObjectMapper feignObjectMapper;

    public MemberDto.Member getMember(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMember(token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberDto.Member> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberDto.Member> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberDto.Member>>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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

    public MemberProfileDto.MemberProfile getMyProfile(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyProfile(token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberProfileDto.MemberProfile> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberProfileDto.MemberProfile>>() {});
            log.debug("Successfully retrieved member profile from member service");
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
        }
        catch (Exception e){
            log.error("Member API connection error", e);
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

    public MemberProfileDto.Summary getMyProfileSummary(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyProfileSummary(token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberProfileDto.Summary> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberProfileDto.Summary>>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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

    public MemberProfileDto.MemberProfile getMemberProfileById(Long memberId, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getProfileById(token, memberId);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberProfileDto.MemberProfile> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberProfileDto.MemberProfile>>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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

    public MemberProfileDto.Summary getMemberSummaryById(Long memberId, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getSummaryById(token, memberId);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<MemberProfileDto.Summary> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<MemberProfileDto.Summary>>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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


    public FollowDto.FollowScrollPage getMyFollowersFollowings(Integer page, Integer size, String sort, String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyFollowersFollowings(token, page, size, sort);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<FollowDto.FollowScrollPage> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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

    public List<MemberProfileDto.Summary> getMyFollowingMembers(String token, HttpServletResponse httpResponse) {
        Response feignResponse = memberClient.getMyFollowingMembers(token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<MemberProfileDto.Summary>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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
    public List<MemberProfileDto.Summary> getMembersList(Set<Long> allAuthorIds, String token, HttpServletResponse httpResponse) {
        MemberDto.MembersIdsQuery request = MemberDto.MembersIdsQuery.builder()
                .ids(new ArrayList<>(allAuthorIds))
                .build();

        Response feignResponse = memberClient.getMemberList(request, token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<MemberProfileDto.Summary>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<List<MemberProfileDto.Summary>>>() {});
            return apiResponse.getData();
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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

    public Map<Long, MemberProfileDto.Summary> getMemberMap(Set<Long> authorIds, String token, HttpServletResponse httpResponse) {
        MemberDto.MembersIdsQuery request = MemberDto.MembersIdsQuery.builder()
                .ids(new ArrayList<>(authorIds))
                .build();

        Response feignResponse = memberClient.getMemberList(request, token);
        feignResponseHandler.checkResponseHeadersAndStatusOrThrow(feignResponse, httpResponse);

        try{
            String feignResponseBody = IOUtils.toString(feignResponse.body().asInputStream(), StandardCharsets.UTF_8);
            DailyfeedServerResponse<List<MemberProfileDto.Summary>> apiResponse = feignObjectMapper.readValue(feignResponseBody, new TypeReference<DailyfeedServerResponse<List<MemberProfileDto.Summary>>>() {});
            List<MemberProfileDto.Summary> authors = apiResponse.getData();
            return authors.stream().collect(Collectors.toMap(MemberProfileDto.Summary::getMemberId, author -> author));
        }
        catch (InvalidFormatException e){
            log.error("feign exception {} ", e.getMessage());
            e.printStackTrace();
            throw new MemberFeignSerializeFailException();
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
}
