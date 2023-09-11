package com.mindhubap.homebanking.dtos;

public class CardPaymentDTO {

   private String number;

   private short cvv;

   private  double amount;

   private String description;

    public CardPaymentDTO(String number, short cvv, double amount, String description) {
        this.number = number;
        this.cvv = cvv;
        this.amount = amount;
        this.description = description;
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
}
