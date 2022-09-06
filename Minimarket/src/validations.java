import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validations {
    public boolean validateNumbers(String toValidate){
        Pattern patternNumbers = Pattern.compile("(\\D)");
        Matcher matcherNumbers = patternNumbers.matcher(toValidate);
        return matcherNumbers.find();
    }
    public boolean validateEmail(String email){
        Pattern patternEmail = Pattern.compile("^([a-z._\\d]+@(?:(?:gmail|epn|outlook|yahoo|ymail|hotmail|edu|)\\.?){2}(?:com|ec))$"
                , Pattern.CASE_INSENSITIVE);
        Matcher matcherEmail = patternEmail.matcher(email);
        return !matcherEmail.find();
    }
    public boolean validateEmptyFields(JTextField[] listJTs){
        boolean band = true;
        for(JTextField jT : listJTs)
            if (jT.getText().length() == 0) {
                band = false;
                break;
            }
        return band;
    }
    public boolean lengthField(String toValidate, int length){
        return (toValidate.length() != length);
    }
    public boolean minLengthField(String toValidate, int minLength){
        return (toValidate.length() < minLength);
    }

}
