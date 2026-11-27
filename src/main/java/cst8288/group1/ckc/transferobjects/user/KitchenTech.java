package cst8288.group1.ckc.transferobjects.user;

import cst8288.group1.ckc.transferobjects.enums.Role;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a CKC technician.
 * <p>
 * A <strong>technician</strong>,<i>or known as KitchenTech role</i>, is a type of 
 * {@link User} qualified for authorizing the registry
 * of equipment and perform/log maintenance tasks for credits.
 * </p>
 * 
 * @author Sam Corbett
 */
public class KitchenTech extends User {
    /**
     * Default constructor sets the user role to {@link Role#TRAINER}
     */
    public KitchenTech() {
        super();
        setRole(Role.KITCHEN_TECH);
    }
    
    /**
     * Parameterized constructor makes a new {@link Role#TRAINER},
     * excluding ID and timestamp.
     * 
     * @param name         the trainer's name
     * @param email        the trainer's email
     * @param passwordHash the password hash
     */
    public KitchenTech(String name, String email, String passwordHash) {
        super(name, email, passwordHash, Role.KITCHEN_TECH);
    }
    /**
     * Parameterized constructor makes fully populated {@link Role#TRAINER},
     * ID and timestamp included.
     * 
     * @param userId        the trainer's ID
     * @param name          the trainer's name
     * @param email         the trainer's email
     * @param passwordHash  the password hash
     * @param createdAt     account creation timestamp
     */
    public KitchenTech(Integer userId, String name, String email, String passwordHash, LocalDateTime createdAt) {
        super(name, email, passwordHash, Role.KITCHEN_TECH);
    }
}
