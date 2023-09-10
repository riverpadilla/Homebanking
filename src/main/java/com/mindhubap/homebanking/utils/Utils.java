package com.mindhubap.homebanking.utils;

import com.mindhubap.homebanking.enums.CardColor;
import com.mindhubap.homebanking.enums.CardException;
import com.mindhubap.homebanking.enums.CardType;
import com.mindhubap.homebanking.models.Card;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final public class Utils {

    public static String generateAccountNumber()
    {
         return "VIN-" + String.format("%08d", 11111111 + (int)(Math.random() * 88888888));
    }

    public static String generateCardNumber()
    {
        String[] cardNumber = new String[4];
        StringBuilder number = new StringBuilder();
        for(int i=0;i<4;i++){
                cardNumber[i] = String.format("%04d", 1000 + (int)(Math.random() * 8999));
                number.append(cardNumber[i]);
                if (i<=2) number.append("-");
            }
        return number.toString();
    }

     private static final  Pattern EMAIL_PATTERN = Pattern.compile(
                "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{2,6}$"
        );

    public static short generateCvv() {
        return (short) (100 + Math.random() * 899);
    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    public static String cardExceptionMessage(CardException exception, Card card) {

        String message = null;

        switch (card.getType()){
            case CREDIT:
                message = "Credit Card ";
                break;
            case DEBIT:
                message = "Debit Card ";
                break;
        }

        switch (card.getColor()){
            case SILVER:
                message += "Silver ";
                break;
            case GOLD:
                message += "Gold ";
                break;
            case TITANIUM:
                message += "Titanium ";
                break;
        }

        switch (exception){
            case EXIST:
                message += "exist for ";
                break;
            case MAX_QTY_CREDIT:
                message = "The maximum number of Credit Cards has been reached for ";
                break;
            case MAX_QTY_DEBIT:
                message = "The maximum number of Debit Cards has been reached for ";
                break;
            case CREATED:
                message += "added for ";
                break;
        }

        message += "client " + card.getCardHolder();
        return message;
    }



}
