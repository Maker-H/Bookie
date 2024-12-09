package web.bookie.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import web.bookie.domain.UserEntity;
import web.bookie.exceptions.errors.AuthError;

@Component
public class SessionManager {

    public void startSession(HttpServletRequest request, UserEntity user) {
        HttpSession session = request.getSession(true);
        session.setAttribute("user", user);
    }

    public UserEntity getUserEntityFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            AuthError.USER_NOT_VALID.throwException();
        }
        return (UserEntity) session.getAttribute("user");
    }

    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

}
