package helpers;

import java.text.DecimalFormat;

public class StringConverter {


    public  Double covertStringIntoDouble(String string){
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        String formattedString = string.substring(1);
        double number = Double.parseDouble(dFormat.format(formattedString));
        return number;
    }
//
//    public void removeLowerLettersFromString(String string){
//        String outputString = "";
//
//        for (int i = 0; i < string.length; i++) {
//            c = inputString.charAt(i);
//            outputString += Character.isUpperCase(c) ? c + " " : "";
//        }
//        System.out.println(outputString);
//    }

}
