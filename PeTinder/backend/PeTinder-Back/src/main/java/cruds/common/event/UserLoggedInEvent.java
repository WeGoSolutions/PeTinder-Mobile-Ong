package cruds.common.event;

import cruds.Users.V2.infrastructure.web.dto.UsuarioResponseWebDTO;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class UserLoggedInEvent extends ApplicationEvent {
    private final UsuarioResponseWebDTO user;
    private final LocalDateTime loginTime;

    public UserLoggedInEvent(Object source, UsuarioResponseWebDTO user, LocalDateTime loginTime) {
        super(source);
        this.user = user;
        this.loginTime = loginTime;
    }

    public UsuarioResponseWebDTO getUser() {
        return user;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }
}