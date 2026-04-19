package cruds.IA.core.domain.gateway;

import cruds.IA.core.domain.ChatMessage;

import java.util.List;

public interface AiChatGateway {

    String chat(List<ChatMessage> history);

}