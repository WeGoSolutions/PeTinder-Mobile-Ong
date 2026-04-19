package cruds.Users.V2.infrastructure.persistence.jpa.mapper;

import cruds.Users.V2.core.domain.Endereco;
import cruds.Users.V2.core.domain.ImagemUsuario;
import cruds.Users.V2.core.domain.Usuario;
import cruds.Users.V2.infrastructure.persistence.jpa.EnderecoEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.ImagemUsuarioEntity;
import cruds.Users.V2.infrastructure.persistence.jpa.UsuarioEntity;

public class UsuarioMapper {

    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioEntity.UsuarioEntityBuilder builder = UsuarioEntity.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .senha(usuario.getSenha())
                .dataNasc(usuario.getDataNascimento())
                .cpf(usuario.getCpf())
                .userNovo(usuario.getUsuarioNovo());

        if (usuario.getEndereco() != null) {
            builder.endereco(toEnderecoEntity(usuario.getEndereco()));
        }

        if (usuario.getImagemUsuario() != null) {
            builder.imagemUser(toImagemUsuarioEntity(usuario.getImagemUsuario()));
        }

        return builder.build();
    }

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        Usuario usuario = new Usuario(
            entity.getId(),
            entity.getNome(),
            entity.getEmail(),
            entity.getSenha(),
            entity.getDataNasc(),
            entity.getCpf(),
            entity.getUserNovo()
        );

        if (entity.getId() != null) {
            usuario.setId(entity.getId());
        }

        if (entity.getEndereco() != null) {
            usuario.atualizarEndereco(toEnderecoDomain(entity.getEndereco()));
        }

        if (entity.getImagemUser() != null) {
            usuario.atualizarImagemUsuario(toImagemUsuarioDomain(entity.getImagemUser()));
        }

        return usuario;
    }

    private static EnderecoEntity toEnderecoEntity(Endereco endereco) {
        if (endereco == null) return null;

        return EnderecoEntity.builder()
                .id(endereco.getId())
                .cep(endereco.getCep())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .cidade(endereco.getCidade())
                .uf(endereco.getUf())
                .complemento(endereco.getComplemento())
                .build();
    }

    private static Endereco toEnderecoDomain(EnderecoEntity entity) {
        if (entity == null) return null;

        return new Endereco(
            entity.getId(),
            entity.getCep(),
            entity.getRua(),
            entity.getNumero(),
            entity.getCidade(),
            entity.getUf(),
            entity.getComplemento()
        );
    }

    private static ImagemUsuarioEntity toImagemUsuarioEntity(ImagemUsuario imagemUsuario) {
        if (imagemUsuario == null) return null;

        return ImagemUsuarioEntity.builder()
                .id(imagemUsuario.getId())
                .dados(imagemUsuario.getDados())
                .arquivo(imagemUsuario.getNomeArquivo())
                .build();
    }

    private static ImagemUsuario toImagemUsuarioDomain(ImagemUsuarioEntity entity) {
        if (entity == null) return null;

        return new ImagemUsuario(
            entity.getId(),
            entity.getDados(),
            entity.getArquivo()
        );
    }
}
