package cst8288.group1.ckc.dataaccess.maintenance;

import cst8288.group1.ckc.dataaccess.DaoException;
import cst8288.group1.ckc.dataaccess.DataSource;
import cst8288.group1.ckc.transferobjects.enums.AlertReason;
import cst8288.group1.ckc.transferobjects.maintenance.MaintenanceAlert;
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
 * JDBC implementation of {@link MaintenanceAlertDao} for MySQL.
 *
 * <p>Every method gets the shared connection from
 * {@link cst8288.group1.ckc.dataaccess.DataSource#getConnection()} and must not close it; it closes
 * only its own {@code PreparedStatement} and {@code ResultSet}. Rows are mapped column by column to
 * {@link MaintenanceAlert}: {@code reason} with {@link AlertReason#valueOf(String)}, and SQL
 * {@code NULL} becoming {@code null} for {@code details}, {@code resolved_at} and
 * {@code task_id}. The generated column {@code open_alert_key} is never read or written. Every
 * {@link java.sql.SQLException} is wrapped in a {@link DaoException}.</p>
 *
 * @author Othman Slassi
 */
public class MaintenanceAlertDaoImpl implements MaintenanceAlertDao {

    private static final String COLUMNS = "alert_id, equipment_id, reason, details, created_at, "
            + "resolved_at, task_id";

    /**
     * Creates the DAO; it keeps no state of its own.
     */
    public MaintenanceAlertDaoImpl() {
    }

    /**
     * Finds one alert by its id.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_alerts WHERE alert_id = ?} and maps the row, if
     * any.</p>
     *
     * @param alertId the {@code alert_id} to look for
     * @return the alert, or {@code null} if no alert has that id
     * @throws DaoException if the query fails
     */
    @Override
    public MaintenanceAlert findById(int alertId) throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_alerts WHERE alert_id = ?";
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, alertId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapRow(rs) : null;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findById failed", e);
        }
    }

    /**
     * Returns the alerts that are not resolved yet.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_alerts WHERE resolved_at IS NULL ORDER BY
     * created_at DESC, alert_id DESC}.</p>
     *
     * @return the open alerts, newest first; an empty list if there are none
     * @throws DaoException if the query fails
     */
    @Override
    public List<MaintenanceAlert> findOpen() throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_alerts WHERE resolved_at IS NULL "
                + "ORDER BY created_at DESC, alert_id DESC";
        List<MaintenanceAlert> alerts = new ArrayList<>();
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alerts.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findOpen failed", e);
        }
        return alerts;
    }

    /**
     * Returns the open alerts of one piece of equipment.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_alerts WHERE equipment_id = ? AND resolved_at IS
     * NULL ORDER BY created_at DESC, alert_id DESC}.</p>
     *
     * @param equipmentId the {@code equipment_id}
     * @return the open alerts of that equipment, newest first; an empty list if there are none
     * @throws DaoException if the query fails
     */
    @Override
    public List<MaintenanceAlert> findOpenByEquipment(int equipmentId) throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_alerts WHERE equipment_id = ? "
                + "AND resolved_at IS NULL ORDER BY created_at DESC, alert_id DESC";
        List<MaintenanceAlert> alerts = new ArrayList<>();
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, equipmentId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        alerts.add(mapRow(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findOpenByEquipment failed", e);
        }
        return alerts;
    }

    /**
     * Finds the open alert of one piece of equipment for one reason.
     *
     * <p>Runs {@code SELECT ... FROM maintenance_alerts WHERE equipment_id = ? AND reason = ? AND
     * resolved_at IS NULL}, binding {@code reason.name()}. The unique key on open alerts guarantees
     * at most one row.</p>
     *
     * @param equipmentId the {@code equipment_id}
     * @param reason the reason to look for, not {@code null}
     * @return the open alert, or {@code null} if there is none
     * @throws DaoException if the query fails
     */
    @Override
    public MaintenanceAlert findOpenByEquipmentAndReason(int equipmentId, AlertReason reason)
            throws DaoException {
        String sql = "SELECT " + COLUMNS + " FROM maintenance_alerts WHERE equipment_id = ? "
                + "AND reason = ? AND resolved_at IS NULL";
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, equipmentId);
                ps.setString(2, reason.name());
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() ? mapRow(rs) : null;
                }
            }
        } catch (SQLException e) {
            throw new DaoException("findOpenByEquipmentAndReason failed", e);
        }
    }

    /**
     * Inserts a new alert.
     *
     * <p>Inserts {@code equipment_id}, {@code reason}, {@code details}, {@code created_at},
     * {@code resolved_at} and {@code task_id}, writing SQL {@code NULL} for the {@code null}
     * fields, and reads the generated {@code alert_id} with
     * {@code Statement.RETURN_GENERATED_KEYS}. The given object is not changed.</p>
     *
     * @param alert the alert to save; {@code createdAt} must not be {@code null}
     * @return the new {@code alert_id}
     * @throws DaoException if the insert fails, for example on the unique key of open alerts
     */
    @Override
    public int add(MaintenanceAlert alert) throws DaoException {
        String sql = "INSERT INTO maintenance_alerts (equipment_id, reason, details, created_at, "
                + "resolved_at, task_id) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, alert.getEquipmentId());
                if (alert.getReason() == null) {
                    ps.setNull(2, Types.VARCHAR);
                } else {
                    ps.setString(2, alert.getReason().name());
                }
                ps.setString(3, alert.getDetails());
                ps.setObject(4, alert.getCreatedAt());
                ps.setObject(5, alert.getResolvedAt());
                if (alert.getTaskId() == null) {
                    ps.setNull(6, Types.INTEGER);
                } else {
                    ps.setInt(6, alert.getTaskId());
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
        throw new DaoException("add failed: no alert_id was generated");
    }

    /**
     * Links an alert to the maintenance task that handles it.
     *
     * <p>Runs {@code UPDATE maintenance_alerts SET task_id = ? WHERE alert_id = ?}.</p>
     *
     * @param alertId the {@code alert_id} to change
     * @param taskId the {@code task_id} to store in the alert
     * @throws DaoException if the update fails, changes no row, or the task does not exist
     */
    @Override
    public void linkTask(int alertId, int taskId) throws DaoException {
        String sql = "UPDATE maintenance_alerts SET task_id = ? WHERE alert_id = ?";
        int rows;
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, taskId);
                ps.setInt(2, alertId);
                rows = ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("linkTask failed", e);
        }
        if (rows == 0) {
            throw new DaoException("linkTask failed: no alert with id " + alertId);
        }
    }

    /**
     * Marks an open alert as resolved.
     *
     * <p>Runs {@code UPDATE maintenance_alerts SET resolved_at = ? WHERE alert_id = ? AND
     * resolved_at IS NULL}, so a resolved alert keeps its first resolution time.</p>
     *
     * @param alertId the {@code alert_id} to resolve
     * @param resolvedAt the time to store in {@code resolved_at}
     * @throws DaoException if the update fails
     */
    @Override
    public void resolve(int alertId, LocalDateTime resolvedAt) throws DaoException {
        String sql = "UPDATE maintenance_alerts SET resolved_at = ? "
                + "WHERE alert_id = ? AND resolved_at IS NULL";
        try {
            Connection conn = DataSource.INSTANCE.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, resolvedAt);
                ps.setInt(2, alertId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DaoException("resolve failed", e);
        }
    }

    /**
     * Maps the current row of a result set to an alert.
     *
     * @param rs a result set positioned on a row that has every column of {@link #COLUMNS}
     * @return the alert of that row
     * @throws SQLException if a column cannot be read
     */
    private static MaintenanceAlert mapRow(ResultSet rs) throws SQLException {
        MaintenanceAlert alert = new MaintenanceAlert();
        alert.setAlertId(rs.getInt("alert_id"));
        alert.setEquipmentId(rs.getInt("equipment_id"));
        alert.setReason(AlertReason.valueOf(rs.getString("reason")));
        alert.setDetails(rs.getString("details"));
        alert.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        alert.setResolvedAt(rs.getObject("resolved_at", LocalDateTime.class));
        alert.setTaskId(rs.getObject("task_id", Integer.class));
        return alert;
    }
}
