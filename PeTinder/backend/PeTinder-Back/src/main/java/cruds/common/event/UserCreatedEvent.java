package cruds.common.event;

import cruds.Users.V2.infrastructure.web.dto.UsuarioResponseWebDTO;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {
    private final UsuarioResponseWebDTO user;

    public UserCreatedEvent(Object source, UsuarioResponseWebDTO user) {
        super(source);
        this.user = user;
    }

    public UsuarioResponseWebDTO getUser() {
        return user;
    }
}