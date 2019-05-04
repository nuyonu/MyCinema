package app.controller.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Pattern;

@Service("captcha")
public class CaptchaService {


    public boolean processResponse(String response, String ip, String key, String url) {
        if (!responseSanityCheck(response)) return false;
        URI verifyUri = URI.create(String.format(url, key, response, ip));
        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
        return googleResponse.isSuccess();
    }


    private boolean responseSanityCheck(String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }


    private RestOperations restTemplate = new RestTemplate();
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");



}