package com.travel.system.repositories;

import com.travel.system.models.TravelBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TravelBudgetRepository extends JpaRepository<TravelBudget, Long> {
    Optional<TravelBudget> findByDepartmentAndCostCenterAndFinancialYear(String department, String costCenter, Integer financialYear);
}
