package kanefreeman;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ApplicationTest {

    // Written with underscores for readability
    private static final String MAX_LENGTH_TEST_INPUT = "187_531_531_520_036_100_187_531_520_036_776_890_188_100_187_531_520_036_163_270_013_110".replaceAll("_", "");
    private static final String MAX_LENGTH_TEST_OUTPUT = "one hundred and eighty seven vigintillion, five hundred and thirty one novemdecillion"
            + ", five hundred and thirty one octodecillion, five hundred and twenty septendecillion, thirty six sexdecillion, one hundred quindecillion"
            + ", one hundred and eighty seven quattuordecillion, five hundred and thirty one tredecillion, five hundred and twenty duodecillion"
            + ", thirty six undecillion, seven hundred and seventy six decillion, eight hundred and ninety nonillion, one hundred and eighty eight octillion"
            + ", one hundred septillion, one hundred and eighty seven sextillion, five hundred and thirty one quintillion, five hundred and twenty quadrillion"
            + ", thirty six trillion, one hundred and sixty three billion, two hundred and seventy million, thirteen thousand, one hundred and ten";

    @Test
    public void InvalidNumbers() {
        // Sequential tests
        assertEquals("Not a number", Application.processNumber("A"));
        assertEquals("Not a number", Application.processNumber("-946UHG27"));
        assertEquals("Not a number", Application.processNumber("502^068"));
        assertEquals("Not a number", Application.processNumber("-40*-0"));
        assertEquals("Not a number", Application.processNumber("-6B32"));
        assertEquals("Not a number", Application.processNumber("4d0ad"));
        assertEquals("Not a number", Application.processNumber("-7n<5"));

        // Parallel tests
        assertEquals("Not a number", Application.processNumber("150d06", true));
        assertEquals("Not a number", Application.processNumber("-160)40", true));
        assertEquals("Not a number", Application.processNumber("1705'6027", true));
        assertEquals("Not a number", Application.processNumber("8430f", true));
        assertEquals("Not a number", Application.processNumber("A" + MAX_LENGTH_TEST_INPUT, true));
    }

    @Test
    public void SingleSegment() {
        assertEquals("zero", Application.processNumber("0"));
        assertEquals("three", Application.processNumber("3"));
        assertEquals("twelve", Application.processNumber("12"));
        assertEquals("four hundred", Application.processNumber("400"));
        assertEquals("six hundred and thirty two", Application.processNumber("632"));
        assertEquals("fourty", Application.processNumber("40"));
        assertEquals("seventy five", Application.processNumber("75"));
    }

    @Test
    public void TwoSegments() {
        assertEquals("fifty two hundred and three", Application.processNumber("5203"));
        assertEquals("three hundred thousand, four", Application.processNumber("300004"));
        assertEquals("ninety four thousand, six hundred and twenty seven", Application.processNumber("94627"));
        assertEquals("five hundred and two thousand, sixty eight", Application.processNumber("502068"));
    }

    @Test
    public void Teens() {
        assertEquals("eleven thousand, two hundred and three", Application.processNumber("11203"));
        assertEquals("twelve million, four", Application.processNumber("12000004"));
        assertEquals("thirteen", Application.processNumber("13"));
        assertEquals("fourteen billion", Application.processNumber("14000000000"));
        assertEquals("fifteen thousand, six", Application.processNumber("15006"));
        assertEquals("sixteen thousand, fourty", Application.processNumber("16040"));
        assertEquals("seventeen million, fifty six thousand, twenty seven", Application.processNumber("17056027"));
        assertEquals("eighteen thousand, ninety nine", Application.processNumber("18099"));
        assertEquals("nineteen billion, five hundred and twenty three million, four hundred and fifty five", Application.processNumber("19523000455"));
    }

    @Test
    public void Negatives() {
        assertEquals("zero", Application.processNumber("-0"));
        assertEquals("negative ninety four thousand, six hundred and twenty seven", Application.processNumber("-94627"));
        assertEquals("negative five hundred and two thousand, sixty eight", Application.processNumber("-502068"));
        assertEquals("negative four hundred", Application.processNumber("-400"));
        assertEquals("negative six hundred and thirty two", Application.processNumber("-632"));
        assertEquals("negative fourty", Application.processNumber("-40"));
        assertEquals("negative seventy five", Application.processNumber("-75"));
        assertEquals("negative fifteen thousand, six", Application.processNumber("-15006"));
        assertEquals("negative sixteen thousand, fourty", Application.processNumber("-16040"));
        assertEquals("negative seventeen million, fifty six thousand, twenty seven", Application.processNumber("-17056027"));
        assertEquals("negative eighty four hundred and thirty", Application.processNumber("-8430"));
        assertEquals("negative two thousand, thirty six", Application.processNumber("-2036"));
    }

    @Test
    /**
     * For the output to sound more natural, numbers between 1000 and 9999 should be written with the following rules:
     *
     * 1. If the first two digits are a multiple of 10, use thousand syntax
     * 2. If the first two digits are NOT a multiple of 10, write as hundreds
     */
    public void HundredsAndThousands() {
        assertEquals("eleven hundred and three", Application.processNumber("1103"));
        assertEquals("twenty one hundred and thirty six", Application.processNumber("2136"));
        assertEquals("fourty five hundred", Application.processNumber("4500"));
        assertEquals("ninety nine hundred and ninety eight", Application.processNumber("9998"));
        assertEquals("sixteen hundred and fourty two", Application.processNumber("1642"));
        assertEquals("eighty four hundred and thirty", Application.processNumber("8430"));

        assertEquals("two thousand, thirty six", Application.processNumber("2036"));
        assertEquals("four thousand, six", Application.processNumber("4006"));
        assertEquals("five thousand, fourty five", Application.processNumber("5045"));
        assertEquals("seven thousand", Application.processNumber("7000"));
        assertEquals("nine thousand, eleven", Application.processNumber("9011"));
    }

    @Test
    public void HandleLeadingZeros() {
        assertEquals("zero", Application.processNumber("00"));
        assertEquals("zero", Application.processNumber("0000"));
        assertEquals("ten", Application.processNumber("0010"));
        assertEquals("fifty two hundred and three", Application.processNumber("00000005203"));
    }

    @Test
    public void MaxLengthTest() {
        assertEquals(MAX_LENGTH_TEST_OUTPUT, Application.processNumber(MAX_LENGTH_TEST_INPUT));
    }

    @Test
    public void SequentialVsParallelTest() {
        // Sequential test
        String sequentialResponse = Application.processNumber(MAX_LENGTH_TEST_INPUT);

        // Parallel test
        String parallelResponse = Application.processNumber(MAX_LENGTH_TEST_INPUT, true);
        assertEquals(sequentialResponse, parallelResponse);
    }
}
