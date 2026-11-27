/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cst8288.group1.ckc.business.user;

import cst8288.group1.ckc.business.ValidationException;
import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.user.UserDao;
import cst8288.group1.ckc.dataaccess.user.UserDaoImpl;
import cst8288.group1.ckc.transferobjects.enums.Role;
import cst8288.group1.ckc.transferobjects.user.User;
import java.util.Optional;
/**
 * Business logic component for user registration and account lookup operations.
 *
 * @author Sam Corbett
 */
public class UserBusinessLogic {

    private final UserDao userDao;
    private final UserFactory userFactory;

    /**
     * Default constructor connecting {@link UserDaoImpl} with {@link UserFactory}.
     */
    public UserBusinessLogic() {
        this(new UserDaoImpl(), new UserFactory());
    }

    /**
     * Constructs {@code UserBusinessLogic} with explicit dependencies (useful for testing).
     *
     * @param userDao     data access implementation for users
     * @param userFactory factory for creating user instances
     */
    public UserBusinessLogic(UserDao userDao, UserFactory userFactory) {
        if (userDao == null) {
            throw new IllegalArgumentException("UserDao cannot be null");
        }
        if (userFactory == null) {
            throw new IllegalArgumentException("UserFactory cannot be null");
        }
        this.userDao = userDao;
        this.userFactory = userFactory;
    }

    /**
     * Registers a new user after validating fields and ensuring email uniqueness.
     *
     * @param name         the user's name
     * @param email        the user's email address
     * @param passwordHash the hashed password
     * @param role         the assigned user role
     * @return the newly created {@link User} with its generated {@code userId} populated
     * @throws ValidationException if inputs are invalid or the email is already in use
     * @throws DaoException        if a database error occurs during lookup or insertion
     */
    public User register(String name, String email, String passwordHash, Role role)
            throws ValidationException, DaoException {
        String normalizedName = requireNonBlank(name, "Name is required");
        String normalizedEmail = requireNonBlank(email, "Email is required").trim().toLowerCase();
        String normalizedPassword = requireNonBlank(passwordHash, "Password is required");

        if (!normalizedEmail.contains("@")) {
            throw new ValidationException("Please provide a valid email address");
        }
        if (role == null) {
            throw new ValidationException("Role is required");
        }
        if (userDao.findByEmail(normalizedEmail).isPresent()) {
            throw new ValidationException("An account with that email already exists");
        }

        User user = userFactory.createUser(role, normalizedName.trim(), normalizedEmail, normalizedPassword);
        int generatedId = userDao.add(user);
        user.setUserId(generatedId);
        return user;
    }

    /**
     * Retrieves a user via ID.
     *
     * @param userId the user ID in search for
     * @return an {@link Optional} containing the matching {@link User}, or empty if not found
     * @throws ValidationException if {@code userId} is not positive
     * @throws DaoException        upon a database error
     */
    public Optional<User> findById(int userId) throws ValidationException, DaoException {
        if (userId <= 0) {
            throw new ValidationException("User ID must be positive");
        }
        return userDao.findById(userId);
    }

    /**
     * Retrieves a user via email address.
     *
     * @param email the email address in search for
     * @return an {@link Optional} containing the matching {@link User}, or empty if not found
     * @throws ValidationException if {@code email} is null or blank
     * @throws DaoException        upon a database error
     */
    public Optional<User> findByEmail(String email) throws ValidationException, DaoException {
        String normalizedEmail = requireNonBlank(email, "Email is required").trim().toLowerCase();
        return userDao.findByEmail(normalizedEmail);
    }

    /**
     * The exception for requiring a non-blank data field.
     * 
     * @param value     the type of data in a field
     * @param message   the exception message
     * @return The exception depending on the conditions
     * @throws ValidationException if data field is blank
     */
    private String requireNonBlank(String value, String message) throws ValidationException {
        if (value == null || value.isBlank()) {
            throw new ValidationException(message);
        }
        return value;
    }
}
