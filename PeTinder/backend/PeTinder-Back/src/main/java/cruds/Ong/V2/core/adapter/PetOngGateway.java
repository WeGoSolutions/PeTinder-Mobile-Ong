    package cruds.Ong.V2.core.adapter;

    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;

    import java.util.List;
    import java.util.UUID;

    public interface PetOngGateway {

        Page<PetOngInfo> listarPetsPorOng(UUID ongId, Pageable pageable1);

        void removerPetsPorOng(UUID ongId);

        public static class PetOngInfo {
            private UUID ongId;
            private UUID petId;
            private String petNome;
            private Double idade;
            private String porte;
            private Integer curtidas;
            private List<String> tags;
            private String descricao;
            private Boolean isCastrado;
            private Boolean isVermifugo;
            private Boolean isVacinado;
            private List<String> imageUrl;
            private String sexo;
            private List<String> status;

            public PetOngInfo(UUID ongId, UUID petId, String petNome, Double idade, String porte,
                             Integer curtidas, List<String> tags, String descricao,
                             Boolean isCastrado, Boolean isVermifugo, Boolean isVacinado,
                             List<String> imageUrl, String sexo, List<String> status) {
                this.ongId = ongId;
                this.petId = petId;
                this.petNome = petNome;
                this.idade = idade;
                this.porte = porte;
                this.curtidas = curtidas;
                this.tags = tags;
                this.descricao = descricao;
                this.isCastrado = isCastrado;
                this.isVermifugo = isVermifugo;
                this.isVacinado = isVacinado;
                this.imageUrl = imageUrl;
                this.sexo = sexo;
                this.status = status;
            }

            // Getters
            public UUID getOngId() { return ongId; }
            public UUID getPetId() { return petId; }
            public String getPetNome() { return petNome; }
            public Double getIdade() { return idade; }
            public String getPorte() { return porte; }
            public Integer getCurtidas() { return curtidas; }
            public List<String> getTags() { return tags; }
            public String getDescricao() { return descricao; }
            public Boolean getIsCastrado() { return isCastrado; }
            public Boolean getIsVermifugo() { return isVermifugo; }
            public Boolean getIsVacinado() { return isVacinado; }
            public List<String> getImageUrl() { return imageUrl; }
            public String getSexo() { return sexo; }
            public List<String> getStatus() { return status; }
        }
    }
