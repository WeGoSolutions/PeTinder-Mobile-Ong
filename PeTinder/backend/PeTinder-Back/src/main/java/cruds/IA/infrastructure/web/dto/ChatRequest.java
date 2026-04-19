package cruds.IA.infrastructure.web.dto;

public class ChatRequest {

    private String conversationId;
    private String message;
    private String imageUrl;

    public String getConversationId() {
        return conversationId;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
