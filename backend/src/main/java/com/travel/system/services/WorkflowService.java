package com.travel.system.services;

import com.travel.system.models.*;
import com.travel.system.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkflowService {

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private TravelBudgetRepository budgetRepository;

    @Transactional
    public TravelRequest submitRequest(Long requestId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != TravelStatus.DRAFT) {
            throw new RuntimeException("Only draft requests can be submitted");
        }
        
        request.setStatus(TravelStatus.SUBMITTED);
        return travelRequestRepository.save(request);
    }

    @Transactional
    public TravelRequest managerApprove(Long requestId, String remarks) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != TravelStatus.SUBMITTED) {
            throw new RuntimeException("Request is not in SUBMITTED state");
        }
        
        request.setStatus(TravelStatus.MANAGER_APPROVED);
        return travelRequestRepository.save(request);
    }

    @Transactional
    public TravelRequest financeApprove(Long requestId, String remarks) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != TravelStatus.MANAGER_APPROVED) {
            throw new RuntimeException("Request must be approved by manager first");
        }

        // Budget check
        User emp = request.getEmployee();
        TravelBudget budget = budgetRepository.findByDepartmentAndCostCenterAndFinancialYear(
                emp.getDepartment(), emp.getCostCenter(), 2024)
                .orElseThrow(() -> new RuntimeException("Budget not found for department"));

        if (budget.getRemainingBalance() < request.getEstimatedCost()) {
            throw new RuntimeException("Insufficient budget for this request");
        }

        // Deduct budget
        budget.setTotalUtilized(budget.getTotalUtilized() + request.getEstimatedCost());
        budgetRepository.save(budget);

        request.setStatus(TravelStatus.FINANCE_APPROVED);
        return travelRequestRepository.save(request);
    }

    @Transactional
    public TravelRequest bookRequest(Long requestId) {
        TravelRequest request = travelRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != TravelStatus.FINANCE_APPROVED) {
            throw new RuntimeException("Request must be approved by finance first");
        }
        
        request.setStatus(TravelStatus.BOOKED);
        return travelRequestRepository.save(request);
    }
}
