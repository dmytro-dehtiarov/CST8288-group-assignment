package cst8288.group1.ckc.business.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;

/**
 * Observer that takes a piece of equipment out of service when a sensor reports a fault.
 *
 * <p>For a {@code DIAGNOSTIC} alert (a sensor reports a fault) it sets the equipment's status to
 * {@code DOWN_FOR_MAINTENANCE}, so members cannot check it in until a Kitchen-Tech completes the
 * task; a repeated alert changes nothing. A {@code WEAR} alert is only a warning and is ignored, so
 * the equipment stays available until a Kitchen-Tech schedules and completes the task.</p>
 *
 * <p>Depends on: equipment module, an operation to update the status of a piece of
 * equipment.</p>
 *
 * @author Othman Slassi
 */
public class EquipmentStatusUpdater implements MaintenanceObserver {

    /**
     * Creates the updater.
     */
    public EquipmentStatusUpdater() {
    }

    /**
     * Sets the status of the alert's equipment to {@code DOWN_FOR_MAINTENANCE} when the alert is a
     * {@code DIAGNOSTIC} alert.
     *
     * <p>For a {@code DIAGNOSTIC} alert (a sensor reports a fault) the equipment is taken out of
     * service, so members cannot check it in until a Kitchen-Tech completes the task; a repeated
     * alert changes nothing because the equipment is already down for maintenance. A {@code WEAR}
     * alert is only a warning and is ignored, so the equipment stays available until a
     * Kitchen-Tech schedules and completes the task. Not implemented yet.</p>
     *
     * <p>Depends on: equipment module, an operation to update the status of a piece of equipment
     * by its {@code equipment_id}.</p>
     *
     * @param alert the alert raised by the monitor
     * @throws DaoException if the status cannot be saved
     */
    @Override
    public void onMaintenanceNeeded(MaintenanceAlert alert) throws DaoException {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
