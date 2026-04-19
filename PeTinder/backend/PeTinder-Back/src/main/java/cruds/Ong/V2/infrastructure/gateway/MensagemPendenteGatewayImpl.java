package cruds.Ong.V2.infrastructure.gateway;

import cruds.Ong.V2.core.adapter.MensagemPendenteGateway;
import cruds.Pets.V2.core.domain.PetStatusEnum;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusEntity;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetStatusJpaRepository;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioJpaRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class MensagemPendenteGatewayImpl implements MensagemPendenteGateway {

    private final PetJpaRepository petRepository;
    private final PetStatusJpaRepository petStatusRepository;
    private final UsuarioJpaRepository usuarioRepository;

    public MensagemPendenteGatewayImpl(PetJpaRepository petRepository,
                                       PetStatusJpaRepository petStatusRepository,
                                       UsuarioJpaRepository usuarioRepository) {
        this.petRepository = petRepository;
        this.petStatusRepository = petStatusRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<MensagemPendente> listarMensagensPendentes(UUID ongId) {
        List<PetEntity> petsOng = petRepository.findByOngId(ongId);
        List<MensagemPendente> mensagensPendentes = new ArrayList<>();

        for (PetEntity pet : petsOng) {
            List<PetStatusEntity> statusList = petStatusRepository.findByPetIdAndStatus(
                pet.getId(),
                PetStatusEnum.PENDING
            );

            for (PetStatusEntity status : statusList) {
                Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findById(status.getUserId());
                if (usuarioOpt.isPresent()) {
                    UsuarioEntity usuario = usuarioOpt.get();
                    byte[] imagemDados = usuario.getImagemUser() != null ? usuario.getImagemUser().getDados() : null;
                    mensagensPendentes.add(new MensagemPendente(
                        pet.getId(),
                        pet.getNome(),
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        imagemDados,
                        status.getAlteradoParaPending()
                    ));
                }
            }
        }

        return mensagensPendentes;
    }
}

