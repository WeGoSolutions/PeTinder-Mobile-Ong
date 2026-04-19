package cruds.IA.core.application.service;

import cruds.IA.core.domain.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConversationMemoryService {

    private final Map<String, List<ChatMessage>> conversations = new HashMap<>();

    public List<ChatMessage> getHistory(String conversationId) {
        return conversations.getOrDefault(conversationId, new ArrayList<>());
    }

    public void addMessage(String conversationId, ChatMessage message) {

        List<ChatMessage> history =
                conversations.getOrDefault(conversationId, new ArrayList<>());

        history.add(message);

        conversations.put(conversationId, history);
    }
}
