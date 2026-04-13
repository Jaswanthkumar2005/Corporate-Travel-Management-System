package com.travel.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "travel_request_id")
    private TravelRequest travelRequest;

    private String expenseType;
    private Double amount;
    private String receiptUrl;

    @ManyToOne
    @JoinColumn(name = "claimed_by_id")
    private User claimedBy;

    private String status; // PENDING, VERIFIED, REIMBURSED, REJECTED
}
