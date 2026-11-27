package cst8288.group1.ckc.dataaccess.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.DataSource;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceTask;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of {@link MaintenanceTaskDao} for MySQL.
 *
 * <p>Every method gets the shared connection from
 * {@link cst8288.group1.ckc.dataaccess.DataSource#getConnection()} and must not close it; it closes
 * only its own {@code PreparedStatement} and {@code ResultSet}. Rows are mapped column by column to
 * {@link MaintenanceTask}, with SQL {@code NULL} becoming {@code null} for {@code tech_id},
 * {@code completed_at} and {@code minutes_logged}. Every {@link java.sql.SQLException} is wrapped
 * in a {@link DaoException}.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceTaskDaoImpl implements MaintenanceTaskDao {

    private static final String COLUMNS = "task_id, equipment_id, tech_id, description, "
            + "scheduled_for, completed_at, minutes_logged";

    /**
     * Creates the DAO; it keeps no state of its own.
     */
    public MaintenanceTaskDaoImpl() {
    }

    /**
     * Finds one task by its id.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_tasks WHERE task_id = ?} and maps the row, if
     * any.</p>
     *
     * @param taskId the {@code task_id} to look for
     * @return the task, or {@code null} if no task has that id
     * @throws DaoException if the query fails
     */
    @Override
    public MaintenanceTask findById(int taskId) throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_tasks WHERE task_id = ?";
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, taskId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapRow(rs) : null;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findById failed", e);
        }
    }

    /**
     * Returns every task, open and completed.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_tasks ORDER BY scheduled_for, task_id}.</p>
     *
     * @return all tasks; an empty list if there are none
     * @throws DaoException if the query fails
     */
    @Override
    public List<MaintenanceTask> findAll() throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_tasks ORDER BY scheduled_for, task_id";
        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findAll failed", e);
        }
        return tasks;
    }

    /**
     * Returns the tasks that are not completed yet.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_tasks WHERE completed_at IS NULL ORDER BY
     * scheduled_for, task_id}.</p>
     *
     * @return the open tasks; an empty list if there are none
     * @throws DaoException if the query fails
     */
    @Override
    public List<MaintenanceTask> findOpen() throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_tasks WHERE completed_at IS NULL "
                + "ORDER BY scheduled_for, task_id";
        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findOpen failed", e);
        }
        return tasks;
    }

    /**
     * Returns the tasks taken by one Kitchen-Tech, open and completed.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_tasks WHERE tech_id = ? ORDER BY scheduled_for,
     * task_id}.</p>
     *
     * @param techId the {@code user_id} of the Kitchen-Tech
     * @return the tasks of that Kitchen-Tech; an empty list if there are none
     * @throws DaoException if the query fails
     */
    @Override
    public List<MaintenanceTask> findByTech(int techId) throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_tasks WHERE tech_id = ? "
                + "ORDER BY scheduled_for, task_id";
        List<MaintenanceTask> tasks = new ArrayList<>();
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, techId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        tasks.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findByTech failed", e);
        }
        return tasks;
    }

    /**
     * Inserts a new task.
     *
     * <p>Inserts {@code equipment_id}, {@code tech_id}, {@code description}, {@code scheduled_for},
     * {@code completed_at} and {@code minutes_logged}, writing SQL {@code NULL} for the
     * {@code null} fields, and reads the generated {@code task_id} with
     * {@code Statement.RETURN_GENERATED_KEYS}. The given object is not changed.</p>
     *
     * @param task the task to save
     * @return the new {@code task_id}
     * @throws DaoException if the insert fails or no key is generated
     */
    @Override
    public int add(MaintenanceTask task) throws DaoException {
        String sql = "INSERT INTO maintenance_tasks (equipment_id, tech_id, description, "
                + "scheduled_for, completed_at, minutes_logged) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, task.getEquipmentId());
                if (task.getTechId() == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, task.getTechId());
                }
                ps.setString(3, task.getDescription());
                ps.setObject(4, task.getScheduledFor());
                ps.setObject(5, task.getCompletedAt());
                if (task.getMinutesLogged() == null) {
                    ps.setNull(6, Types.INTEGER);
                } else {
                    ps.setInt(6, task.getMinutesLogged());
                }
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("add failed", e);
        }
        throw new DaoException("add failed: no task_id was generated");
    }

    /**
     * Saves every column of an existing task except its id.
     *
     * <p>Runs {@code UPDATE maintenance_tasks SET equipment_id = ?, tech_id = ?, description = ?,
     * scheduled_for = ?, completed_at = ?, minutes_logged = ? WHERE task_id = ?}.</p>
     *
     * @param task the task with its new values
     * @throws DaoException if the update fails or changes no row
     */
    @Override
    public void update(MaintenanceTask task) throws DaoException {
        String sql = "UPDATE maintenance_tasks SET equipment_id = ?, tech_id = ?, description = ?, "
                + "scheduled_for = ?, completed_at = ?, minutes_logged = ? WHERE task_id = ?";
        int rows;
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, task.getEquipmentId());
                if (task.getTechId() == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, task.getTechId());
                }
                ps.setString(3, task.getDescription());
                ps.setObject(4, task.getScheduledFor());
                ps.setObject(5, task.getCompletedAt());
                if (task.getMinutesLogged() == null) {
                    ps.setNull(6, Types.INTEGER);
                } else {
                    ps.setInt(6, task.getMinutesLogged());
                }
                ps.setInt(7, task.getTaskId());
                rows = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("update failed", e);
        }
        if (rows == 0) {
            throw new DaoException("update failed: no task with id " + task.getTaskId());
        }
    }

    /**
     * Maps the current row of a result set to a task.
     *
     * @param rs a result set positioned on a row that has every column of {@link #COLUMNS}
     * @return the task of that row
     * @throws SQLException if a column cannot be read
     */
    private static MaintenanceTask mapRow(ResultSet rs) throws SQLException {
        MaintenanceTask task = new MaintenanceTask();
        task.setTaskId(rs.getInt("task_id"));
        task.setEquipmentId(rs.getInt("equipment_id"));
        task.setTechId(rs.getObject("tech_id", Integer.class));
        task.setDescription(rs.getString("description"));
        task.setScheduledFor(rs.getObject("scheduled_for", LocalDateTime.class));
        task.setCompletedAt(rs.getObject("completed_at", LocalDateTime.class));
        task.setMinutesLogged(rs.getObject("minutes_logged", Integer.class));
        return task;
    }
}
