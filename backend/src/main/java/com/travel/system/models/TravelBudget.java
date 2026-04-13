package com.travel.system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "travel_budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String department;
    private String costCenter;
    private Integer financialYear;
    private Double totalAllocated;
    private Double totalUtilized;

    public Double getRemainingBalance() {
        return totalAllocated - totalUtilized;
    }
}
