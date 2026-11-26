package cst8288.group1.ckc.business.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.maintenance.MaintenanceAlertDao;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
import java.util.Objects;

/**
 * Observer that saves each new maintenance alert in the database.
 *
 * <p>The table allows only one open alert per equipment and reason, and the same equipment is
 * checked at every check-out, so the same wear or fault is usually reported many times before a
 * Kitchen-Tech fixes it. This observer therefore saves an alert only when no open alert exists yet
 * for that equipment and reason; otherwise it keeps the existing one and does nothing.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceAlertLog implements MaintenanceObserver {

    private final MaintenanceAlertDao alertDao;

    /**
     * Creates the log on top of an alert DAO.
     *
     * @param alertDao the DAO that stores the alerts; a fake one in unit tests
     * @throws NullPointerException if {@code alertDao} is {@code null}
     */
    public MaintenanceAlertLog(MaintenanceAlertDao alertDao) {
        this.alertDao = Objects.requireNonNull(alertDao, "alertDao");
    }

    /**
     * Saves the alert unless the same equipment already has an open alert for the same reason.
     *
     * <p>When the alert is saved, its {@code alertId} is set to the id the DAO returns, so the
     * observers notified after this one and the caller of the monitor see the saved id. When an
     * open alert already exists, nothing is saved and the given alert is left unchanged.</p>
     *
     * @param alert the alert raised by the monitor
     * @throws DaoException if the lookup or the insert fails
     */
    @Override
    public void onMaintenanceNeeded(MaintenanceAlert alert) throws DaoException {
        MaintenanceAlert open = alertDao.findOpenByEquipmentAndReason(alert.getEquipmentId(),
                alert.getReason());
        if (open == null) {
            alert.setAlertId(alertDao.add(alert));
        }
    }
}
