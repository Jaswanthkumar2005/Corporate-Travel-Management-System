package com.travel.system.repositories;

import com.travel.system.models.TravelRequest;
import com.travel.system.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TravelRequestRepository extends JpaRepository<TravelRequest, Long> {
    List<TravelRequest> findByEmployee(User employee);
}
