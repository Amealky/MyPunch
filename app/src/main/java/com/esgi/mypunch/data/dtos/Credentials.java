package com.esgi.mypunch.data.dtos;

public class Credentials {
    private String login;
    private String pswd; // don't forget to crypt it !

    public Credentials(String login, String pswd) {
        this.login = login;
        this.pswd = pswd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "login='" + login + '\'' +
                ", pswd='" + pswd + '\'' +
                '}';
    }
}
