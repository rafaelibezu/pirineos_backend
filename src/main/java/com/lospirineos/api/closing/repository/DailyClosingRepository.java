package com.lospirineos.api.closing.repository;

import com.lospirineos.api.closing.entity.DailyClosing;
import com.lospirineos.api.common.enums.ClosingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface DailyClosingRepository extends JpaRepository<DailyClosing, Long> {

    List<DailyClosing> findAllByStatus(ClosingStatus status);

    List<DailyClosing> findAllByWaiterId(Long waiterId);

    List<DailyClosing> findAllByCashRegisterId(Long cashRegisterId);

    List<DailyClosing> findAllByShiftId(Long shiftId);

    List<DailyClosing> findAllByClosingAtBetween(
            OffsetDateTime start,
            OffsetDateTime end
    );
}