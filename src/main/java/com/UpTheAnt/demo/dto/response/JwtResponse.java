package com.uptheant.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String name;
    private String username;
    private String email;
    private String role;
    
    public JwtResponse(String token, Integer id, String name, String username, String email, String role) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.role = role;
    }
}
    
