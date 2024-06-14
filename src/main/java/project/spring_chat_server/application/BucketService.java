package project.spring_chat_server.application;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BucketService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // 버킷 가져오기
    public Bucket resolveBucket(String sessionId) {
        return cache.computeIfAbsent(sessionId, this::newBucket);
    }

    // 버킷 생성
    private Bucket newBucket(String sessionId) {
        return Bucket4j.builder()
                // 버킷의 총 크기 = 5, 한 번에 충전되는 토큰 수  = 1, 1초마다 충전
                .addLimit(Bandwidth.classic(5, Refill.intervally(1, Duration.ofSeconds(1))))
                .build();
    }

}
