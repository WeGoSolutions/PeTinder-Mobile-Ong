package cruds.Pets.V2.infrastructure.web.service;

import cruds.Ong.V2.core.adapter.OngGateway;
import cruds.Ong.V2.infrastructure.web.dto.OngResponseWebDTO;
import cruds.Pets.V2.core.adapter.PetGateway;
import cruds.Pets.V2.core.adapter.PetStatusGateway;
import cruds.Pets.V2.core.domain.Pet;
import cruds.Pets.V2.core.domain.PetStatus;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.core.application.exception.PetException;
import cruds.Pets.V2.infrastructure.web.dto.*;
import cruds.Users.V2.core.adapter.UsuarioGateway;
import cruds.Users.V2.core.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Infrastructure service for complex cross-domain Pet Status queries.
 * This orchestrates data from Pet, User, and Ong domains.
 */
@Service
public class PetStatusQueryService {

    private final PetGateway petGateway;
    private final PetStatusGateway petStatusGateway;
    private final UsuarioGateway usuarioGateway;
    private final OngGateway ongGateway;

    public PetStatusQueryService(PetGateway petGateway, 
                                PetStatusGateway petStatusGateway,
                                UsuarioGateway usuarioGateway,
                                OngGateway ongGateway) {
        this.petGateway = petGateway;
        this.petStatusGateway = petStatusGateway;
        this.usuarioGateway = usuarioGateway;
        this.ongGateway = ongGateway;
    }

    public Page<Map<String, Object>> getAllPetsWithUserStatus(Pageable pageable) {
        // Buscar todos os pets
        List<Pet> allPets = petGateway.listarTodos();
        
        // Buscar todos os usuários
        List<Usuario> allUsers = usuarioGateway.listarTodos();
        
        List<Map<String, Object>> result = new ArrayList<>();
        
        for (Pet pet : allPets) {
            for (Usuario user : allUsers) {
                var statusOpt = petStatusGateway.buscarPorPetEUsuario(pet.getId(), user.getId());
                String statusStr = statusOpt.map(ps -> ps.getStatus().toString()).orElse(null);
                
                Map<String, Object> map = new HashMap<>();
                map.put("id_pet", pet.getId().toString());
                map.put("id_user", user.getId().toString());
                map.put("nome_pet", pet.getNome());
                map.put("status", statusStr);
                result.add(map);
            }
        }
        
        // Aplicar paginação manualmente
        int start = (int) pageable.getOffset();
        if (start >= result.size()) {
            return new PageImpl<>(List.of(), pageable, result.size());
        }
        int end = Math.min((start + pageable.getPageSize()), result.size());
        List<Map<String, Object>> paginatedResult = result.subList(start, end);
        
        return new PageImpl<>(paginatedResult, pageable, result.size());
    }

    public List<PetResponseGeralWebDTO> listAvailablePetsForUser(UUID userId) {
        // Verificar se usuário existe
        usuarioGateway.buscarPorId(userId)
                .orElseThrow(() -> new PetException("Usuário não encontrado"));
        
        // Buscar pets não interagidos
        List<Pet> pets = petGateway.listarPetsNaoInteragidosPorUsuario(userId);
        
        // Filtrar apenas pets não adotados e construir DTOs
        return pets.stream()
                .filter(Pet::estaDisponivel)
                .map(pet -> buildPetResponseGeralWebDTO(pet))
                .collect(Collectors.toList());
    }

    public Page<PetResponseGeralWebDTO> listDefaultPets(UUID userId, Pageable pageable) {
        // Verificar se usuário existe
        usuarioGateway.buscarPorId(userId)
                .orElseThrow(() -> new PetException("Usuário não encontrado"));
        
        // Buscar todos os pets
        List<Pet> allPets = petGateway.listarTodos();
        
        // Filtrar pets não adotados e sem status com o usuário
        List<PetResponseGeralWebDTO> availablePets = allPets.stream()
                .filter(Pet::estaDisponivel)
                .filter(pet -> !petStatusGateway.existePorPetEUsuario(pet.getId(), userId))
                .map(this::buildPetResponseGeralWebDTO)
                .collect(Collectors.toList());
        
        if (availablePets.isEmpty()) {
            throw new PetException("Nenhum pet default encontrado para o usuário");
        }
        
        // Aplicar paginação manualmente
        int start = (int) pageable.getOffset();
        if (start >= availablePets.size()) {
            return new PageImpl<>(List.of(), pageable, availablePets.size());
        }
        int end = Math.min((start + pageable.getPageSize()), availablePets.size());
        List<PetResponseGeralWebDTO> paginatedResult = availablePets.subList(start, end);
        
        return new PageImpl<>(paginatedResult, pageable, availablePets.size());
    }

