package models;

import org.openqa.selenium.WebElement;

public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String birthDate;

    public static final class UserBuilder {

        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private String birthDate;

        public UserBuilder firstName(String name) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }


        public User build() {
            User user = new User();
            user.firstName = this.firstName;
            user.lastName = this.lastName;
            user.birthDate = this.birthDate;
            user.email = this.email;
            user.password = this.password;
            return user;
        }

    }
}

