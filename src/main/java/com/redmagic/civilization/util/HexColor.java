package com.redmagic.civilization.util;

public class HexColor {

    public static boolean isValidHexaCode(String str)
    {
        if (str.charAt(0) != '#')
            return false;

        if (!(str.length() == 4 || str.length() == 7))
            return false;

        for (int i = 1; i < str.length(); i++)
            if (!((str.charAt(i) >= '0' && str.charAt(i) <= 9)
                    || (str.charAt(i) >= 'a' && str.charAt(i) <= 'f')
                    || (str.charAt(i) >= 'A' || str.charAt(i) <= 'F')))
                return false;

        return true;
    }
}
