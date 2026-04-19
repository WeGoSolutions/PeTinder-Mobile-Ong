package cruds.Users.V2.core.adapter;

public interface AutenticacaoGateway {

    boolean autenticar(String email, String senha);

    String gerarToken(String email);

    boolean validarToken(String token);

    String extrairEmailDoToken(String token);
}
