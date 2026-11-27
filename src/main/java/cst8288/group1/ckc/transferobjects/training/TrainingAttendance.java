package cst8288.group1.ckc.transferobjects.training;

/**
 * A member's attendance at one training session. Mirrors the {@code training_attendance} table.
 *
 * @param trainingId the session
 * @param userId the member who attended
 * @author Dmytro Dehtiarov
 */
public record TrainingAttendance(
        int trainingId,
        int userId
) {}