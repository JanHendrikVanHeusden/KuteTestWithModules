package nl.kute.kutetest;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

import static nl.kute.asstring.core.AsString.asString;

@SuppressWarnings({"FieldMayBeFinal", "unused"})  // instance vars accessed reflectively
public class TestAsString extends AbstractTestAsString {

    private String aRandomString = RandomStringUtils.randomAlphabetic(5);
    private int aRandomInt = new Random().nextInt();

    public static void main(String[] args) {
        TestAsString instance = new TestAsString();

        String localAsString = asString(instance);

        // Calls toString() of AbstractTestAsString; it's `toString()` calls `asString(this)`
        // Due to reflective call, it should include `this` class's properties as well
        String superToString = instance.toString();

        // output both
        System.out.println(superToString);
        System.out.println(superToString);

        if (!superToString.equals(localAsString)) {
            throw new AssertionError("Both Strings should be equal, but they aren't!");
        } else {
            System.out.println("OK :-)");
        }
    }

}
