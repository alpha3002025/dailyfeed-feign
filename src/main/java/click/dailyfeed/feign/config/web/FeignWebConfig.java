package click.dailyfeed.feign.config.web;

import click.dailyfeed.feign.config.web.resolver.AuthenticatedMemberArgumentResolver;
import click.dailyfeed.feign.config.web.resolver.AuthenticatedMemberProfileArgumentResolver;
import click.dailyfeed.feign.config.web.resolver.AuthenticatedMemberProfileSummaryArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class FeignWebConfig implements WebMvcConfigurer {
    private final AuthenticatedMemberArgumentResolver authenticatedMemberArgumentResolver;
    private final AuthenticatedMemberProfileArgumentResolver authenticatedMemberProfileArgumentResolver;
    private final AuthenticatedMemberProfileSummaryArgumentResolver authenticatedMemberProfileSummaryArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberArgumentResolver);
        resolvers.add(authenticatedMemberProfileArgumentResolver);
        resolvers.add(authenticatedMemberProfileSummaryArgumentResolver);
    }
}
