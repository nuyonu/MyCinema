package App.Controller.ServiceController;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Cookie auth;
    private Cookie user;
    private Cookie conn;
    private final String FALSE_VALUE = "false";
    private final String TRUE_VALUE = "true";

    public CookieHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        auth = WebUtils.getCookie(this.request, "auth");
        user = WebUtils.getCookie(this.request, "user");
        conn = WebUtils.getCookie(this.request, "auth_con");
    }

    private void addToResponse() {
        auth.setMaxAge(24 * 60 * 60);
        user.setMaxAge(24 * 60 * 60);
        conn.setMaxAge(24 * 60 * 60);
        response.addCookie(auth);
        response.addCookie(user);
        response.addCookie(conn);
    }

    public void createCookie() {
        if (auth == null) {
            auth = new Cookie("auth", FALSE_VALUE);
            user = new Cookie("user", "none");
            conn = new Cookie("auth_con", FALSE_VALUE);
            addToResponse();
        }
    }

    public boolean isConnected() {
        if (conn == null) return false;
        return conn.getValue().equals(TRUE_VALUE);
    }

    public void setCookie(String username, boolean remainConnected) {
        createCookie();
        auth.setValue("true");
        if (remainConnected) conn.setValue(TRUE_VALUE);
        else conn.setValue(FALSE_VALUE);
        user.setValue(username);
        addToResponse();
    }
}
