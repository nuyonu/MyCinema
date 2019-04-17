package app.controller.services;

import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
    private HttpServletResponse response;
    private static final String FALSE_VALUE = "false";
    private Cookie user;
    private static final String TRUE_VALUE = "true";
    private Cookie autentificate;
    private Cookie remainConnected;

    public CookieHandler(HttpServletRequest request, HttpServletResponse response) {
        HttpServletRequest request1 = request;
        this.response = response;
        autentificate = WebUtils.getCookie(request1, "autentificate");
        user = WebUtils.getCookie(request1, "user");
        remainConnected = WebUtils.getCookie(request1, "remainConnected");
    }

    private void addToResponse() {
        autentificate.setMaxAge(24 * 60 * 60);
        user.setMaxAge(24 * 60 * 60);
        remainConnected.setMaxAge(24 * 60 * 60);
        response.addCookie(autentificate);
        response.addCookie(user);
        response.addCookie(remainConnected);
    }

    public void createCookie() {
        if (autentificate == null) {
            autentificate = new Cookie("autentificate", FALSE_VALUE);
            user = new Cookie("user", "none");
            remainConnected = new Cookie("remainConnected", FALSE_VALUE);
            addToResponse();
        }
    }

    public boolean isConnected() {
        if (autentificate == null) return false;
        return autentificate.getValue().equals(TRUE_VALUE);
    }

    public void setCookie(String username, boolean remainConnected) {
        createCookie();
        autentificate.setValue("true");
        if (remainConnected) this.remainConnected.setValue(TRUE_VALUE);
        else this.remainConnected.setValue(FALSE_VALUE);
        user.setValue(username);
        addToResponse();
    }
}
