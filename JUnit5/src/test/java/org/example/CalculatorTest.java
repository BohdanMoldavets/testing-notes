package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    Calculator calculator;

    @BeforeEach
    void beforeAll() {
        calculator = new Calculator();
    }

    @DisplayName("/ by zero")
    @Test
    void testIntegerDivision_shouldThrowAritheticException_whenDivisorEqualsZero() {
        Exception exception = assertThrows(ArithmeticException.class,
                () -> calculator.integerDivision(20, 0));

        assertEquals(ArithmeticException.class, exception.getClass());
    }

    @DisplayName("Test example")
    @ParameterizedTest
    @MethodSource()
    void testIntegerSubtraction_shouldReturnCorrectResult_whenInputContainsCorrectParams(int minuend, int subtrahend, int expectedResult) {

        int actualResult = calculator.integerSubtraction(minuend, subtrahend);

        assertEquals(expectedResult, actualResult, minuend + " - " + subtrahend + " must be equals " + expectedResult);
    }

    @DisplayName("Test example")
    @ParameterizedTest
    @CsvSource( {
            "11,1,10",
            "83,2,81"
    })
    void testIntegerSubtraction_shouldReturnCorrectResult_whenInputContainsCorrectParamsFormCsvSource(int minuend, int subtrahend, int expectedResult) {

        int actualResult = calculator.integerSubtraction(minuend, subtrahend);

        assertEquals(expectedResult, actualResult, minuend + " - " + subtrahend + " must be equals " + expectedResult);
    }

    @DisplayName("Test example")
    @ParameterizedTest
    @CsvFileSource(resources = "/integerSubtraction.csv")
    void testIntegerSubtraction_shouldReturnCorrectResult_whenInputContainsCorrectParamsFormCsvFileSource(int minuend, int subtrahend, int expectedResult) {

        int actualResult = calculator.integerSubtraction(minuend, subtrahend);

        assertEquals(expectedResult, actualResult, minuend + " - " + subtrahend + " must be equals " + expectedResult);
    }




    //For annotation @MethodSource, but @CsvSource - is better!
    private static Stream<Arguments> testIntegerSubtraction_shouldReturnCorrectResult_whenInputContainsCorrectParams () {
        return Stream.of(
                Arguments.of(24,1,23),
                Arguments.of(55,5,50),
                Arguments.of(83,2,81)
        );
    }
}