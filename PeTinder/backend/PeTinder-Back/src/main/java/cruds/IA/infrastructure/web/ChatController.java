package cruds.IA.infrastructure.web;

import cruds.IA.core.application.usecase.ChatWithAiUseCase;
import cruds.IA.core.domain.ChatContent;
import cruds.IA.core.domain.ChatMessage;
import cruds.IA.infrastructure.web.dto.ChatRequest;
import cruds.IA.infrastructure.web.dto.ChatResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatWithAiUseCase chatUseCase;

    public ChatController(ChatWithAiUseCase chatUseCase) {
        this.chatUseCase = chatUseCase;
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {

        String conversationId = request.getConversationId();

        if (conversationId == null) {
            conversationId = UUID.randomUUID().toString();
        }

        ChatMessage userMessage;

        if (request.getImageUrl() != null) {

            userMessage = new ChatMessage(
                    "user",
                    List.of(
                            new ChatContent("text", request.getMessage()),
                            new ChatContent("image", request.getImageUrl())
                    )
            );

        } else {

            userMessage = new ChatMessage(
                    "user",
                    List.of(
                            new ChatContent("text", request.getMessage())
                    )
            );
        }

        String response = chatUseCase.chat(conversationId, request.getMessage());

        return new ChatResponse(response);
    }
}