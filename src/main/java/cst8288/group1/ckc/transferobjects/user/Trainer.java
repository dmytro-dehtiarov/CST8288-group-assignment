package cst8288.group1.ckc.transferobjects.user;

import cst8288.group1.ckc.transferobjects.enums.Role;
import java.time.LocalDateTime;

/**
 * Data Transfer Object representing a CKC Trainer.
 * <p>
 * A <strong>trainer</strong> is a type of {@link User} qualified for delivering safety
 * training sessions and earn training credits.
 * </p>
 * 
 * @author Sam Corbett
 */
public class Trainer extends User {
    /**
     * Default constructor sets the user role to {@link Role#TRAINER}
     */
    public Trainer() {
        super();
        setRole(Role.TRAINER);
    }
    
    /**
     * Parameterized constructor makes a new {@link Role#TRAINER},
     * excluding ID and timestamp.
     * 
     * @param name         the trainer's name
     * @param email        the trainer's email
     * @param passwordHash the password hash
     */
    public Trainer(String name, String email, String passwordHash) {
        super(name, email, passwordHash, Role.TRAINER);
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
    public Trainer(Integer userId, String name, String email, String passwordHash, LocalDateTime createdAt) {
        super(name, email, passwordHash, Role.TRAINER);
    }
    
    /**
     * This verifies if a trainer can schedule the training session.
     * @return true if they can, false otherwise
     */
    @Override
    public boolean canScheduleTraining() {
        return true;
    }
}
