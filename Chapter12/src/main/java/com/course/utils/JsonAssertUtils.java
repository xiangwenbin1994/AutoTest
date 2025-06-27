package com.course.utils;

import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;

import java.util.Arrays;
import java.util.List;

public class JsonAssertUtils {
    public static void assertEqualsIgnoreQuotes(String expectedJson, String actualJson, String... ignoreFields) throws Exception {
        List<String> fieldsToIgnore = Arrays.asList(ignoreFields);
        Customization[] customizations = fieldsToIgnore.stream().map(field -> new Customization(field, (expected, actual) -> String.valueOf(expected).equals(String.valueOf(actual)))).toArray(Customization[]::new);

        CustomComparator comparator = new CustomComparator(JSONCompareMode.LENIENT, customizations);

        JSONAssert.assertEquals(expectedJson, actualJson, comparator);
    }
}
