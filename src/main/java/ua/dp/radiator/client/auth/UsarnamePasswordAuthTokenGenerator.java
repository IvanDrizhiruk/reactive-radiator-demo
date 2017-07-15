package ua.dp.radiator.client.auth;

import java.util.Base64;

public class UsarnamePasswordAuthTokenGenerator implements AuthTokenGenerator {

    private String username;
    private String password;

    public UsarnamePasswordAuthTokenGenerator(String username, String password) {
        this.username = username;
        this.password = (password == null ? "" : password);
    }

    @Override
    public String generateToken() {
        byte[] token = Base64.getEncoder().encode((this.username + ":" + this.password).getBytes());

        return new String(token);
    }
}
