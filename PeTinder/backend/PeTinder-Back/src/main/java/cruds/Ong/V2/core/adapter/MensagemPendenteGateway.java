package cruds.Ong.V2.core.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MensagemPendenteGateway {

    List<MensagemPendente> listarMensagensPendentes(UUID ongId);

    public static class MensagemPendente {
        private UUID petId;
        private String petNome;
        private UUID userId;
        private String userName;
        private String userEmail;
        private byte[] userImage;
        private LocalDateTime dataStatus;

        public MensagemPendente(UUID petId, String petNome, UUID userId,
                               String userName, String userEmail, byte[] userImage, LocalDateTime dataStatus) {
            this.petId = petId;
            this.petNome = petNome;
            this.userId = userId;
            this.userName = userName;
            this.userEmail = userEmail;
            this.userImage = userImage;
            this.dataStatus = dataStatus;
        }

        // Getters
        public UUID getPetId() { return petId; }
        public String getPetNome() { return petNome; }
        public UUID getUserId() { return userId; }
        public String getUserName() { return userName; }
        public String getUserEmail() { return userEmail; }
        public byte[] getUserImage() { return userImage; }
        public LocalDateTime getDataStatus() { return dataStatus; }
    }
}

