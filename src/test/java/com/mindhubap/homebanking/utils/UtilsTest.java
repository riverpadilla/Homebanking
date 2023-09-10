package com.mindhubap.homebanking.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UtilsTest {

    @Test
    public void generateAccountNumber() {

        String accountNumber;
        for (int i=1; i<=25; i++) {
            accountNumber = Utils.generateAccountNumber();
            assertThat(accountNumber, not(blankOrNullString()));
            assertThat(accountNumber, startsWith("VIN-"));
            assertThat(accountNumber.length(), equalTo(12));
        }
    }

    @Test
    public void generateCardNumber() {

        String cardNumber;
        for (int i=1; i<=25; i++){
            cardNumber= Utils.generateCardNumber();
            assertThat(cardNumber, not(blankOrNullString()));
            assertThat(cardNumber.length(), equalTo(19));
        }

    }

    @Test
    public void isValidEmail() {

        boolean validEmail = Utils.isValidEmail("email@dominio.ext");
        assertThat(validEmail,equalTo(true));
    }

    @Test
    public void generateCvv() {

        short cvv;
        for (int i=1; i<=25; i++) {
            cvv = Utils.generateCvv();
            assertThat(cvv, lessThanOrEqualTo((short) 999) );
            assertThat(cvv, greaterThanOrEqualTo((short) 100));
        }

    }
}