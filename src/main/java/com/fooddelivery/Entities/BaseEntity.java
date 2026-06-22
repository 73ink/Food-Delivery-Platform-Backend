package com.fooddelivery.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

    // This class contains the common fields required in every entity.
// All entities will extend this class to avoid repeating id, createdDate, updatedDate, and isActive.
    @Getter
    @Setter
    @NoArgsConstructor
    @MappedSuperclass
    public abstract class BaseEntity {

        // Primary key must be Integer according to the project requirements.
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        // Stores the date and time when the record is created.
        @Column(nullable = false, updatable = false)
        private LocalDateTime createdDate;

        // Stores the date and time when the record is updated.
        @Column(nullable = false)
        private LocalDateTime updatedDate;

        // Used for soft delete.
        // true = active record
        // false = deleted/deactivated record
        @Column(nullable = false)
        private Boolean isActive = true;

        // This method runs automatically before inserting a new row.
        @PrePersist
        protected void onCreate() {
            createdDate = LocalDateTime.now();
            updatedDate = LocalDateTime.now();

            if (isActive == null) {
                isActive = true;
            }
        }

        // This method runs automatically before updating an existing row.
        @PreUpdate
        protected void onUpdate() {
            updatedDate = LocalDateTime.now();
        }
    }
