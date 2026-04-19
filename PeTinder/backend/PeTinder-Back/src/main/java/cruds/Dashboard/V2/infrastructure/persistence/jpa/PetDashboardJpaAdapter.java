package cruds.Dashboard.V2.infrastructure.persistence.jpa;

import cruds.Dashboard.V2.core.adapter.PetDashboardGateway;
import cruds.Dashboard.V2.core.domain.PetDashboard;
import cruds.Dashboard.V2.infrastructure.persistence.jpa.mapper.PetDashboardMapper;
import cruds.Pets.V2.infrastructure.persistence.jpa.PetJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PetDashboardJpaAdapter implements PetDashboardGateway {

    private final PetJpaRepository petRepository;

    public PetDashboardJpaAdapter(PetJpaRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public List<PetDashboard> listarPetsPorOngIdOrderByCurtidas(UUID ongId) {
        return petRepository.findByOngIdOrderByCurtidasDesc(ongId).stream()
                .map(PetDashboardMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetDashboard> listarPetsPorOngId(UUID ongId) {
        return petRepository.findByOngId(ongId).stream()
                .map(PetDashboardMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long contarPetsAdotadosPorOngId(UUID ongId) {
        return petRepository.findByOngId(ongId).stream()
                .filter(pet -> Boolean.TRUE.equals(pet.getIsAdotado()))
                .count();
    }

    @Override
    public long contarPetsNaoAdotadosPorOngId(UUID ongId) {
        return petRepository.findByOngId(ongId).stream()
                .filter(pet -> !Boolean.TRUE.equals(pet.getIsAdotado()))
                .count();
    }
}

