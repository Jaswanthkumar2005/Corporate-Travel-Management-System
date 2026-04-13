package com.travel.system.controllers;

import com.travel.system.models.TravelRequest;
import com.travel.system.models.User;
import com.travel.system.repositories.TravelRequestRepository;
import com.travel.system.repositories.UserRepository;
import com.travel.system.services.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travel-requests")
@CrossOrigin(origins = "http://localhost:5173")
public class TravelRequestController {

    @Autowired
    private TravelRequestRepository travelRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkflowService workflowService;

    @PostMapping
    public TravelRequest createRequest(@RequestBody TravelRequest request) {
        // In a real app, user would be from security context
        if (request.getEmployee() != null && request.getEmployee().getId() != null) {
            User emp = userRepository.findById(request.getEmployee().getId()).orElse(null);
            request.setEmployee(emp);
        }
        return travelRequestRepository.save(request);
    }

    @GetMapping
    public List<TravelRequest> getAllRequests() {
        return travelRequestRepository.findAll();
    }

    @GetMapping("/employee/{employeeId}")
    public List<TravelRequest> getEmployeeRequests(@PathVariable Long employeeId) {
        User emp = userRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("User not found"));
        return travelRequestRepository.findByEmployee(emp);
    }

    @PutMapping("/{id}/submit")
    public TravelRequest submitRequest(@PathVariable Long id) {
        return workflowService.submitRequest(id);
    }

    @PutMapping("/{id}/manager-approve")
    public TravelRequest managerApprove(@PathVariable Long id, @RequestParam(required = false) String remarks) {
        return workflowService.managerApprove(id, remarks);
    }

    @PutMapping("/{id}/finance-approve")
    public TravelRequest financeApprove(@PathVariable Long id, @RequestParam(required = false) String remarks) {
        return workflowService.financeApprove(id, remarks);
    }

    @PutMapping("/{id}/book")
    public TravelRequest bookRequest(@PathVariable Long id) {
        return workflowService.bookRequest(id);
    }
}
