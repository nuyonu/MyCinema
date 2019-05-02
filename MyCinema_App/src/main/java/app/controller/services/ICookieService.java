package app.controller.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICookieService {
    String getUser();

    void disconnect();

    void createCookie();

    boolean isConnected();

    void setCookie(String username, boolean remainConnected);

    void setConfig(HttpServletRequest request, HttpServletResponse response);

}
