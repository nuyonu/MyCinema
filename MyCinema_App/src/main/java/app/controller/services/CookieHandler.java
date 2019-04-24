package app.controller.services;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
    public CookieHandler(HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest request1 = request;
        this.response = response;
        auth = WebUtils.getCookie(request1, "auth");
        user = WebUtils.getCookie(request1, "user");
        conn = WebUtils.getCookie(request1, "auth_con");
    }

    public void disconnect() {
        auth.setValue(FALSE_VALUE);
        conn.setValue(FALSE_VALUE);
        user.setValue("none");
        addToResponse();
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
        if (auth == null) return false;
        return auth.getValue().equals(TRUE_VALUE);
    }

    public void setCookie(String username, boolean remainConnected) {
        createCookie();
        auth.setValue("true");
        if (remainConnected) conn.setValue(TRUE_VALUE);
        else conn.setValue(FALSE_VALUE);
        user.setValue(username);
        addToResponse();
    }

    private void addToResponse() {
        auth.setMaxAge(24 * 60 * 60);
        user.setMaxAge(24 * 60 * 60);
        conn.setMaxAge(24 * 60 * 60);
        response.addCookie(auth);
        response.addCookie(user);
        response.addCookie(conn);
    }

    private HttpServletResponse response;
    private Cookie auth;
    private Cookie user;
    private Cookie conn;
    private static final String FALSE_VALUE = "false";

    private static final String TRUE_VALUE = "true";
}
