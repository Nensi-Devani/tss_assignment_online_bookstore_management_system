package com.bookstore.tss_assignment_online_bookstore_management_system.entity;

import com.bookstore.tss_assignment_online_bookstore_management_system.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tools.jackson.core.ObjectReadContext;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user_profiles")
public class UserProfile extends ObjectReadContext.Base {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;

    @Column
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column
    private LocalDateTime dateOfBirth;

    @Column
    private String avatarPath;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;
}
