package cruds.Users.V2.core.adapter;

public interface EmailGateway {

    void enviarEmailBoasVindas(String email, String nome);

    void enviarEmail(String destinatario, String assunto, String conteudo);
}
