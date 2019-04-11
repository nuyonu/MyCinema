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

    public CookieHandler(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        Cookie[] cookies = request.getCookies();
        auth = WebUtils.getCookie(request, "auth");
        user = WebUtils.getCookie(request, "user");
        conn = WebUtils.getCookie(request, "auth_con");
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
            auth = new Cookie("auth", "false");
            user = new Cookie("user", "none");
            conn = new Cookie("auth_con", "false");
            addToResponse();
        }
    }

    public boolean isCoonected() {
        if (conn == null) return false;
        return conn.getValue().equals("true");
    }

    public void setCookie(String username, boolean remainConnected) {
        createCookie();
        auth.setValue("true");
        if (remainConnected) conn.setValue("true");
        else conn.setValue("false");
        user.setValue(username);
        addToResponse();
    }
}
