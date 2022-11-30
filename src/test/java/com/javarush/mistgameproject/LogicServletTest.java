package com.javarush.mistgameproject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogicServletTest {

    LogicServlet logic = new LogicServlet();

    @Test
    void parseStoryId_RightValueTest() {
        String testId = "10";
        int value = logic.parseStoryId(testId);
        assertEquals(10, value);
    }

    @ParameterizedTest
    @MethodSource("argsProviderFactory")
    void parseStoryId_WrongValueTest(String wrongId) {
        int value = logic.parseStoryId(wrongId);
        assertEquals(404, value);
    }

        static Stream<String> argsProviderFactory() {
            return Stream.of("String", "-5",  "40");
        }

    @Test
    void calcTimeRemains_RightValueTest() {
        int value = logic.calculateTimeRemains("20", 100);
        assertEquals(80, value);
    }

    @Test
    void calcTimeRemains_WrongValueTest() {
        int value = logic.calculateTimeRemains("String", 100);
        assertEquals(90, value);
    }

    @Test
    void calcTimeRemains_NegativeValueTest() {
        int value = logic.calculateTimeRemains("30", 25);
        assertEquals(0, value);
    }

    @Test
    void calcParamChanging_PositiveValueTest() {
        int value = logic.calculateParamChanging("+5");
        assertEquals(5, value);
    }

    @Test
    void calcParamChanging_NegativeValueTest() {
        int value = logic.calculateParamChanging("-5");
        assertEquals(-5, value);
    }

    @ParameterizedTest
    @MethodSource("argsCalcParamChanging")
    void calcParamChanging_WrongValueTest(String wrongValue) {
        int value = logic.calculateParamChanging(wrongValue);
        assertEquals(0, value);
    }

    static Stream<String> argsCalcParamChanging() {
        return Stream.of("String", "*5",  "5-");
    }
}
