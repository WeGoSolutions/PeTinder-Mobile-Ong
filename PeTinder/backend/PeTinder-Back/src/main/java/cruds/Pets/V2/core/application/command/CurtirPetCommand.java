package cruds.Pets.V2.core.application.command;

import java.util.UUID;

public class CurtirPetCommand {

    private final UUID petId;
    private final UUID usuarioId;

    public CurtirPetCommand(UUID petId, UUID usuarioId) {
        this.petId = petId;
        this.usuarioId = usuarioId;
    }

    public UUID getPetId() {
        return petId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }
}
