package com.esgi.mypunch.data.dtos;

public class Credentials {
    private String email;
    private String password; // don't forget to crypt it !

    public Credentials(String login, String password) {
        this.email = login;
        this.password = password;
    }

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

    @Override
    public String toString() {
        return "Credentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
