package cst8288.group1.ckc.transferobjects.training;

import cst8288.group1.ckc.transferobjects.enums.EquipmentCategory;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A safety training session a trainer delivers. Attending it qualifies a member for one
 * equipment category. Mirrors the {@code training_sessions} table.
 *
 * @param trainingId the database id; 0 before the session is saved
 * @param trainerId the user who delivers the session
 * @param title a short title, for example "Oven safety"
 * @param equipmentCategory the equipment the session qualifies members for
 * @param scheduledAt when the session takes place
 * @param durationMinutes how long the session lasts
 * @author Dmytro Dehtiarov
 */
public record TrainingSession(
        int trainingId,
        int trainerId,
        String title,
        EquipmentCategory equipmentCategory,
        LocalDateTime scheduledAt,
        int durationMinutes
) {

    /**
     * Creates a session with every field, as the DAO does for a row read from the database.
     *
     * @throws NullPointerException if {@code title}, {@code equipmentCategory} or {@code scheduledAt} is null
     */
    public TrainingSession {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(equipmentCategory, "equipmentCategory");
        Objects.requireNonNull(scheduledAt, "scheduledAt");
    }

    /**
     * Creates a new session that is not saved yet; the database sets its id.
     *
     * @param trainerId the user who delivers the session
     * @param title a short title
     * @param equipmentCategory the equipment the session qualifies members for
     * @param scheduledAt when the session takes place
     * @param durationMinutes how long the session lasts
     */
    public TrainingSession(
            int trainerId,
            String title,
            EquipmentCategory equipmentCategory,
            LocalDateTime scheduledAt,
            int durationMinutes
    ) {
        this(0, trainerId, title, equipmentCategory, scheduledAt, durationMinutes);
    }
}