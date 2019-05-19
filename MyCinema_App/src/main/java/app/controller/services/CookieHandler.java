package app.controller.services;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieHandler implements ICookieService {

    @Override
    public  void  setConfig(HttpServletRequest request, HttpServletResponse response){
        this.response = response;
        user = WebUtils.getCookie(request, "user");
        secureConn = WebUtils.getCookie(request, "myCinema");
        if(!setted){
            encryptor.setPassword(password);
            encryptor.setAlgorithm(alg);
            setted=true;
        }
    }

    @Override
    public String getUser() {
        if (secureConn == null)
            return null;
        return encryptor.decrypt(secureConn.getValue()).substring(4);
    }

    @Override
    public void disconnect() {
        secureConn.setValue("none");
        user.setValue("none");
        addToResponse();
    }

    @Override
    public void createCookie() {
        if (secureConn == null) {
            user = new Cookie("user", "none");
            secureConn = new Cookie("myCinema", "none");
            addToResponse();
        }
    }

    @Override
    public boolean isConnected() {
        if (secureConn == null) return false;
        if (secureConn.getValue().equals("none")) return false;
        String conn = encryptor.decrypt(secureConn.getValue());
        conn = conn.substring(0, 3);
        return conn.equals("YES");
    }

    @Override
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
    @Value("${encrypt.password}")
    private String password;
    @Value("${encrypt.alg}")
    private String alg;
    private boolean setted=false;
}
