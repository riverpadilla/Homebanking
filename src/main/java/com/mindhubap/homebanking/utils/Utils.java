package com.mindhubap.homebanking.utils;

import com.mindhubap.homebanking.models.Account;
import com.mindhubap.homebanking.models.Card;
import com.mindhubap.homebanking.repositories.AccountRepository;
import com.mindhubap.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class Utils {

    static public String generateAccountNumber(List<Account> accounts)
    {
        String number;
        boolean check;

        do {
            check=true;
            number = "VIN-" + String.format("%08d", 11111111 + (int)(Math.random() * 88888888));

            for(Account account:accounts)
            {
                if (account.getNumber().equals(number)) {
                    check = false;
                    break;
                }
            }
        } while(!check);

        return number;
    }

    static public String generateCardNumber(List<Card> cards)
    {
        String[] cardNumber = new String[4];
        String number = "";
        boolean check;
        do {
            check=true;
            for(int i=0;i<4;i++){
                cardNumber[i] = String.format("%04d", 1000 + (int)(Math.random() * 8999));
                number += cardNumber[i];
                if (i<=2) number += "-";
            }
            for(Card card:cards)
            {
                if (card.getNumber().equals(number)) {
                    check = false;
                    break;
                }
            }
        } while(!check);

        return number;
    }
}
