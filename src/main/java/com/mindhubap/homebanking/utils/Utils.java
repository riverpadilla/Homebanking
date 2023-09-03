package com.mindhubap.homebanking.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    static public String generateAccountNumber()
    {
         return "VIN-" + String.format("%08d", 11111111 + (int)(Math.random() * 88888888));
    }

    static public String generateCardNumber()
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

     static private final  Pattern EMAIL_PATTERN = Pattern.compile(
                "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]{2,6}$"
        );

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

}
