package cruds.IA.infrastructure.external;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cruds.IA.core.domain.ChatContent;
import cruds.IA.core.domain.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeminiAiClient {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public GeminiAiClient(WebClient webClient) {
        this.webClient = webClient;
    }

    private static final String SYSTEM_PROMPT = """
            
        Seu objetivo é ajudar usuários com informações sobre pets, especialmente relacionadas a adoção responsável, cuidados, comportamento, alimentação, saúde básica e atividades para animais.
        Você responde de forma amigável, clara e educativa.
            
        Você pode responder perguntas relacionadas a:
        - Adoção de pets
        - Cuidados com animais (higiene, alimentação, rotina)
        - Comportamento animal
        - Brincadeiras e enriquecimento ambiental
        - Diferenças entre raças
        - Dicas para adaptação de pets em casa
        - Convivência entre pets e pessoas
        - Curiosidades sobre animais domésticos

        Você NÃO deve responder perguntas que não estejam relacionadas a pets.
        Isso inclui, mas não se limita a:
        - Política
        - Programação
        - Tecnologia
        - Finanças
        - Medicina humana
        - Assuntos gerais que não envolvam animais
            
            
        Antes de responder qualquer pergunta, verifique se ela está relacionada a pets ou animais domésticos.
        
        Se a pergunta NÃO for sobre pets:
        1. Não responda a pergunta.
        2. Informe educadamente que você só pode responder dúvidas sobre pets.
        3. Sugira que o usuário faça uma pergunta relacionada a animais.
        
        Se o usuário fizer uma pergunta fora do escopo, responda apenas:
        
        "🐾 Eu sou o assistente do Petinder e posso ajudar apenas com dúvidas sobre pets, como cuidados, comportamento, adoção ou brincadeiras com animais. \s
        Poderia fazer uma pergunta relacionada a pets?"
        
        - Tom amigável e acolhedor
        - Linguagem simples
        - Respostas claras e úteis
        - Sempre priorizar o bem-estar animal
    """;


    public String sendMessage(String message) {

        String body = """
        {
          "model": "gpt-4.1-mini",
          "input": [
            {
              "role": "system",
              "content": "%s"
            },
            {
              "role": "user",
              "content": "%s"
            }
          ]
        }
        """.formatted(SYSTEM_PROMPT ,message);

        return webClient.post()
                .uri("/v1/responses")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String chatWithImage(String message, String imageUrl) {

        String body = """
    {
      "model": "gpt-4.1-mini",
      "input": [
        {
          "role": "user",
          "content": [
            {"type": "input_text", "text": "%s"},
            {"type": "input_image", "image_url": "%s"}
          ]
        },
        {
          "role": "system",
          "content": "%s"
        }
      ]
    }
    """.formatted(SYSTEM_PROMPT, message, imageUrl);

        return webClient.post()
                .uri("/v1/responses")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public String chat(List<ChatMessage> history) {

        try {

            ObjectMapper mapper = new ObjectMapper();

            List<Map<String, Object>> contents = new ArrayList<>();

            for (ChatMessage msg : history) {

                List<Map<String, Object>> parts = new ArrayList<>();

                for (ChatContent content : msg.getContent()) {

                    if (content.getType().equals("text")) {

                        parts.add(Map.of(
                                "text", content.getValue()
                        ));

                    } else if (content.getType().equals("image")) {

                        parts.add(Map.of(
                                "inline_data", Map.of(
                                        "mime_type", "image/jpeg",
                                        "data", content.getValue()
                                )
                        ));
                    }
                }

                contents.add(Map.of(
                        "role", msg.getRole().equals("assistant") ? "model" : "user",
                        "parts", parts
                ));
            }

            Map<String, Object> request = Map.of(
                    "contents", contents
            );

            String body = mapper.writeValueAsString(request);

            String response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemini-flash-latest:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return extractText(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String extractText(String responseJson) {

        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseJson);

            return root
                    .path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao ler resposta do Gemini", e);
        }
    }

}