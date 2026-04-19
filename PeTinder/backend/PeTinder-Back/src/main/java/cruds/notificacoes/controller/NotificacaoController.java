package cruds.notificacoes.controller;

import cruds.notificacoes.dto.NotificacaoDTO;
import cruds.notificacoes.service.NotificacaoFilaService;
import cruds.notificacoes.storage.NotificacaoMemoriaStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de notificações")
public class NotificacaoController {

    private final NotificacaoFilaService notificacaoFilaService;
    private final NotificacaoMemoriaStorage notificacaoStorage;

    @Operation(summary = "Lista todas as notificações não visualizadas de um usuário")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificacaoDTO>> buscarNotificacoes(@PathVariable UUID userId) {
        List<NotificacaoDTO> notificacoes = notificacaoStorage.listarNotificacoesNaoLidas(userId);

        return ResponseEntity.ok(notificacoes);
    }

    @Operation(summary = "Deleta uma notificação específica")
    @DeleteMapping("/{userId}/{notifyId}")
    public ResponseEntity<Void> deletarNotificacao(
            @PathVariable UUID userId,
            @PathVariable UUID notifyId) {

        boolean sucesso = notificacaoFilaService.deletarNotificacao(userId, notifyId);

        if (sucesso) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}