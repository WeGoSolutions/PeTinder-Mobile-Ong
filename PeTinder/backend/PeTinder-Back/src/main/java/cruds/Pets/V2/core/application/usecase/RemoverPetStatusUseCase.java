package cruds.Pets.V2.core.application.usecase;

import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.application.exception.PetException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RemoverPetStatusUseCase {

    private final PetStatusGateway petStatusGateway;

    public RemoverPetStatusUseCase(PetStatusGateway petStatusGateway) {
        this.petStatusGateway = petStatusGateway;
    }

    public void executar(UUID petId, UUID userId) {
        var petStatus = petStatusGateway.buscarPorPetEUsuario(petId, userId)
                .orElseThrow(() -> new PetException("Status não encontrado para pet " + petId + " e usuário " + userId));
        
        petStatusGateway.removerPorPetEUsuario(petId, userId);
    }
}
