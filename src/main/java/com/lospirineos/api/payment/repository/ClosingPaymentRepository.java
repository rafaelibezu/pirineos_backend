package com.lospirineos.api.payment.repository;

import com.lospirineos.api.payment.entity.ClosingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClosingPaymentRepository extends JpaRepository<ClosingPayment, Long> {

    List<ClosingPayment> findAllByDailyClosingId(Long dailyClosingId);

    List<ClosingPayment> findAllByDistributorId(Long distributorId);
}