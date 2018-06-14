package com.taboola.tests.ex1.thirdparty;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * This class was taken from the net. bt
 * It simply compute any arithmetic operation <br/>
 *
 *
 */
public class SimpleCalc {

    // Initialise public version number and usage info.
    public static String welcomeMsg = "SimpleCalc - The simple calculator - v1.11";
    public static String usage = "Usage: SimpleCalc [math expression] (precision). Use only +, -, * and /.";

    // Default Precision VariableImpl
    public static Integer outputPrecision = 10;


    public static void fatalError(String exitMsg, int errorCode) {
        if (exitMsg.length() > 0)   // if the exitMsg is not ""
            System.out.println("Fatal Error " + errorCode + ": " + exitMsg);

        System.out.println(usage);
        System.out.println("");

        System.exit(errorCode);
    }

    private static void initialiseArgumentsAndSetPrecision(String [] args) {
        // Check for arguments.
        if (args.length < 1 || args[0].equals("?") || args[0].equals("--help"))
            fatalError("", 1);

        if(args.length > 1 && isInteger(args[1]))
            outputPrecision = new Integer(args[1]);
        else if(args.length > 1 && !isInteger(args[1]))
            fatalError("Invalid precision. Must be an integer.",1);
    }


    private static BigDecimal toBigDecimal(String num) {
        // Returns a BigDecimal from a String or handles a NumberFormatException.

        try {
            return new BigDecimal(num);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number '" + num + "'. Must be a numeric value. Use '.' for decimals");
//            fatalError("Invalid number '" + num + "'. Must be a numeric value. Use '.' for decimals.", 2);
//            return BigDecimal.ZERO; // is never called.
        }
    }

    private static boolean isBigDecimal(String num) {
        // Test if a number is a BigDecimal. Return TRUE or FALSE.

        try {
            BigDecimal testVariable = new BigDecimal(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isInteger(String num) {
        // Test if a number is an integer. Return TRUE or FALSE.

        try {
            Integer testVariable = new Integer(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static String [] splitStringAtPlusSigns(String input) {
        // Replace '-' with '+-' to be able to split the string at '+' for both subtraction and addition.
        // Afterwards, split the array at '+'

        String replacedPlusMinusString = "";

        if ( input.substring(0,1).equals("-") ) {
            // If first char is "-", it shouldn't be replaced (or else the string with start like "+-2+2" and the program will crash).
            replacedPlusMinusString = "-" + input.substring(1).replaceAll("-","+-");
        }
        else {
            replacedPlusMinusString = input.replaceAll("-","+-");
        }

        // Check if some division and multiplication have been replaced wrongly - f.x. "2/-2" -> "2/+-2" or "2*-2" -> "2*+-2"
        replacedPlusMinusString = replacedPlusMinusString.replaceAll("/\\+","/");
        replacedPlusMinusString = replacedPlusMinusString.replaceAll("\\*\\+","*");

        // Split string at '+'
        return replacedPlusMinusString.split("\\+");
    }

    private static String [] splitStringAtMultiplicationSigns(String input) {

        // Split the string at "*"
        return input.split("\\*");
    }


    public static BigDecimal calculateNumericValue (String input) {

        // Parse the input string.

        // 1:
        // Replace '-' with '+-' to be able to split the string at '+' for both subtraction and addition.
        // Afterwards, split the array at '+'
        // The result is an array with values to be added together.
        String [] splitArrayAtPlus = splitStringAtPlusSigns(input);


        // 2:
        // Calculate the numeric value of each entry in the splitArrayAtPlus-array.
        // When all entries are numeric, the sum can be calculated.

        for (int i=0 ; i<splitArrayAtPlus.length ; i++)
        {
            // Two scenarios:
            // 1: Either the string is a BigDecimal (if there are no "/" and "*"-signs in the string)
            // 2: The string contains contains non-numeric chars.

            // Use boolean isBigDecimal to check if the number is a BigDecimal.
            // If it is not a BigDecimal (ie. a numeric value), try to calculate the numeric value.

            if ( !isBigDecimal(splitArrayAtPlus[i]) )
                splitArrayAtPlus[i] = calculateNumericValueFromMultiplication(splitArrayAtPlus[i]);
        }

        // Return the final sum
        return calculateFinalResultFromStringArray(splitArrayAtPlus);
    }

    private static String calculateNumericValueFromMultiplication (String input) {
        // Input must be a string with only multiplication and division, f.x. "2*2+4*8/2"
        // Output is a string with the calculated numeric value.

        // Split the string at "*"
        String [] splitArrayAtStar = splitStringAtMultiplicationSigns(input);

        // Initiate tempMultiplicationValue as ONE (because of multiplication to come - 1*x1*x2*x3 ...)
        BigDecimal tempMultiplicationValue = BigDecimal.ONE;

        // Cycle through the array split at '*' and calculate the product.
        for (int j=0 ; j<splitArrayAtStar.length ; j++)
        {
            // Check if the value is a BigDecimal.
            // If not, assume that it is because it contains "/" (dashes) and calculate the division.
            if (!isBigDecimal(splitArrayAtStar[j]))
                splitArrayAtStar[j] = calculateNumericValueFromDivision( splitArrayAtStar[j] );

            // As the loops iterates, tempMultiplicationValue will be the product of the array values.
            tempMultiplicationValue = tempMultiplicationValue.multiply( toBigDecimal(splitArrayAtStar[j]) );
        }

        // Now the product of the splitArrayAtPlus-array value is calculated. Put it back into the array.
        return tempMultiplicationValue.toPlainString();
    }

    private static String calculateNumericValueFromDivision (String input) {
        // Input is a string containing "/", for example "5/2"
        // Output is a rounded numeric value

        // Split the number at "/"
        String [] splitArrayAtDash = input.split("/");

        // Do the division and return!
        return toBigDecimal(splitArrayAtDash[0])
                .divide( toBigDecimal(splitArrayAtDash[1]) , outputPrecision*100, BigDecimal.ROUND_HALF_UP)
                .toPlainString();
    }

    private static BigDecimal calculateFinalResultFromStringArray (String [] input) {

        BigDecimal finalSum = BigDecimal.ZERO;

        for (int i = 0 ; i<input.length ; i++)
            finalSum = finalSum.add( toBigDecimal(input[i]) );

        // Divide with one and round to outputPrecision to get the final result
        return finalSum.divide(BigDecimal.ONE, outputPrecision, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();

    }


    public static void main(String[] args) {

        String line = null;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println(welcomeMsg);
            System.out.println("Please enter your expression");
            line = scanner.nextLine();
            mainOriginal(line.split(" "));
        }
        while(!"".equals(line));

    }
    public static void mainOriginal(String args[]) {


        System.out.println(welcomeMsg);
        initialiseArgumentsAndSetPrecision(args);

        System.out.println("Precision is " + outputPrecision + " decimals.");


        // Calculate the numeric value of args[0]:
        BigDecimal finalSum = calculateNumericValue(args[0]);

        // Print the result.
        System.out.println(args[0] + " = " + finalSum.toPlainString());

        // Print an extra line...
        System.out.println();

    }


}