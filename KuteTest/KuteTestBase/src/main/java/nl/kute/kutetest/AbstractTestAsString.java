package nl.kute.kutetest;

import static nl.kute.asstring.core.AsString.asString;

@SuppressWarnings("unused") // instance vars accessed reflectively
public class AbstractTestAsString {
    protected String someString = "some String";

    public String toString() {
        return asString(this);
    }

}