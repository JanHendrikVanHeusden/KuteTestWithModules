package nl.kute.kutetest;

import kotlin.jvm.functions.Function1;
import nl.kute.asstring.property.meta.PropertyMeta;

import java.util.List;

import static nl.kute.asstring.config.AsStringConfigKt.asStringConfig;
import static nl.kute.asstring.core.AsString.asString;

@SuppressWarnings("unused")  // instance vars accessed reflectively
public class UsingAsStringSubClass extends UsingAsString {
    List<String> myListToExclude = List.of("I", "will", "be", "excluded");
    String[] myArrayToExclude = {"I", "will", "also", "be", "excluded"};
    String[] myArrayToInclude = {"I", "will", "be", "included"};
    List<String> myListToInclude = List.of("I", "will", "also", "be", "included");

//    public String toString() {
//        return asString(this);
//    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Function1<? super PropertyMeta, Boolean> propertyMetaFilter =
                (PropertyMeta m) -> m.getPropertyName().contains("Exclude");
        {
            asStringConfig()
                    .withPropertyOmitFilters(propertyMetaFilter)
                    .applyAsDefault();
        }
        UsingAsStringSubClass instance = new UsingAsStringSubClass();
        try {
            System.out.println("Excluded vars will be excluded:");
            String asStringResult = asString(instance);
            System.out.println(asStringResult); // calls asString() in abstract super class
            if (asStringResult.contains("Exclude")) {
                throw new AssertionError("properties with `Exclude` in their names should be excluded," +
                        " but result includes these:\n" + asStringResult);
            }
        } finally {
            asStringConfig()
                    .withPropertyOmitFilters() // empty, so clears filters
                    .applyAsDefault();
        }
        System.out.println("Excluded vars will be there now:");
        String asStringResult = asString(instance);
        System.out.println(asStringResult); // calls asString() in abstract super class
        if (!asStringResult.contains("Exclude")) {
            throw new AssertionError("properties with `Exclude` in their names should NOT be excluded anymore," +
                    " but result does not include these:\n" + asStringResult);
        }
    }
}
