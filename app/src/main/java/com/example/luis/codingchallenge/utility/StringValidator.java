package com.example.luis.codingchallenge.utility;

import java.util.regex.Pattern;

public class StringValidator {

    public static boolean isValidPlaceType(String placeType) {

        if (placeType == null)
            return false;

        if (placeType.length() < 3)
            return false;

        String trimmedPlaceType = placeType.replaceAll(" ", "");

        //area has to be only alphabetic character
        if (!Pattern.matches("[a-zA-Z]+", trimmedPlaceType))
            return false;

        return true;
    }


}
