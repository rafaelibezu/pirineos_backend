package com.lospirineos.api.transfer.entity;

import com.lospirineos.api.cashregister.entity.CashRegister;
import com.lospirineos.api.common.entity.AuditableEntity;
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
@Table(name = "cash_transfers")
public class CashTransfer extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "transfer_at",
            nullable = false
    )
    private OffsetDateTime transferAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "responsible_user_id",
            nullable = false
    )
    private User responsibleUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "source_cash_register_id",
            nullable = false
    )
    private CashRegister sourceCashRegister;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "destination_cash_register_id",
            nullable = false
    )
    private CashRegister destinationCashRegister;

    @Column(
            nullable = false,
            precision = 12,
            scale = 2
    )
    private BigDecimal amount;

    @Column(length = 255)
    private String reason;
}