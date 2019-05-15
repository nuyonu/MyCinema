package app.controller.services;

import lombok.Setter;

@Setter
public class Message {
    private String code;
    private StringBuilder messageEmail =new StringBuilder();

    public  Message(String code){
        messageEmail.append("Hi\n\n");
        messageEmail.append("We got a request to reset your MyCinema password\n");
        messageEmail.append("To reset your password, please click on the following link:");
        messageEmail.append("http://localhost:8443/accountReset?code=");
        messageEmail.append(code);
    }

    @Override
    public String toString() {
        return  messageEmail.toString() ;

    }
}
