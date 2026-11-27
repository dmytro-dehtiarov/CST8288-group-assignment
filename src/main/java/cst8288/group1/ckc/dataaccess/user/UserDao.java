import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.user.User;

import java.util.Optional;
/**
 * Data access object for the {@code users} table.
 * 
 * DAO Interface for methods used in UserDAOImpl
 * 
 * @author Sam Corbett
 */
public interface UserDao {
    /**
     * Add new user record to database, returns generated primary key.
     * 
     * @param user              the inserted user entity
     * @return                  a newly generated {@code userId}
     * @throws DaoException     upon a database error.
     */
    int add(User user) throws DaoException;
    
    /**
     * Find the user via primary key (their ID).
     * 
     * @param userId            the user's ID in search for  
     * @return                  an {@code Optional} of the matching {@link User}, becomes empty otherwise.
     * @throws DaoException     upon a database error.
     */
    Optional<User> findById(int userId) throws DaoException;
    
    /**
     * Find the user via primary key (their ID).
     * 
     * @param email             the user's email address
     * @return                  an {@code Optional} of the matching {@link User}, becomes empty otherwise.
     * @throws DaoException     upon a database error.
     */
    Optional<User> findByEmail(String email) throws DaoException;
}
