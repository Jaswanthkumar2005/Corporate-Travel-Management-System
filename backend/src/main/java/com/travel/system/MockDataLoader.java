package com.travel.system;

import com.travel.system.models.*;
import com.travel.system.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MockDataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, TravelBudgetRepository budgetRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                // Admin
                User admin = User.builder().name("Admin").email("admin@travel.com").role(Role.ADMIN).department("IT").costCenter("CC-001").build();
                userRepository.save(admin);

                // Manager
                User manager = User.builder().name("John Manager").email("manager@travel.com").role(Role.MANAGER).department("Sales").costCenter("CC-101").build();
                manager = userRepository.save(manager);

                // Employee (Reporting to Manager)
                User employee = User.builder().name("Alice Employee").email("employee@travel.com").role(Role.EMPLOYEE).department("Sales").costCenter("CC-101").managerId(manager.getId()).build();
                userRepository.save(employee);

                // Finance
                User finance = User.builder().name("Bob Finance").email("finance@travel.com").role(Role.FINANCE).department("Finance").costCenter("CC-002").build();
                userRepository.save(finance);

                // Travel Desk
                User travelDesk = User.builder().name("Charlie Travel").email("desk@travel.com").role(Role.TRAVEL_DESK).department("Operations").costCenter("CC-003").build();
                userRepository.save(travelDesk);

                System.out.println("Sample users loaded.");
            }

            if (budgetRepository.count() == 0) {
                TravelBudget salesBudget = TravelBudget.builder()
                        .department("Sales")
                        .costCenter("CC-101")
                        .financialYear(2024)
                        .totalAllocated(1000000.0)
                        .totalUtilized(0.0)
                        .build();
                budgetRepository.save(salesBudget);
                System.out.println("Sample budgets loaded.");
            }
        };
    }
}
