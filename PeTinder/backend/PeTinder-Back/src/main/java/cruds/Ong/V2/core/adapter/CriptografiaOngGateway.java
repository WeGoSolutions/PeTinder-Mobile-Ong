package cruds.Ong.V2.core.adapter;

public interface CriptografiaOngGateway {

    String criptografarSenha(String senha);

    boolean verificarSenha(String senhaTexto, String senhaCriptografada);
}

