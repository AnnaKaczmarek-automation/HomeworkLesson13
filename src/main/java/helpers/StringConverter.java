package helpers;
import java.text.DecimalFormat;

public class StringConverter {

    public  Double covertStringIntoDouble(String string){
        DecimalFormat dFormat = new DecimalFormat("#,###.##");
        String formattedString = string.substring(1);
        double number = Double.parseDouble(dFormat.format(formattedString));
        return number;
    }

}
