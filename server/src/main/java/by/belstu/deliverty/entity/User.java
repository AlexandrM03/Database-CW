package by.belstu.deliverty.entity;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String telephone;
    private Long userRoleId;
}
