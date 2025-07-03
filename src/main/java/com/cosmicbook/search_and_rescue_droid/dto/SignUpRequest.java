package com.cosmicbook.search_and_rescue_droid.dto;

import com.cosmicbook.search_and_rescue_droid.model.Role;

import java.util.Set;

public class SignUpRequest {

    private String username;
    private String email;
    private String password;
    private Set<String> role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassWord(String password) {
        this.password = password;
    }

    public String getPassword(){
        return password;
    }

    public Set<String> getRole() {
        return role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

}