    public List<PetResponsePendingOngWebDTO> listPendingPetsWithOngForUser(UUID userId) {
        // Verificar se usuário existe
        usuarioGateway.buscarPorId(userId)
                .orElseThrow(() -> new PetException("Usuário não encontrado"));
        
        String baseUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .build()
                .toUriString();
        
        // Buscar pets com status PENDING para o usuário
        List<PetStatus> pendingStatuses = petStatusGateway.buscarPorUsuarioEStatus(userId, PetStatusEnum.PENDING);
        
        return pendingStatuses.stream()
                .map(status -> {
                    Pet pet = petGateway.buscarPorId(status.getPetId())
                            .orElseThrow(() -> new PetException("Pet não encontrado"));
                    
                    var ong = ongGateway.buscarPorId(pet.getOngId())
                            .orElse(null);
                    
                    List<String> imageUrls = new ArrayList<>();
                    if (pet.getImagens() != null) {
                        imageUrls = IntStream.range(0, pet.getImagens().size())
                                .mapToObj(i -> baseUrl + "/api/pets/" + pet.getId() + "/imagens/" + i)
                                .collect(Collectors.toList());
                    }
                    
                    return new PetResponsePendingOngWebDTO(
                            userId,
                            pet.getId(),
                            pet.getNome(),
                            pet.getIdade(),
                            pet.getPorte(),
                            pet.getDescricao(),
                            pet.getIsCastrado(),
                            pet.getIsVermifugo(),
                            pet.getIsVacinado(),
                            imageUrls,
                            pet.getSexo(),
                            pet.getOngId(),
                            ong != null ? OngResponseWebDTO.fromDomain(ong) : null
                    );
                })
                .collect(Collectors.toList());
    }

    public List<PetResponseUserPendenteWebDTO> listPendingUsersByPetId(UUID petId) {
        // Verificar se pet existe
        petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException("Pet não encontrado"));
        
        // Buscar usuários com status PENDING para este pet
        List<PetStatus> pendingStatuses = petStatusGateway.buscarPorPet(petId).stream()
                .filter(ps -> ps.getStatus() == PetStatusEnum.PENDING)
                .collect(Collectors.toList());
        
        if (pendingStatuses.isEmpty()) {
            throw new PetException("Nenhum usuário pendente encontrado para o pet");
        }
        
        return pendingStatuses.stream()
                .map(status -> {
                    Usuario user = usuarioGateway.buscarPorId(status.getUserId())
                            .orElse(null);
                    
                    String imageUrl = null;
                    if (user != null && user.getImagemUsuario() != null) {
                        imageUrl = "http://localhost:8080/api/users/" + user.getId() + "/imagens/0";
                    }
                    
                    return PetResponseUserPendenteWebDTO.builder()
                            .idPet(petId)
                            .idUser(status.getUserId())
                            .nomeUser(user != null ? user.getNome() : null)
                            .imageUrl(imageUrl)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public PetResponseAdotanteWebDTO getAdoptedInfoByPetId(UUID petId) {
        // Buscar pet
        Pet pet = petGateway.buscarPorId(petId)
                .orElseThrow(() -> new PetException("Pet não encontrado"));
        
        // Buscar status ADOPTED
        List<PetStatus> statuses = petStatusGateway.buscarPorPet(petId);
        Optional<PetStatus> adoptedStatusOpt = statuses.stream()
                .filter(s -> s.getStatus() == PetStatusEnum.ADOPTED)
                .findFirst();
        
        if (adoptedStatusOpt.isEmpty()) {
            throw new PetException("Pet não está adotado");
        }
        
        PetStatus adoptedStatus = adoptedStatusOpt.get();
        Usuario user = usuarioGateway.buscarPorId(adoptedStatus.getUserId())
                .orElseThrow(() -> new PetException("Usuário não encontrado"));
        
        PetResponseAdotanteWebDTO dto = new PetResponseAdotanteWebDTO();
        dto.setPetId(pet.getId());
        dto.setNomePet(pet.getNome());
        dto.setIdadePet(pet.getIdade());
        dto.setPortePet(pet.getPorte());
        dto.setCurtidas(pet.getCurtidas());
        dto.setDescricao(pet.getDescricao());
        dto.setTags(pet.getTags());
        dto.setIsCastrado(pet.getIsCastrado());
        dto.setIsVermifugo(pet.getIsVermifugo());
        dto.setIsVacinado(pet.getIsVacinado());
        dto.setSexoPet(pet.getSexo());
        dto.setUserId(user.getId());
        dto.setImagemUsuarioId(user.getId());
        
        if (user.getImagemUsuario() != null && user.getImagemUsuario().getDados() != null) {
            String base64Image = Base64.getEncoder().encodeToString(user.getImagemUsuario().getDados());
            dto.setImagemUrl("data:image/png;base64," + base64Image);
        }
        
        dto.setNomeUsuario(user.getNome());
        dto.setEmail(user.getEmail());
        dto.setDataNascUsuario(user.getDataNascimento());
        dto.setCpf(user.getCpf());
        
        return dto;
    }

    private PetResponseGeralWebDTO buildPetResponseGeralWebDTO(Pet pet) {
        // Buscar ONG
        var ong = ongGateway.buscarPorId(pet.getOngId()).orElse(null);
        
        PetResponseGeralWebDTO.EnderecoResponseWebDTO enderecoDTO = null;
        if (ong != null && ong.getEndereco() != null) {
            enderecoDTO = new PetResponseGeralWebDTO.EnderecoResponseWebDTO(
                    ong.getEndereco().getCep(),
                    ong.getEndereco().getRua(),
                    ong.getEndereco().getNumero(),
                    ong.getEndereco().getCidade(),
                    ong.getEndereco().getUf(),
                    ong.getEndereco().getComplemento()
            );
        }
        
        return PetResponseGeralWebDTO.fromDomain(
                pet,
                pet.getOngId(),
                ong != null ? ong.getNome() : null,
                ong != null ? ong.getLink() : null,
                enderecoDTO
        );
    }
    
    /**
     * Gets the pet name by its ID.
     * Used by NotificacaoFanoutService to send notifications.
     */
    public String getPetNomeById(UUID petId) {
        return petGateway.buscarPorId(petId)
                .map(Pet::getNome)
                .orElse("");
    }
}
