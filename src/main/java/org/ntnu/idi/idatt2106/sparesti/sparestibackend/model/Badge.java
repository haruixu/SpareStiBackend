package org.ntnu.idi.idatt2106.sparesti.sparestibackend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Badge entity used to represent a badge that user's can get after completing
 * a given amount of challenges
 *
 * @author Yasin M.
 * @version 1.0
 * @since 18.4.24
 */
@Entity
@Data
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String description;
}
