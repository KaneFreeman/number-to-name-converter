package kanefreeman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Application {

    private static final char ZERO = '0';
    private static final char ONE = '1';

    private static final Map<Integer, String> LARGE_NUMBER_NAMES = new HashMap<Integer, String>() {
        {
            put(3, "thousand");
            put(6, "million");
            put(9, "billion");
            put(12, "trillion");
            put(15, "quadrillion");
            put(18, "quintillion");
            put(21, "sextillion");
            put(24, "septillion");
            put(27, "octillion");
            put(30, "nonillion");
            put(33, "decillion");
            put(36, "undecillion");
            put(39, "duodecillion");
            put(42, "tredecillion");
            put(45, "quattuordecillion");
            put(48, "quindecillion");
            put(51, "sexdecillion");
            put(54, "septendecillion");
            put(57, "octodecillion");
            put(60, "novemdecillion");
            put(63, "vigintillion");
        }
    };

    private static final Map<Character, String> SMALL_NUMBER_NAMES = new HashMap<Character, String>() {
        {
            put('1', "one");
            put('2', "two");
            put('3', "three");
            put('4', "four");
            put('5', "five");
            put('6', "six");
            put('7', "seven");
            put('8', "eight");
            put('9', "nine");
        }
    };

    private static final Map<Character, String> TENS_NUMBER_NAMES = new HashMap<Character, String>() {
        {
            put('1', "ten");
            put('2', "twenty");
            put('3', "thirty");
            put('4', "fourty");
            put('5', "fifty");
            put('6', "sixty");
            put('7', "seventy");
            put('8', "eighty");
            put('9', "ninety");
        }
    };

    private static final Map<Character, String> TEENS_NUMBER_NAMES = new HashMap<Character, String>() {
        {
            put('1', "eleven");
            put('2', "twelve");
            put('3', "thirteen");
            put('4', "fourteen");
            put('5', "fifteen");
            put('6', "sixteen");
            put('7', "seventeen");
            put('8', "eighteen");
            put('9', "nineteen");
        }
    };

    private static void testProcessNumber(String number) {
        testProcessNumber(number, false);
        testProcessNumber(number, true);
    }

    private static void testProcessNumber(String number, boolean parallel) {
        long start = System.nanoTime();
        String result = processNumber(number, parallel);
        long stop = System.nanoTime();
        long duration = stop - start;
        System.out.println("number: " + number + ", [" + (parallel ? "parallel" : "sequential") + "] time = " + duration + " ns, result: " + result);
    }

    public static String processNumber(String number) {
        return processNumber(number, false);
    }

    public static String processNumber(String input, boolean parallel) {
        boolean negative = input.startsWith("-");
        if (negative) {
            input = input.substring(1, input.length());
        }

        final String number = input.replaceFirst("^0+(?!$)", "");
        if ("0".equals(number)) {
            return "zero";
        }

        final int length = number.length();
        int segments;
        int segmentLength;
        try {
            // If number is four digits long and the first two digits (from left) are not multiples of 10
            if (length == 4 && Integer.valueOf(number.substring(0, 2)) % 10 != 0) {
                segments = 1;
                segmentLength = 4;
            } else { // All other cases
                segments = (int) Math.ceil((double) length / 3.0);
                segmentLength = 3;
            }

            IntStream stream = IntStream.range(0, segments);
            if (parallel) {
                stream.parallel();
            }

            List<Segment> output;
            output = stream.mapToObj(idx -> {
                int end = length - idx * segmentLength;
                int start = length - (idx + 1) * segmentLength;
                if (start < 0) {
                    start = 0;
                }

                return processSegment(number.substring(start, end), idx * segmentLength);
            }).filter(seg -> null != seg).collect(Collectors.toList());

            output.sort((Segment s2, Segment s3) -> s3.getPower() - s2.getPower());

            return (negative ? "negative " : "") + output.stream().map(seg -> seg.getWordValue()).collect(Collectors.joining(", "));
        } catch (NumberFormatException e) {
            return "Not a number";
        }
    }

    private static Segment processSegment(String segment, int power) throws NumberFormatException {
        Integer.parseInt(segment);

        int length = segment.length();
        if (length > 4 || length == 0) {
            return null;
        }

        // Calculate words for digits 4 and 3 (from right) if present
        String s0, s1;
        char c0 = length == 4 ? segment.charAt(0) : ZERO;
        char c1 = length >= 3 ? segment.charAt(length - 3) : ZERO;
        if (ONE == c0 && ZERO != c1) {
            s0 = null;
            s1 = TEENS_NUMBER_NAMES.get(c1);
        } else {
            s0 = TENS_NUMBER_NAMES.get(c0);
            s1 = SMALL_NUMBER_NAMES.get(c1);
        }

        // Calculate words for digits 1 and 2 (from right) if present
        String s2, s3;
        char c2 = length >= 2 ? segment.charAt(length - 2) : ZERO;
        char c3 = segment.charAt(length - 1);
        if (ONE == c2 && ZERO != c3) {
            s2 = TEENS_NUMBER_NAMES.get(c3);
            s3 = null;
        } else {
            s2 = TENS_NUMBER_NAMES.get(c2);
            s3 = SMALL_NUMBER_NAMES.get(c3);
        }

        // Combine words for each digit
        String output = "";

        // Only present if total number is four digits long and first two digits (from left) are not a multiple of 10
        if (null != s0) {
            output += s0;
        }

        if (null != s1) {
            output += (!output.isEmpty() ? " " : "") + s1 + " hundred";
            if (null != s2 || null != s3) {
                output += " and";
            }
        }

        if (null != s2) {
            output += (!output.isEmpty() ? " " : "") + s2;
        }

        if (null != s3) {
            output += (!output.isEmpty() ? " " : "") + s3;
        }

        String powerOutput = LARGE_NUMBER_NAMES.get(power);
        if (output.isEmpty()) {
            return null;
        }

        return new Segment(segment, output + (null != powerOutput ? " " + powerOutput : ""), power);
    }

    public static void main(String[] args) {
        testProcessNumber(String.valueOf(1_013_125));
        testProcessNumber(String.valueOf(12));
        testProcessNumber(String.valueOf(3_004));
        testProcessNumber(String.valueOf(666_071_009_120L));
        testProcessNumber(String.valueOf(180_623_050_103_505L));
        testProcessNumber(String.valueOf(1_032_120_603_000_003_100L));

        testProcessNumber("776_890_188_100_187_531_520_036_163_270_013_110".replaceAll("_", ""));
        testProcessNumber("100_187_531_520_036_776_890_188_100_187_531_520_036_163_270_013_110".replaceAll("_", ""));
        testProcessNumber("187_531_531_520_036_100_187_531_520_036_776_890_188_100_187_531_520_036_163_270_013_110".replaceAll("_", ""));
    }
}
