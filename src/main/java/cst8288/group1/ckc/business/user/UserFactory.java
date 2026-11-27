/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8288.group1.ckc.business.user;

import cst8288.group1.ckc.transferobjects.enums.Role;
import cst8288.group1.ckc.transferobjects.user.KitchenTech;
import cst8288.group1.ckc.transferobjects.user.Trainer;
import cst8288.group1.ckc.transferobjects.user.User;
import java.time.LocalDateTime;

/**
 * Simple Factory for creating {@link User} and its role-specific subclasses
 * ({@link Trainer} and {@link KitchenTech}) from a {@link Role}.
 *
 * @author Sam Corbett
 */
public class UserFactory {

    /**
     * Creates a new user instance prior to database persistence.
     *
     * @param role         the user's role
     * @param name         full name of the user
     * @param email        unique email address
     * @param passwordHash stored password hash
     * @return a {@link User}, {@link Trainer}, or {@link KitchenTech} instance
     */
    public User createUser(Role role, String name, String email, String passwordHash) {
        throw new UnsupportedOperationException("UserFactory.createUser is not yet implemented");
    }

    /**
     * Creates a hydrated user instance from persisted database values.
     *
     * @param role         the user's role
     * @param userId       database primary key
     * @param name         full name of the user
     * @param email        unique email address
     * @param passwordHash stored password hash
     * @param createdAt    account creation timestamp
     * @return a hydrated {@link User}, {@link Trainer}, or {@link KitchenTech} instance
     */
    public User createUser(Role role, Integer userId, String name, String email,
                           String passwordHash, LocalDateTime createdAt) {
        throw new UnsupportedOperationException("UserFactory.createUser is not yet implemented");
    }
}
