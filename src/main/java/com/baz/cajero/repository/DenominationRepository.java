package com.baz.cajero.repository;

import com.baz.cajero.entity.Denomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DenominationRepository  extends JpaRepository<Denomination, Long> {

    List<Denomination> findAllByOrderByValueDesc();

    Optional<Denomination> findByValue(BigDecimal value);

    @Query("SELECT SUM(d.value * d.quantity) FROM Denomination d")
    BigDecimal getTotalBalance();

}
