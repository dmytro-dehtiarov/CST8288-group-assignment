package cst8288.group1.ckc.dataaccess.user;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.user.User;
import java.util.Optional;

/**
 * Implementation for the User DAO interface methods.
 * 
 * @author Sam Corbett
 */
public class UserDaoImpl implements UserDao {
    
    public UserDaoImpl() {}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int add(User user) throws DaoException {
        throw new UnsupportedOperationException("UserDaoImpl.add is not yet implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(int userId) throws DaoException {
        throw new UnsupportedOperationException("UserDaoImpl.findById is not yet implemented");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        throw new UnsupportedOperationException("UserDaoImpl.findByEmail is not yet implemented");
    }
}
