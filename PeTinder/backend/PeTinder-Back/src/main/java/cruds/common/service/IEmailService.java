package cruds.common.service;

public interface IEmailService {
    void enviarEmail(String destinatario, String assunto, String mensagem);
}
