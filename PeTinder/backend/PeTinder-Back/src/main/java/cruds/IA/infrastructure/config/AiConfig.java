package cruds.IA.infrastructure.config;

import cruds.IA.core.application.service.ConversationMemoryService;
import cruds.IA.core.application.usecase.ChatWithAiUseCase;
import cruds.IA.core.domain.gateway.AiChatGateway;
import cruds.IA.infrastructure.external.GeminiAiClient;
import cruds.IA.infrastructure.gateway.GeminiAiChatGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiConfig {

    @Bean
    public GeminiAiClient geminiAiClient() {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("Content-Type", "application/json")
                .build();

        return new GeminiAiClient(webClient);
    }

    @Bean
    public AiChatGateway aiChatGateway(GeminiAiClient client) {
        return new GeminiAiChatGateway(client);
    }

    @Bean
    public ChatWithAiUseCase chatWithAiUseCase(
            AiChatGateway gateway,
            ConversationMemoryService memoryService) {

        return new ChatWithAiUseCase(memoryService, gateway);
    }
}
