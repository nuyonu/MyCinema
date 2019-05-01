package app.controller.services;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHandler {
    public CookieHandler(HttpServletRequest request, HttpServletResponse response) {
        encryptor.setPassword("some-random-passwprd");
        encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
        HttpServletRequest request1 = request;
        this.response = response;
        user = WebUtils.getCookie(request1, "user");
        secureConn = WebUtils.getCookie(request1, "myCinema");
    }

    public String getUser() {
        if (secureConn == null)
            return null;
        return encryptor.decrypt(secureConn.getValue()).substring(4);
    }


    public void disconnect() {
        secureConn.setValue("none");
        user.setValue("none");
        addToResponse();
    }

    public void createCookie() {
        if (secureConn == null) {
            user = new Cookie("user", "none");
            secureConn = new Cookie("myCinema", "none");
            addToResponse();
        }
    }

    public boolean isConnected() {
        if (secureConn.getValue().equals("none"))
            return false;
        String conn = encryptor.decrypt(secureConn.getValue());

        conn = conn.substring(0, 3);
        return conn.equals("YES");
    }

    public void setCookie(String username, boolean remainConnected) {
        createCookie();
        if (remainConnected) secureConn.setValue(encryptor.encrypt("YESY" + username));
        else secureConn.setValue(encryptor.encrypt("YESN " + username));
        user.setValue(username);
        addToResponse();
    }

    private void addToResponse() {
        user.setMaxAge(24 * 60 * 60);
        response.addCookie(user);
        secureConn.setMaxAge(24 * 60 * 60);
        response.addCookie(secureConn);
    }


    private HttpServletResponse response;
    private Cookie user;
    private Cookie secureConn;
    private StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();


}
