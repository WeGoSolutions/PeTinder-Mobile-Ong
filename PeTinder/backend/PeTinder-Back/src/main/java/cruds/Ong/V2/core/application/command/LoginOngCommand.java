package cruds.Ong.V2.core.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginOngCommand {
    private String email;
    private String senha;
}

