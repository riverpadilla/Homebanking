package com.mindhubap.homebanking.dtos;

import java.time.LocalDate;

public class CardPaymentDTO {

   private String number;

   private short cvv;

   private  double amount;

   private String description;

   private LocalDate thruDate;

    public CardPaymentDTO(String number, short cvv, double amount, String description, LocalDate thruDate) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
        this.thruDate = thruDate;
    }

    public String getNumber() {
        return number;
    }

    public short getCvv() {
        return cvv;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }
}
