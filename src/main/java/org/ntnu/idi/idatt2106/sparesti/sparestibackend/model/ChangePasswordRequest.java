package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Represents a request to change a user's password. This entity stores the unique identifiers
 * and timestamp of the password change request to manage and validate user requests securely.
 *
 * @author L.M.L Nilsen and H.L XU
 */
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class ChangePasswordRequest {

    /**
     * Unique identifier for the password change request. This is typically a token or string that can be used to validate the request.
     * It must be non-null and is marked as unique to ensure no duplicate requests are present.
     */
    @NotNull
    @Column(unique = true, nullable = false)
    private String id;

    /**
     * The time when the password change request was created. This is automatically set to the current timestamp when the request is persisted.
     * This timestamp is used to validate the request's validity period.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime time;

    /**
     * The user ID associated with this password change request. This field serves as the primary key of the entity and is unique to each request.
     * It links the request specifically to a user in the system.
     */
    @Id
    @NotNull
    @Column(unique = true, nullable = false)
    private Long userID;
}
