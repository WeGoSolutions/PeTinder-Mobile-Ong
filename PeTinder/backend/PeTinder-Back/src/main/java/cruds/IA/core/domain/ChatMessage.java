package cruds.IA.core.domain;

import java.util.List;

public class ChatMessage {

    private String role;
    private List<ChatContent> content;

    public ChatMessage(String role, List<ChatContent> content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public List<ChatContent> getContent() {
        return content;
    }
}
