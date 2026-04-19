package cruds.Users.V2.infrastructure.config;

import cruds.Pets.V2.core.application.usecase.RemoverPorUsuarioUseCase;
import cruds.Users.V2.core.adapter.*;
import cruds.Users.V2.core.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioConfig {

    @Bean
    public CriarUsuarioUseCase criarUsuarioUseCase(UsuarioGateway usuarioGateway,
                                                   CriptografiaGateway criptografiaGateway,
                                                   EmailGateway emailGateway) {
        return new CriarUsuarioUseCase(usuarioGateway, criptografiaGateway, emailGateway);
    }

    @Bean
    public LoginUsuarioUseCase loginUsuarioUseCase(UsuarioGateway usuarioGateway,
                                                   CriptografiaGateway criptografiaGateway,
                                                   AutenticacaoGateway autenticacaoGateway) {
        return new LoginUsuarioUseCase(usuarioGateway, criptografiaGateway, autenticacaoGateway);
    }

    @Bean
    public BuscarUsuarioPorIdUseCase buscarUsuarioPorIdUseCase(UsuarioGateway usuarioGateway) {
        return new BuscarUsuarioPorIdUseCase(usuarioGateway);
    }

    @Bean
    public ListarUsuariosUseCase listarUsuariosUseCase(UsuarioGateway usuarioGateway) {
        return new ListarUsuariosUseCase(usuarioGateway);
    }

    @Bean
    public AtualizarInformacoesOpcionaisUseCase atualizarInformacoesOpcionaisUseCase(UsuarioGateway usuarioGateway) {
        return new AtualizarInformacoesOpcionaisUseCase(usuarioGateway);
    }

    @Bean
    public AtualizarSenhaUseCase atualizarSenhaUseCase(UsuarioGateway usuarioGateway, 
                                                      CriptografiaGateway criptografiaGateway) {
        return new AtualizarSenhaUseCase(usuarioGateway, criptografiaGateway);
    }

    @Bean
    public UploadImagemPerfilUseCase uploadImagemPerfilUseCase(UsuarioGateway usuarioGateway,
                                                              ArmazenamentoImagemGateway armazenamentoImagemGateway) {
        return new UploadImagemPerfilUseCase(usuarioGateway, armazenamentoImagemGateway);
    }

    @Bean
    public MarcarUsuarioExperienteUseCase marcarUsuarioExperienteUseCase(UsuarioGateway usuarioGateway) {
        return new MarcarUsuarioExperienteUseCase(usuarioGateway);
    }

    @Bean
    public RemoverUsuarioUseCase removerUsuarioUseCase(
            UsuarioGateway usuarioGateway,
            RemoverPorUsuarioUseCase removerPorUsuarioUseCase
    ) {
        return new RemoverUsuarioUseCase(usuarioGateway, removerPorUsuarioUseCase);
    }


    @Bean
    public ValidarEmailUseCase validarEmailUseCase(UsuarioGateway usuarioGateway) {
        return new ValidarEmailUseCase(usuarioGateway);
    }

    @Bean
    public AtualizarUsuarioUseCase atualizarUsuarioUseCase(UsuarioGateway usuarioGateway) {
        return new AtualizarUsuarioUseCase(usuarioGateway);
    }

    @Bean
    public RemoverTodosUsuariosUseCase removerTodosUsuariosUseCase(UsuarioGateway usuarioGateway) {
        return new RemoverTodosUsuariosUseCase(usuarioGateway);
    }
}
