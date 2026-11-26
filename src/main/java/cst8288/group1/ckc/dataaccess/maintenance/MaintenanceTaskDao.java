package cst8288.group1.ckc.dataaccess.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceTask;
import java.util.List;

/**
 * Data access contract for the {@code maintenance_tasks} table.
 *
 * <p>Business logic depends on this interface, not on the JDBC implementation, so unit tests can
 * pass a fake DAO. Implementations check no business rule: they read and write rows and wrap every
 * {@link java.sql.SQLException} in a {@link DaoException}.</p>
 *
 * @author Othman Slassi
 */
public interface MaintenanceTaskDao {

    /**
     * Finds one task by its id.
     *
     * @param taskId the {@code task_id} to look for
     * @return the task, or {@code null} if no task has that id
     * @throws DaoException if the query fails
     */
    MaintenanceTask findById(int taskId) throws DaoException;

    /**
     * Returns every task, open and completed.
     *
     * <p>Tasks are ordered by {@code scheduled_for}, then by {@code task_id}.</p>
     *
     * @return all tasks; an empty list if there are none
     * @throws DaoException if the query fails
     */
    List<MaintenanceTask> findAll() throws DaoException;

    /**
     * Returns the tasks that are not completed yet.
     *
     * <p>A task is open while {@code completed_at IS NULL}, whether or not a Kitchen-Tech has taken
     * it. Tasks are ordered by {@code scheduled_for}, the earliest first.</p>
     *
     * @return the open tasks; an empty list if there are none
     * @throws DaoException if the query fails
     */
    List<MaintenanceTask> findOpen() throws DaoException;

    /**
     * Returns the tasks taken by one Kitchen-Tech, open and completed.
     *
     * <p>Tasks are ordered by {@code scheduled_for}, the earliest first.</p>
     *
     * @param techId the {@code user_id} of the Kitchen-Tech
     * @return the tasks whose {@code tech_id} equals {@code techId}; an empty list if there are none
     * @throws DaoException if the query fails
     */
    List<MaintenanceTask> findByTech(int techId) throws DaoException;

    /**
     * Inserts a new task.
     *
     * <p>The {@code taskId} of the given object is ignored because the database generates it; the
     * object itself is not changed.</p>
     *
     * @param task the task to save
     * @return the new {@code task_id}
     * @throws DaoException if the insert fails, for example when the equipment does not exist
     */
    int add(MaintenanceTask task) throws DaoException;

    /**
     * Saves every column of an existing task except its id.
     *
     * <p>The row to change is chosen by {@code task.getTaskId()}.</p>
     *
     * @param task the task with its new values
     * @throws DaoException if the update fails or no task has that id
     */
    void update(MaintenanceTask task) throws DaoException;
}
