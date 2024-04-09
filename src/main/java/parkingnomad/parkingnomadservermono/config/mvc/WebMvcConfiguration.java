package parkingnomad.parkingnomadservermono.config.mvc;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import parkingnomad.parkingnomadservermono.config.mvc.interceptor.ApiKeyInterceptor;
import parkingnomad.parkingnomadservermono.config.mvc.resolver.AuthArgumentResolver;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final AuthArgumentResolver authArgumentResolver;
    private final ApiKeyInterceptor apiKeyInterceptor;

    public WebMvcConfiguration(
            final AuthArgumentResolver authArgumentResolver,
            final ApiKeyInterceptor apiKeyInterceptor
    ) {
        this.authArgumentResolver = authArgumentResolver;
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/swagger-ui/index.html")
                .addPathPatterns("/api/docs");
    }
}
