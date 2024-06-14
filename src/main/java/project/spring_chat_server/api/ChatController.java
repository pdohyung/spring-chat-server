package project.spring_chat_server.api;

import io.github.bucket4j.Bucket;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import project.spring_chat_server.api.request.ChatMessage;
import project.spring_chat_server.application.BucketService;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessageSendingOperations messageTemplate;
    private final BucketService bucketService;
    private final RateLimiter chatRateLimiter;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        log.info("접근 세션 ID = {}, message = {}", sessionId, message.getMessage());

        Bucket bucket = bucketService.resolveBucket(sessionId);
        if (bucket.tryConsume(1)) {
            if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
                message.setEnterMessage(message.getSender());
            }
            messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
        } else {
            log.info("Bucket 처리율 제한");
        }
    }

//    @MessageMapping("/chat/message") // 웹 소켓 프로토콜
//    public void message(ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
//        String sessionId = headerAccessor.getSessionId(); // 세션 ID 추출
//        log.info("접근 세션 ID = {}, message = {}", sessionId, message.getMessage());
//
//        if (chatRateLimiter.acquirePermission()) { // 처리율 제한이 걸리지 않은 경우
//            if (ChatMessage.MessageType.ENTER.equals(message.getType())) { // 채팅방에 입장하는 경우
//                message.setEnterMessage(message.getSender());
//            }
//            messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message); // 메세지 전송
//        } else { // RateLimiter 처리율 제한이 발생한 경우
//            log.info("RateLimiter 처리율 제한");
//        }
//    }

}
