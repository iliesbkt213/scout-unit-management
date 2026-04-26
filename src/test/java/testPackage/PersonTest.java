package testPackage;

import modelPackage.Person;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersonTest {

    @Test
    void setEmailValidShouldBeAccepted() {
        Person person = new Person();
        person.setEmail("ilies.boukhatem@example.be");
        assertEquals("ilies.boukhatem@example.be", person.getEmail());
    }

    @Test
    void setEmailWithoutAtShouldThrow() {
        Person person = new Person();
        assertThrows(IllegalArgumentException.class,
                () -> person.setEmail("not-an-email"));
    }

    @Test
    void setEmailWithoutDotShouldThrow() {
        Person person = new Person();
        assertThrows(IllegalArgumentException.class,
                () -> person.setEmail("ilies@example"));
    }

    @Test
    void setEmailNullShouldThrow() {
        Person person = new Person();
        assertThrows(IllegalArgumentException.class,
                () -> person.setEmail(null));
    }

    @Test
    void setGenderValidValuesShouldBeAccepted() {
        Person person = new Person();
        person.setGender("M");
        assertEquals("M", person.getGender());
        person.setGender("F");
        assertEquals("F", person.getGender());
        person.setGender("X");
        assertEquals("X", person.getGender());
    }

    @Test
    void setGenderInvalidValueShouldThrow() {
        Person person = new Person();
        assertThrows(IllegalArgumentException.class,
                () -> person.setGender("Z"));
    }

    @Test
    void setBirthDateInTheFutureShouldThrow() {
        Person person = new Person();
        Date future = Date.valueOf(LocalDate.now().plusDays(1));
        assertThrows(IllegalArgumentException.class,
                () -> person.setBirthDate(future));
    }

    @Test
    void setBirthDateInThePastShouldBeAccepted() {
        Person person = new Person();
        Date past = Date.valueOf(LocalDate.of(2010, 1, 1));
        person.setBirthDate(past);
        assertEquals(past, person.getBirthDate());
    }

    @Test
    void setPhoneInvalidShouldThrow() {
        Person person = new Person();
        assertThrows(IllegalArgumentException.class,
                () -> person.setPhone("abc"));
    }

    @Test
    void setPhoneValidShouldBeAccepted() {
        Person person = new Person();
        person.setPhone("+32 81 22 33 44");
        assertEquals("+32 81 22 33 44", person.getPhone());
    }

    @Test
    void setFirstNameEmptyShouldThrow() {
        Person person = new Person();
        assertThrows(IllegalArgumentException.class,
                () -> person.setFirstName("   "));
    }

    @Test
    void setLastNameTooLongShouldThrow() {
        Person person = new Person();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 101; i++) {
            builder.append('a');
        }
        String tooLong = builder.toString();
        assertThrows(IllegalArgumentException.class,
                () -> person.setLastName(tooLong));
    }
}
