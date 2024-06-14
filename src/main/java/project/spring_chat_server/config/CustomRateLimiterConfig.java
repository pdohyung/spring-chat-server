package project.spring_chat_server.config;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CustomRateLimiterConfig {

    @Bean
    public RateLimiter chatRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(1)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofSeconds(0))
                .build(); // 1초에 1개의 요청만 가능, timeout = 0이므로 즉시 처리율 제한
        return RateLimiter.of("chatRateLimiter", config);
    }

}
