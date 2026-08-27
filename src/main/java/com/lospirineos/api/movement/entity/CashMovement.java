package com.lospirineos.api.movement.entity;

import com.lospirineos.api.common.entity.AuditableEntity;
import com.lospirineos.api.common.enums.AccountType;
import com.lospirineos.api.common.enums.MovementType;
import com.lospirineos.api.shift.entity.Shift;
import com.lospirineos.api.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "cash_movements")
public class CashMovement extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "movement_at",
            nullable = false
    )
    private OffsetDateTime movementAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "shift_id",
            nullable = false
    )
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "movement_type",
            nullable = false,
            length = 20
    )
    private MovementType movementType;

    @Column(
            nullable = false,
            length = 150
    )
    private String concept;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 50
    )
    private AccountType account;

    @Column(length = 150)
    private String beneficiary;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Column(
            name = "photo_url",
            length = 500
    )
    private String photoUrl;
}