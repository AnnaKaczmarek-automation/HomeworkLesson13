package models;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.junit.Assert.assertTrue;

public class UserFactory {

    public User.UserBuilder getRandomUser() {

        FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
        String fakeFirstName = fakeValuesService.letterify("??????", true);
        Matcher firstnameMatcher = Pattern.compile("\\w{6}").matcher(fakeFirstName);
        assertTrue(firstnameMatcher.find());

        String fakeLastName = fakeValuesService.letterify("??????????", true);
        Matcher lastNameMatcher = Pattern.compile("\\w{10}").matcher(fakeLastName);
        assertTrue(lastNameMatcher.find());

        String fakeBirthDate = fakeValuesService.numerify("########");
        Matcher birthDateMatcher = Pattern.compile("\\d{2}\\'.'\\d{2}\\'.'\\d{4}").matcher(fakeBirthDate);
        assertTrue(birthDateMatcher.find());

        String fakeEmail = fakeValuesService.bothify("????##@gmail.com");
        Matcher emailMatcher = Pattern.compile("\\w{4}\\d{2}@gmail.com").matcher(fakeEmail);
        assertTrue(emailMatcher.find());

        String fakePassword = fakeValuesService.bothify("?#??#?##");
        Matcher passwordMatcher = Pattern.compile("\\w{1}\\d{1}\\w{2}\\d{1}\\w{1}\\d{2}").matcher(fakePassword);
        assertTrue(passwordMatcher.find());

        User.UserBuilder user = new User.UserBuilder()
                .firstName(fakeFirstName)
                .lastName(fakeLastName)
                .birthDate(fakeBirthDate)
                .email(fakeEmail)
                .password(fakePassword);
        return user;
    }

    public User.UserBuilder getAlreadyRegisteredUser() {
        User.UserBuilder user = new User.UserBuilder()
                .firstName("karolina")
                .lastName("Pawlak")
                .birthDate("06.06.1988")
                .email("kp@gmail.com")
                .password("asdf1234");
        return user;
    }
}
