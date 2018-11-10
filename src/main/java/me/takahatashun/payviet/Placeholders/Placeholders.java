package me.takahatashun.payviet.Placeholders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Placeholders {
    public static String Bonus(String input){
        final String bregex = "(?ium)\\{bonus:\\d*\\.?\\d*:\\d*\\.?\\d*}";
        Pattern p = Pattern.compile(bregex);
        Matcher m = p.matcher(input);
        String output = input;
        while(m.find()){
           String found = m.group(0);
           String[] data = found.substring(1,found.length()-1).split(":",3);
           double value = Double.parseDouble(data[1]);
           double bonus = Double.parseDouble(data[2]);
           double result = value+(bonus*value/100);
           output = input.replace(found,String.valueOf(result));
        }
        return output;
    }
}
