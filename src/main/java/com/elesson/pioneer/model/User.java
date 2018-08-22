package com.elesson.pioneer.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User extends Entity {
    private String name;
    private String email;
    private String password;
    private Role role;

    public User() {
    }

    public User(ResultSet rs) throws SQLException {
        this(rs.getInt("u.uid"),
                rs.getString("u.name"),
                rs.getString("u.email"),
                rs.getString("u.password"),
                User.Role.valueOf(rs.getString("u.role")));
    }

    public User(Integer id, String name, String email, String password, Role role) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String name, String email, String password, Role role) {
        this(null, name, email, password, role);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public enum Role {
        CLIENT,
        ADMIN
    }
}

