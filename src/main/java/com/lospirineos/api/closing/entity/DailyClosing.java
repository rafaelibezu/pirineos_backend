package com.lospirineos.api.closing.entity;

import com.lospirineos.api.cashregister.entity.CashRegister;
import com.lospirineos.api.common.entity.AuditableEntity;
import com.lospirineos.api.common.enums.ClosingStatus;
import com.lospirineos.api.shift.entity.Shift;
import com.lospirineos.api.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "daily_closings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyClosing extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "closing_at", nullable = false)
    private OffsetDateTime closingAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cash_register_id", nullable = false)
    private CashRegister cashRegister;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "waiter_id", nullable = false)
    private User waiter;

    @Column(name = "z_closing", nullable = false, precision = 12, scale = 2)
    private BigDecimal zClosing;

    @Column(name = "manual_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal manualAmount;

    @Column(name = "declared_visa", nullable = false, precision = 12, scale = 2)
    private BigDecimal declaredVisa;

    @Column(name = "total_payments", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPayments;

    @Column(name = "closing_photo_url", length = 500)
    private String closingPhotoUrl;

    @Column(name = "coins", nullable = false, precision = 12, scale = 2)
    private BigDecimal coins;

    @Column(name = "banknotes", nullable = false, precision = 12, scale = 2)
    private BigDecimal banknotes;

    @Column(name = "real_pos", nullable = false, precision = 12, scale = 2)
    private BigDecimal realPos;

    @Column(name = "cash_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashTotal;

    @Column(name = "expected_cash", nullable = false, precision = 12, scale = 2)
    private BigDecimal expectedCash;

    @Column(name = "cash_difference", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashDifference;

    @Column(name = "pos_difference", nullable = false, precision = 12, scale = 2)
    private BigDecimal posDifference;

    @Column(name = "cashier_photo_url", length = 500)
    private String cashierPhotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ClosingStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;
}