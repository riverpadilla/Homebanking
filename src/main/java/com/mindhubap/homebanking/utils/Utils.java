package com.mindhubap.homebanking.utils;

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



}
