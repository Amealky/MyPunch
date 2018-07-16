package com.esgi.mypunch.data.dtos;

public class CandidateUser {
    private String email;
    private String firstname;
    private String lastname;
    private String password;

    public CandidateUser(String email, String firstname, String lastname, String pswd) {
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = pswd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CandidateUser{" +
                "email='" + email + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
