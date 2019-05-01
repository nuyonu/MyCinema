package app.controller.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Pattern;

@Service
public class CaptchaService {


    public boolean processResponse(String response, String ip) {
        if (!responseSanityCheck(response)) return false;

        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                "6LdThqAUAAAAAM3FE-xVMORaVMqpppgRl5qqixFd",//todo config
                response,
                ip));

        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

        return googleResponse.isSuccess();
    }


    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }


    private RestOperations restTemplate = new RestTemplate();

    @Value("${google.recaptcha.key.secret}")
    private String recaptchaSecret;


    private final static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

}