package cruds.Users.V2.core.adapter;

public interface CriptografiaImagemGateway {

    byte[] criptografarImagem(byte[] texto);

    byte[] descriptografarImagem(byte[] texto);

}