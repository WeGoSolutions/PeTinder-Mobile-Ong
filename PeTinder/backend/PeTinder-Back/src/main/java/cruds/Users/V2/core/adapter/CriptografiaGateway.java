package cruds.Users.V2.core.adapter;

public interface CriptografiaGateway {

    String criptografarSenha(String senhaPlana);

    boolean verificarSenha(String senhaPlana, String senhaCriptografada);
}
