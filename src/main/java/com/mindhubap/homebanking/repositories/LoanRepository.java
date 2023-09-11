package com.mindhubap.homebanking.repositories;

import com.mindhubap.homebanking.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long> {

    Loan findByName(String name);

    boolean existsByName(String name);
}
