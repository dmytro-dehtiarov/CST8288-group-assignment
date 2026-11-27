package cst8288.group1.ckc.dataaccess.training;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.training.TrainingSession;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access for the {@code training_sessions} table.
 *
 * @author Dmytro Dehtiarov
 */
public interface TrainingSessionDao {

    /**
     * Saves a new session; its {@code trainingId} is ignored.
     *
     * @param session the session to save
     * @return the saved session with its generated id
     * @throws DaoException if the insert fails
     */
    TrainingSession add(TrainingSession session) throws DaoException;

    /**
     * Returns one session.
     *
     * @param trainingId the id
     * @return the session, or {@code null} when there is none with that id
     * @throws DaoException if the query fails
     */
    TrainingSession findById(int trainingId) throws DaoException;

    /**
     * Returns the sessions a trainer delivers, earliest first.
     *
     * @param trainerId the trainer
     * @return the sessions; empty when there are none
     * @throws DaoException if the query fails
     */
    List<TrainingSession> findByTrainer(int trainerId) throws DaoException;

    /**
     * Returns the sessions scheduled at or after a moment, earliest first.
     *
     * @param from the earliest start time to include
     * @return the sessions; empty when there are none
     * @throws DaoException if the query fails
     */
    List<TrainingSession> findUpcoming(LocalDateTime from) throws DaoException;
}