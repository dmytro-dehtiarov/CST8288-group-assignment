package cst8288.group1.ckc.dataaccess.training;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.enums.EquipmentCategory;
import cst8288.group1.ckc.transferobjects.training.TrainingAttendance;
import java.util.List;

/**
 * Data access for the {@code training_attendance} table.
 *
 * @author Dmytro Dehtiarov
 */
public interface TrainingAttendanceDao {

    /**
     * Records that a member attended a session.
     *
     * @param attendance the session and the member
     * @throws DaoException if the insert fails, including when this attendance already exists
     */
    void add(TrainingAttendance attendance) throws DaoException;

    /**
     * Returns everyone who attended a session.
     *
     * @param trainingId the session
     * @return the attendance records; empty when nobody attended
     * @throws DaoException if the query fails
     */
    List<TrainingAttendance> findByTraining(int trainingId) throws DaoException;

    /**
     * Tells whether a member attended any session for an equipment category.
     *
     * @param userId the member
     * @param category the equipment category
     * @return {@code true} if the member attended at least one such session
     * @throws DaoException if the query fails
     */
    boolean hasAttended(int userId, EquipmentCategory category) throws DaoException;
}