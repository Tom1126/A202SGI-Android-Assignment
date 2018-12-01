package com.example.chqns022.androidassignment;

public class User {

    private String email;
    private String hashedPassword;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String password) {
        this.hashedPassword = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
    }
}
