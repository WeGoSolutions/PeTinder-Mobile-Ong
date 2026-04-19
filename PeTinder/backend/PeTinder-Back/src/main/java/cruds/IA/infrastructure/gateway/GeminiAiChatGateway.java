package cruds.IA.infrastructure.gateway;

import cruds.IA.core.domain.ChatMessage;
import cruds.IA.core.domain.gateway.AiChatGateway;
import cruds.IA.infrastructure.external.GeminiAiClient;

import java.util.List;

public class GeminiAiChatGateway implements AiChatGateway {

    private final GeminiAiClient client;

    public GeminiAiChatGateway(GeminiAiClient client) {
        this.client = client;
    }

    @Override
    public String chat(List<ChatMessage> history) {
        return client.chat(history);
    }
}
