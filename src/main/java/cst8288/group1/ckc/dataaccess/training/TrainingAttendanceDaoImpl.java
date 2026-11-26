package cst8288.group1.ckc.dataaccess.training;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.enums.EquipmentCategory;
import cst8288.group1.ckc.transferobjects.training.TrainingAttendance;

import java.util.List;

/**
 * JDBC implementation of {@link TrainingAttendanceDao}.
 *
 * <p>TEMPORARY: every method throws until the implementation lands in #17.</p>
 *
 * @author Dmytro Dehtiarov
 */
public class TrainingAttendanceDaoImpl implements TrainingAttendanceDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(TrainingAttendance attendance) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TrainingAttendance> findByTraining(int trainingId) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAttended(int userId, EquipmentCategory category) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
