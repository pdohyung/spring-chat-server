package project.spring_chat_server.api;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import project.spring_chat_server.api.request.ChatMessage;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations messageTemplate;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setEnterMessage(message.getSender());
            //message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
