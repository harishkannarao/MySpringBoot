package com.harishkannarao.jdbc.security;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Subject {
    private String id;
    private List<String> roles;

    public Subject() {
    }

    public Subject(String id, List<String> roles) {
        this.id = id;
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public List<String> getRoles() {
        return Optional.ofNullable(roles).orElse(Collections.emptyList());
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
