package cst8288.group1.ckc.transferobjects.user;

import cst8288.group1.ckc.transferobjects.enums.Role;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for a generic CKC user account.
 * 
 * Made class Serializable to store user session instances. It's also
 * the base for {@code Trainer} and {@code KitchenTech} classes.
 * 
 * @author Sam Corbett
 */
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
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
     * excluding ID or timestamp.
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
     * @return the user ID
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * This sets the user's ID
     * @param userId the user ID
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * This gets the user's name
     * @return the user name
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the user's name
     * @param name the user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This gets the user's email
     * @return the user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This sets the user's email
     * @param email the user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This gets the password hash
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * This sets the password hash
     * @param passwordHash the password hash
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * This gets the user's role
     * @return the user role
     */
    public Role getRole() {
        return role;
    }

    /**
     * This sets the user's role
     * @param role the user role
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * This gets the user's timestamp of creation
     * @return the user's timestamp of creation
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * This gets the user's timestamp of creation
     * @param createdAt the user's timestamp of creation
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    /**
     * This verifies if a generic user can schedule the training session.
     * @return false, but true if they are a trainer.
     */
    public boolean canScheduleTraining() {
        return false;
    }
    
    /**
     * This verifies if a generic user can register the kitchen equipment.
     * @return false, but true if they are a technician.
     */
    public boolean canRegisterEquipment() {
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return Objects.equals(userId, other.userId) && Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{"
                + "userId=" + userId
                + ", name='" + name + '\''
                + ", email='" + email + '\''
                + ", role=" + role
                + ", createdAt=" + createdAt
                + '}';
    }
}
