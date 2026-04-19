package cruds.Ong.V2.infrastructure.config;

import cruds.Ong.V2.core.adapter.*;
import cruds.Ong.V2.core.application.usecase.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OngConfig {

    @Bean
    public CriarOngUseCase criarOngUseCase(OngGateway ongGateway,
                                          CriptografiaOngGateway criptografiaGateway) {
        return new CriarOngUseCase(ongGateway, criptografiaGateway);
    }

    @Bean
    public LoginOngUseCase loginOngUseCase(OngGateway ongGateway,
                                          CriptografiaOngGateway criptografiaGateway) {
        return new LoginOngUseCase(ongGateway, criptografiaGateway);
    }

    @Bean
    public BuscarOngPorIdUseCase buscarOngPorIdUseCase(OngGateway ongGateway) {
        return new BuscarOngPorIdUseCase(ongGateway);
    }

    @Bean
    public AtualizarOngUseCase atualizarOngUseCase(OngGateway ongGateway) {
        return new AtualizarOngUseCase(ongGateway);
    }

    @Bean
    public AtualizarSenhaOngUseCase atualizarSenhaOngUseCase(OngGateway ongGateway,
                                                            CriptografiaOngGateway criptografiaGateway) {
        return new AtualizarSenhaOngUseCase(ongGateway, criptografiaGateway);
    }

    @Bean
    public UploadImagemOngUseCase uploadImagemOngUseCase(OngGateway ongGateway,
                                                        ArmazenamentoImagemOngGateway armazenamentoGateway) {
        return new UploadImagemOngUseCase(ongGateway, armazenamentoGateway);
    }

    @Bean
    public BuscarImagemOngUseCase buscarImagemOngUseCase(OngGateway ongGateway) {
        return new BuscarImagemOngUseCase(ongGateway);
    }

    @Bean
    public ListarPetsOngUseCase listarPetsOngUseCase(PetOngGateway petOngGateway) {
        return new ListarPetsOngUseCase(petOngGateway);
    }

    @Bean
    public ListarMensagensPendentesUseCase listarMensagensPendentesUseCase(
            MensagemPendenteGateway mensagemPendenteGateway) {
        return new ListarMensagensPendentesUseCase(mensagemPendenteGateway);
    }

    @Bean
    public RemoverOngUseCase removerOngUseCase(OngGateway ongGateway, PetOngGateway petOngGateway) {
        return new RemoverOngUseCase(ongGateway, petOngGateway);
    }
}

