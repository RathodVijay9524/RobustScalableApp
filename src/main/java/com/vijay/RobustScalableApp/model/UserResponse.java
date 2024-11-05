package com.vijay.RobustScalableApp.model;


import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String username;
    private String email;
   // private Set<Role> roles;
}
