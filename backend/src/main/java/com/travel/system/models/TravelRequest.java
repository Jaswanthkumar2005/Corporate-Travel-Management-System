package com.travel.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "travel_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestNumber;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Enumerated(EnumType.STRING)
    private TravelType travelType;

    private String purpose;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String fromCity;
    private String toCity;

    private Double estimatedCost;
    private Double actualCost;

    @Enumerated(EnumType.STRING)
    private TravelStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = TravelStatus.DRAFT;
        if (requestNumber == null) requestNumber = "TR-" + System.currentTimeMillis();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
