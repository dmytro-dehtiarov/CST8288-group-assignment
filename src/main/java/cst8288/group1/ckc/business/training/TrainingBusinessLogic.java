package cst8288.group1.ckc.business.training;

import cst8288.group1.ckc.business.ledger.LedgerBusinessLogic;
import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.training.TrainingAttendanceDao;
import cst8288.group1.ckc.dataaccess.training.TrainingSessionDao;
import cst8288.group1.ckc.transferobjects.enums.EquipmentCategory;

/**
 * Training sessions, attendance and the qualifications they give members.
 *
 * @author Dmytro Dehtiarov
 */
public class TrainingBusinessLogic {

    private final TrainingSessionDao sessionDao;
    private final TrainingAttendanceDao attendanceDao;
    private final LedgerBusinessLogic ledger;

    /**
     * Creates the training logic.
     *
     * @param sessionDao storage for training sessions
     * @param attendanceDao storage for attendance
     * @param ledger the ledger that credits trainers
     */
    public TrainingBusinessLogic(TrainingSessionDao sessionDao, TrainingAttendanceDao attendanceDao,
                                 LedgerBusinessLogic ledger) {
        this.sessionDao = sessionDao;
        this.attendanceDao = attendanceDao;
        this.ledger = ledger;
    }

    /**
     * Tells whether a member may use equipment of a category: true when the member attended a
     * training session for that category.
     *
     * @param userId the member
     * @param category the equipment category
     * @return {@code true} if the member is qualified
     * @throws DaoException if reading attendance fails
     */
    public boolean isQualified(int userId, EquipmentCategory category) throws DaoException {
        return true; // TEMPORARY: everyone is qualified until training is implemented (#18)
    }
}