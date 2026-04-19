package cruds.Pets.V2.core.application.command;

import java.util.List;
import java.util.UUID;

public class UploadImagemPetCommand {
    
    private final UUID petId;
    private final List<byte[]> imagensBytes;
    private final List<String> nomesArquivos;
    
    public UploadImagemPetCommand(UUID petId, List<byte[]> imagensBytes, List<String> nomesArquivos) {
        this.petId = petId;
        this.imagensBytes = imagensBytes;
        this.nomesArquivos = nomesArquivos;
    }
    
    public UUID getPetId() { return petId; }
    public List<byte[]> getImagensBytes() { return imagensBytes; }
    public List<String> getNomesArquivos() { return nomesArquivos; }
}