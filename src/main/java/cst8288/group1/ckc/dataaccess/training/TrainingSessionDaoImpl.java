package cst8288.group1.ckc.dataaccess.training;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.training.TrainingSession;

import java.time.LocalDateTime;
import java.util.List;

/**
 * JDBC implementation of {@link TrainingSessionDao}.
 *
 * <p>TEMPORARY: every method throws until the implementation lands in #14.</p>
 *
 * @author Dmytro Dehtiarov
 */
public class TrainingSessionDaoImpl implements TrainingSessionDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingSession add(TrainingSession session) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingSession findById(int trainingId) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingSession> findByTrainer(int trainerId) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingSession> findUpcoming(LocalDateTime from) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
