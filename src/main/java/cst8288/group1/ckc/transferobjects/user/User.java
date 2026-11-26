package cst8288.group1.ckc.transferobjects.user;

import cst8288.group1.ckc.transferobjects.enums.Role;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for a CKC user account.
 * 
 * @author Sam Corbett
 */
public class User {
    private Integer userId;
    private String name;
    private String email;
    private String passwordHash;
    private Role role;
    private LocalDateTime createdAt;
    
    /**
     * Default no-arg constructor initializes the role type ({@link Role#MEMBER}).
     */
    public User() {
        this.role = Role.MEMBER;
    }
    
    /**
     * Parameterized constructor makes the new {@code User},
     * without ID or timestamp.
     * 
     * @param name          the user's name
     * @param email         the user's email
     * @param passwordHash  the hashed password
     * @param role          the user's role
     */
    public User(String name, String email, String passwordHash, Role role) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = (role != null) ? role : Role.MEMBER;
    }
    
    /**
     * Parameterized constructor makes fully populated {@code User},
     * ID and timestamp included.
     * 
     * @param userId        the user's ID
     * @param name          the user's name
     * @param email         the user's email
     * @param passwordHash  the hashed password
     * @param role          the user's role
     * @param createdAt     account creation timestamp
     */
    public User(Integer userId, String name, String email, String passwordHash, Role role, LocalDateTime createdAt) {
        this(name, email, passwordHash, role); // reused data
        this.userId = userId;
        this.createdAt = createdAt;
    }

    /**
     * This gets the user's ID
     * @return 
     */
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    
}
