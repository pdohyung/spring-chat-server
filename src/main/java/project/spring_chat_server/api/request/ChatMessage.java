package project.spring_chat_server.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;

    private String roomId;

    private String sender;

    private String message;

    public void setEnterMessage(String sender) {
        this.message = sender + "님이 입장하셨습니다.";
    }

}
