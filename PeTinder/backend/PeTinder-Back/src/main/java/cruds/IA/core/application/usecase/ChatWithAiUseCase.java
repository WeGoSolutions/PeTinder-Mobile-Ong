package cruds.IA.core.application.usecase;

import cruds.IA.core.application.service.ConversationMemoryService;
import cruds.IA.core.domain.ChatContent;
import cruds.IA.core.domain.ChatMessage;
import cruds.IA.core.domain.gateway.AiChatGateway;

import java.util.List;

public class ChatWithAiUseCase {

    private final AiChatGateway aiChatGateway;
    private final ConversationMemoryService memoryService;

    public ChatWithAiUseCase(
            ConversationMemoryService memoryService,
            AiChatGateway aiChatGateway) {

        this.memoryService = memoryService;
        this.aiChatGateway = aiChatGateway;
    }

    public String chat(String conversationId, String message) {

        ChatMessage userMessage = new ChatMessage(
                "user",
                List.of(new ChatContent("text", message))
        );

        memoryService.addMessage(conversationId, userMessage);

        List<ChatMessage> history = memoryService.getHistory(conversationId);

        String response = aiChatGateway.chat(history);

        memoryService.addMessage(
                conversationId,
                new ChatMessage("assistant",
                        List.of(new ChatContent("text", response)))
        );

        return response;
    }
}
