package cruds.Dashboard.V2.core.domain;

import java.util.UUID;

public class Dashboard {

    private UUID id;
    private UUID ongId;

    public Dashboard(UUID id, UUID ongId) {
        this.id = id;
        this.ongId = ongId;
        validarDados();
    }

    public Dashboard(UUID ongId) {
        this(null, ongId);
    }

    private void validarDados() {
        if (ongId == null) {
            throw new IllegalArgumentException("OngId é obrigatório");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getOngId() {
        return ongId;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

