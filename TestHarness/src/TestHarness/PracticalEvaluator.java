package TestHarness;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * PracticalEvaluator
 *
 * A simple utility for testing expected output of programs based on input for test cases.
 *
 * Use instructions.
 * 1. Setup
 * 2. Available Methods with Examples
 *
 * 1. Setup
 * Setting up to test. You need to either already have test cases prepared in a folder
 * called "TestCases" as part of the project, or use the setupTestCases() method to
 * populate the folder with test cases. Test cases must have the file name structure of:
 * AAA_BBB.in and AAA_BBB.out
 * Where AAA is a Checkpoint number and BBB is a Test Case number (any number 1+).
 * Test Case 1 can have just a .out file if you do not need input, but every other test
 * case requires BOTH a .in and .out file.
 *
 * If you do not have the test cases or a zip file in your practical folder with the
 * test cases inside it you can download it with setupTestCases() method. For example
 * to download and setup all the test cases for Practical 1 you would call:
 * PracticalEvaluator.setupTestCases(1);
 *
 * This will download a file to your project's folder called Practical1Tests.zip,
 * and extract it into a folder called TestCases. Once you have test cases in your
 * project directory the setup is complete.
 *
 * 2. Available Methods with Examples
 * You can call a few variations of the testAllCases() method depending on what you
 * need or what you have access to for available methods to call. The two expected
 * types of methods you can test with are the main method and any method that takes
 * no parameters.
 *
 * The first case is where you want to test a main method. You could for example have
 * a main method defined inside a file HelloWorld.java meaning the class would be
 * HelloWorld. You can use testing for checkpoint 2 with the HelloWorld.java file as follows:
 * PracticalEvaluator.testAllCases(2, HelloWorld::main);
 *
 * Another way to call this would be to specify the option boolean parameters:
 * PracticalEvaluator.testAllCases(2, HelloWorld::main, true, true);
 * The first true makes the expected output show, and the second true makes the validation
 * apply. You can set either of these to false if you do not want to see those outputs.
 *
 * Similar to the use of a main method you can call testAllCases with other methods too.
 * You can use any method that has been defined with no parameter. It is possible to do
 * this with either static or non-static methods. For example you could have a method defined
 * like the following:
 * public static void performCheckpoint2() {
 *     // code here
 * }
 * Then you could test this code for Checkpoint 2 by running the following line (assuming it is in HelloWorld.java).
 * PracticalEvaluator.testAllCases(2, HelloWorld::performCheckpoint2);
 *
 * If you were to remove the static keyword to make it:
 * public void performCheckpoint2() {
 *     // code here
 * }
 * You would then need to call the method like:
 * PracticalEvaluator.testAllCases(2, ()-> new HelloWorld().performCheckpoint2());
 *
 * Just as was the case for the examples with using the main method you can also supply the additional
 * parameters for true/false to make the expected output show, and to optionally perform the validation part.
 * The following two lines show examples of calling these methods with the booleans included.
 * PracticalEvaluator.testAllCases(2, HelloWorld::performCheckpoint2, true, true);
 * PracticalEvaluator.testAllCases(2, ()-> new HelloWorld().performCheckpoint2(), true, true);
 *
 * @author Peter Mitchell
 * @version 2021.1
 */
public class PracticalEvaluator {
    /**
     * Attempts to download a file PracticalXXXTests.zip using downloadTestCases()
     * and if it is either successful in downloading or the file already existed,
     * the file is then unzipped into a TestCases directory.
     *
     * @param practicalNumber The number of the practical file to download and extract.
     */
    public static void setupTestCases(int practicalNumber) {
        boolean success = downloadTestCases(practicalNumber);
        if(success) {
            unZIPTestCases(practicalNumber);
        }
    }

    /**
     * You can use this method to test all expected cases for a specified checkpoint number by also
     * supplying a method. This works for passing the main method. For example if you have a main method
     * in a class called HelloWorld.java you could call this method like:
     * <b>PracticalEvaluator.testAllCases(2, HelloWorld::main);</b>
     * If you are not using the main method for testing, see the other version of this method that can
     * take any method with no parameter.
     *
     * This method will find all valid test cases available for the supplied checkpoint number.
     * For each available test case it will output the input used in that test case (if there is any),
     * followed by the output you should expect to see if your code is performing correctly,
     * then your program's output, and finally the validation of your output line by line
     * showing any differences from the expected output.
     *
     * @param checkpointNumber The checkpoint number to test.
     * @param method Method reference to any method that is defined as methodName(String[] variableName).
     */
    public static void testAllCases(int checkpointNumber, Consumer<String[]> method) {
        testAllCases(checkpointNumber, method, true, true);
    }

    /**
     * You can use this method to test all expected cases for a specified checkpoint number by also
     * supplying a method. This works for passing the any method that has no parameters. For example
     * if you have a static method defined like:
     * public static void exampleMethod()
     * in a class called HelloWorld.java you could call this method like:
     * <b>PracticalEvaluator.testAllCases(2, HelloWorld::exampleMethod);</b>
     * If you need to do this with a non-static method you can write it as:
     * <b>PracticalEvaluator.testAllCases(2, ()-> new HelloWorld().exampleMethod());</b>
     *
     * This method will find all valid test cases available for the supplied checkpoint number.
     * For each available test case it will output the input used in that test case (if there is any),
     * followed by the output you should expect to see if your code is performing correctly,
     * then your program's output, and finally the validation of your output line by line
     * showing any differences from the expected output.
     *
     * @param checkpointNumber The checkpoint number to test.
     * @param method Method reference to any method that is defined as methodName(String[] variableName).
     */
    public static void testAllCases(int checkpointNumber, Runnable method) {
        testAllCases(checkpointNumber, method, true, true);
    }

    /**
     * You can use this method to test all expected cases for a specified checkpoint number by also
     * supplying a method. This works for passing the main method. For example if you have a main method
     * in a class called HelloWorld.java you could call this method like:
     * <b>PracticalEvaluator.testAllCases(2, HelloWorld::main, true, true);</b>
     * If you are not using the main method for testing, see the other version of this method that can
     * take any method with no parameter.
     *
     * This method will find all valid test cases available for the supplied checkpoint number.
     * For each available test case it will output the input used in that test case (if there is any),
     * followed by the output you should expect to see if your code is performing correctly,
     * then your program's output, and finally the validation of your output line by line
     * showing any differences from the expected output.
     *
     * @param checkpointNumber The checkpoint number to test.
     * @param method Method reference to any method that is defined as methodName(String[] variableName).
     * @param showFullExpectedOutput When true, the expected output is shown, when false it is excluded from output.
     * @param showValidation When true, the validation is completed on each individual test, and
     */
    public static void testAllCases(int checkpointNumber, Consumer<String[]> method, boolean showFullExpectedOutput, boolean showValidation) {
        testAllCases(checkpointNumber,()-> method.accept(new String[0]),showFullExpectedOutput,showValidation);
    }

    /**
     * You can use this method to test all expected cases for a specified checkpoint number by also
     * supplying a method. This works for passing the any method that has no parameters. For example
     * if you have a static method defined like:
     * public static void exampleMethod()
     * in a class called HelloWorld.java you could call this method like:
     * <b>PracticalEvaluator.testAllCases(2, HelloWorld::exampleMethod, true, true);</b>
     * If you need to do this with a non-static method you can write it as:
     * <b>PracticalEvaluator.testAllCases(2, ()-> new HelloWorld().exampleMethod(), true, true)</b>
     *
     * This method will find all valid test cases available for the supplied checkpoint number.
     * For each available test case it will output the input used in that test case (if there is any),
     * followed by the output you should expect to see if your code is performing correctly,
     * then your program's output, and finally the validation of your output line by line
     * showing any differences from the expected output.
     *
     * @param checkpointNumber The checkpoint number to test.
     * @param method Method reference to any method that is defined as methodName(String[] variableName).
     * @param showFullExpectedOutput When true, the expected output is shown, when false it is excluded from output.
     * @param showValidation When true, the validation is completed on each individual test, and
     */
    public static void testAllCases(int checkpointNumber, Runnable method, boolean showFullExpectedOutput, boolean showValidation) {
        List<Integer> validTests = getAllValidTestCases(checkpointNumber, false);
        int successCounter = 0;
        for(int testCase : validTests) {
            if(testSingleCase(checkpointNumber, testCase, method, showFullExpectedOutput, showValidation)) {
                successCounter++;
            }
        }

        if(showValidation) {
            System.out.println("\u001B[3"+(successCounter==validTests.size()?2:1)+"mAll Checkpoint "
                    + checkpointNumber + " tests completed. " + successCounter
                    + " of " + validTests.size() + " successfully passed.\n\u001B[0m");
        }
    }

    private static final String TEST_CASE_ZIP_URL = "https://github.com/Squirrelbear/CP1Extras/raw/main/Practical";
    private static final String TEST_CASE_DIRECTORY = "TestCases/";
    private final ByteArrayOutputStream customConsole;
    private final PrintStream defaultConsole;
    private final int checkpointNumber, testInputNumber;
    private final String outputValidationPath, inputValidationPath;
    private final boolean usingCustomInput;

    private PracticalEvaluator(int checkpointNumber, int testInputNumber, boolean redirectOutput) {
        this.checkpointNumber = checkpointNumber;
        this.testInputNumber = testInputNumber;
        defaultConsole = System.out;
        customConsole = new ByteArrayOutputStream();
        if(redirectOutput) {
            System.setOut(new PrintStream(customConsole));
        }
        String checkpointFileStr = checkpointNumber + "_" + testInputNumber;
        inputValidationPath = TEST_CASE_DIRECTORY + checkpointFileStr + ".in";
        outputValidationPath = TEST_CASE_DIRECTORY + checkpointFileStr+".out";
        // Non-1 testInputNumber requires input, otherwise optional only if there is input specified.
        if(testInputNumber != 1 || Files.exists(Path.of(inputValidationPath))) {
            System.setIn(getTestInput());
            usingCustomInput = true;
        } else {
            usingCustomInput = false;
        }
    }

    /**
     * Tests running of a single test case. The test information is shown including any input if there
     * is any input, optionally the expected output, then the actual output, and finally the validation
     * if it was requested.
     *
     * @param checkpointNumber Number of the checkpoint to be evaluated.
     * @param testCase The test case number that is being evaluated.
     * @param method A runnable method referencing the code to be evaluated for output matching.
     * @param showFullExpectedOutput When true, the expected output is displayed.
     * @param showValidation When true, the validation is shown for individual line differences.
     * @return True only if showValidation is true, and the validation was successful.
     */
    private static boolean testSingleCase(int checkpointNumber, int testCase, Runnable method, boolean showFullExpectedOutput, boolean showValidation) {
        System.out.println("Checkpoint " + checkpointNumber + " Test Number: " + testCase);
        PracticalEvaluator practicalEvaluator = new PracticalEvaluator(checkpointNumber, testCase,false);
        practicalEvaluator.showInputIfAny();
        if(showFullExpectedOutput) {
            practicalEvaluator.showFullExpectedOutput();
        }
        System.out.println("\nActual Output:");
        method.run();
        System.out.println("End of Actual Output\n");
        if(showValidation) {
            practicalEvaluator = new PracticalEvaluator(checkpointNumber, testCase,true);
            method.run();
            return practicalEvaluator.validateOutput();
        }
        return false;
    }

    /**
     * Validates the output that was stored since the PracticalEvaluator object was created.
     * Outputs success and failure messages based on any differences found.
     *
     * @return True if the validation found no mismatching output.
     */
    private boolean validateOutput() {
        System.setOut(defaultConsole);
        System.out.println("Validating Checkpoint " + checkpointNumber + " Using Input Variation: " + testInputNumber);
        Scanner checkpointFile;
        try {
            checkpointFile = new Scanner(new File(outputValidationPath));
        } catch (FileNotFoundException e) {
            System.out.println("\u001B[31mFailed to load: " + outputValidationPath);
            System.out.println("Terminating validation...\u001B[0m");
            return false;
        }
        boolean passed = compareExpectedOutput(checkpointFile);
        if(passed) {
            System.out.println("\u001B[32mValidation PASSED!\u001B[0m");
        } else {
            System.out.println("\u001B[31mValidation did not pass all expectations.\u001B[0m");
        }
        System.out.println();
        return passed;
    }

    /**
     * Outputs the test input used for the test case if any has been enabled.
     */
    private void showInputIfAny() {
        if(usingCustomInput) {
            printTestInput();
        }
        System.out.println();
    }

    /**
     * Loads the expected output and prints out all the lines.
     */
    private void showFullExpectedOutput() {
        Scanner checkpointFile;
        try {
            checkpointFile = new Scanner(new File(outputValidationPath));
        } catch (FileNotFoundException e) {
            System.out.println("\u001B[31mFailed to load: " + outputValidationPath);
            System.out.println("Could not show expected output for this task.\u001B[0m");
            return;
        }
        System.out.println("Expected output:");
        while(checkpointFile.hasNextLine()) {
            System.out.println(checkpointFile.nextLine());
        }
        System.out.println("End expected output.");
    }

    /**
     * Compares line by line with the output supplied with the expected output.
     * Any time a difference is found it is logged with counters to be reported at the end.
     * For all mismatching differences, they are shown with the problem output.
     * These include invalid extra (non-empty) lines after the output has ended,
     * correct lines, and invalid lines where a mistake has been made.
     *
     * @param checkpointFile The opened expected output file ready for comparison.
     * @return True if there are no invalid occurrences.
     */
    private boolean compareExpectedOutput(Scanner checkpointFile) {
        String[] lines = customConsole.toString().split("\n");
        int correctLines = 0, invalidLines = 0, invalidExtraLines = 0, unnecessaryExtraLines = 0, missingLines;
        int lineNumber = 0;
        for (String s : lines) {
            lineNumber++;
            String line = s.trim();
            if (!checkpointFile.hasNextLine()) {
                if (line.isEmpty()) {
                    unnecessaryExtraLines++;
                } else {
                    invalidExtraLines++;
                    System.out.println("Unexpected extra output on line "+lineNumber+":\n\u001B[31m" + line + "\u001B[0m");
                }
            } else if(validateLine(line, checkpointFile.nextLine().trim(), lineNumber)) {
                correctLines++;
            } else {
                invalidLines++;
            }
        }
        missingLines = countMissingLines(checkpointFile);
        System.out.println("Finished Validation of Checkpoint " + checkpointNumber + ".");
        System.out.printf("Correct Lines: %d, Incorrect: %d, Incorrect Extra: %d, Unnecessary Blanks: %d, Missing Lines: %d%n",
                correctLines, invalidLines, invalidExtraLines, unnecessaryExtraLines, missingLines);
        return invalidLines == 0 && invalidExtraLines == 0 && missingLines == 0;
    }

    /**
     * Compares the lines for equality. If there is a difference, the first index where
     * a character is different is detected. Then colour coding is applied to show
     * the point where the first difference occurred with green for correct and red for incorrect.
     *
     * @param line The current line to be compared for equality against expectedLine.
     * @param expectedLine The line that is expected for the current evaluation.
     * @param lineNumber The number of the line used for an error message in the cast of mismatching content.
     * @return True if the line equals the expected line.
     */
    private boolean validateLine(String line, String expectedLine, int lineNumber) {
        if(line.equals(expectedLine)) {
            return true;
        }

        // Find the first point of difference for colour coding
        int sameIndex = 0;
        while (sameIndex < expectedLine.length() && sameIndex < line.length()
                && line.charAt(sameIndex) == expectedLine.charAt(sameIndex)) {
            sameIndex++;
        }

        // Output the line with green for as many characters as match and red where the difference starts onward.
        System.out.println("Line mismatch. Expected on line "+lineNumber+":\n\u001B[32m"
                + expectedLine.substring(0,sameIndex) + "\u001B[31m" + expectedLine.substring(sameIndex)
                + "\u001B[0m\nGot:\n\u001B[32m" + line.substring(0,sameIndex)
                + "\u001B[31m" + line.substring(sameIndex) + "\u001B[0m");
        return false;
    }

    /**
     * Counts the number of lines that were not accounted for in the expected output.
     *
     * @param checkpointFile Reference to the remaining expected output lines.
     * @return The number of (non-empty) lines that were not accounted for in the validation.
     */
    private int countMissingLines(Scanner checkpointFile) {
        int missingLines = 0;
        while(checkpointFile.hasNextLine()) {
            String line = checkpointFile.nextLine().trim();
            if(!line.isEmpty()) {
                missingLines++;
                System.out.println("Missing output line:\n\u001B[31m"+line+"\u001B[0m");
            }
        }
        return missingLines;
    }

    /**
     * Iterates over the input file for the current test case and prints all lines.
     */
    private void printTestInput() {
        Scanner inputFileScanner;
        try {
            inputFileScanner = new Scanner(new File(inputValidationPath));
        } catch (FileNotFoundException e) {
            return;
        }
        System.out.println("Input:");
        while (inputFileScanner.hasNextLine()) {
            System.out.println(inputFileScanner.nextLine());
        }
        System.out.println("End of input.");
    }

    /**
     * Terminates the program if there is no file to open, or if the file fails to open.
     * Gets a file input stream ready to use from the input file for the current test case.
     *
     * @return An opened file stream to the input file for the current test case.
     */
    private FileInputStream getTestInput() {
        if(!Files.exists(Path.of(inputValidationPath))) {
            System.setOut(defaultConsole);
            System.out.println("\u001B[31mError! No test input found at: " + inputValidationPath);
            System.out.println("Terminating validation...\u001B[0m");
            System.exit(0);
        }
        try {
            return new FileInputStream(inputValidationPath);
        } catch (FileNotFoundException e) {
            System.setOut(defaultConsole);
            System.out.println("\u001B[31mError! Failed to open: " + inputValidationPath);
            System.out.println("Terminating validation...\u001B[0m");
            System.exit(0);
            return null;
        }
    }

    /**
     * Gets a list of all files in the test case directory and iterates over them to check which are valid
     * to include as test cases for the specified checkpoint. If none are found, or the directory is missing,
     * the result will return empty and a warning is shown.
     *
     * @param checkpointNumber The number of the checkpoint to get all test cases for.
     * @param includePartial When true, test cases returned include ones that would otherwise be invalid for only including an unpaired .in or .out.
     * @return A list of test cases that are valid for the specified checkpoint.
     */
    public static List<Integer> getAllValidTestCases(int checkpointNumber, boolean includePartial) {
        List<Integer> result = new ArrayList<>();
        if(!Files.isDirectory(Path.of(TEST_CASE_DIRECTORY.substring(0,TEST_CASE_DIRECTORY.length()-1)))) {
            System.out.println("\u001B[31mWarning! Test case directory missing.\u001B[0m");
            return result;
        }
        File testCaseDirectory = new File(TEST_CASE_DIRECTORY);
        String[] testCaseFileNames = testCaseDirectory.list();
        if(testCaseFileNames == null) {
            System.out.println("\u001B[31mWarning! Test case directory missing.\u001B[0m");
            return result;
        }

        for(String testCaseFileName : testCaseFileNames) {
            if(testCaseFileName.startsWith(checkpointNumber+"_")) {
                validateTestCaseFileName(testCaseFileName, checkpointNumber, result, includePartial);
            }
        }
        if(result.isEmpty()) {
            System.out.println("\u001B[31mWarning! No test cases found for checkpoint "
                                + checkpointNumber + ".\u001B[0m");
        }
        return result;
    }

    /**
     * Validates a supplied file name to verify that it has a matching .in or .out file. This can be used to
     * remove test cases from the pool that do not meet minimum requirements to execute a test.
     * Warnings are shown for partial test cases to make the user aware some are not valid.
     *
     * @param fileName File name to validate.
     * @param checkpointNumber Number of the checkpoint that the file needs to match for validation.
     * @param resultList Reference to the list of valid test cases.
     * @param includePartial When true, validation is less strict. Files with only input or only output are all included.
     */
    private static void validateTestCaseFileName(String fileName, int checkpointNumber, List<Integer> resultList, boolean includePartial) {
        boolean isInputFile = fileName.endsWith(".in");
        String testCaseNumberStr = fileName.replace(checkpointNumber+"_","")
                .replace(".out","").replace(".in","");
        int testCaseNumber;
        try {
            testCaseNumber = Integer.parseInt(testCaseNumberStr);
        } catch(NumberFormatException e) {
            System.out.println("\u001B[31mWarning! Test case "+fileName+" for checkpoint "
                    + checkpointNumber + " is not a valid test case name.\u001B[0m");
            return;
        }
        String assumedPairFile = TEST_CASE_DIRECTORY + checkpointNumber+"_"+testCaseNumber
                + ((isInputFile) ? ".out" : ".in");
        // Only add to the results if the file being checked is an output file
        // and it is either test 1 or any other number with a matching input file paired with it.
        if(!isInputFile && (testCaseNumber == 1 || Files.exists(Path.of(assumedPairFile)))) {
            resultList.add(testCaseNumber);
        } else if(!isInputFile || !Files.exists(Path.of(assumedPairFile))){
            // Show an error message if the file was an output file and was not added above,
            // or if the output file is missing to match this input file.
            System.out.println("\u001B[31mWarning! Test case "+testCaseNumber+" for checkpoint "
                    + checkpointNumber + " is missing a required "+
                    ((isInputFile) ? "output" : "input") +" file.\u001B[0m");
            if(includePartial) {
                resultList.add(testCaseNumber);
            }
        }
    }

    /**
     * Based on code from: https://gist.github.com/phaniram/6233013
     * Attempts to download the file PracticalXXXTests.zip from the TEST_CASE_ZIP_URL.
     * If the file already exists the download is aborted. Otherwise the download is
     * attempted and returns success upon completion.
     *
     * @param practicalNumber The number of the practical file to download.
     * @return True if either the file already exists and doesn't need downloading, or if the file was successfully downloaded.
     */
    private static boolean downloadTestCases(int practicalNumber) {
        String outputFile = "Practical" + practicalNumber + "Tests.zip";
        if(Files.exists(Path.of(outputFile))) {
            System.out.println("Tests Files Already Downloaded. Skipping Download.");
            System.out.println("Delete " + outputFile + " to re-download.");
            return true;
        }
        try {
            System.out.println("Downloading Test Cases for Practical " + practicalNumber);
            long startTime = System.currentTimeMillis();

            URL url = new URL(TEST_CASE_ZIP_URL + practicalNumber + ".zip");

            url.openConnection();
            InputStream reader = url.openStream();

            FileOutputStream writer = new FileOutputStream(outputFile);
            byte[] buffer = new byte[102400];
            int readBytes;
            while ((readBytes = reader.read(buffer)) > 0) {
                writer.write(buffer, 0, readBytes);
                buffer = new byte[102400];
            }

            long endTime = System.currentTimeMillis();

            System.out.println("Test cases downloaded in: " + (endTime - startTime) + " milliseconds");
            writer.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Opens a file named PracticalXXXTests.zip where XXX is the practicalNumber.
     * All the contents are extracted into the TestCases folder (it is created if it doesn't exist).
     * The method then logs output to show what was newly extracted, and what
     * files were overwritten during extraction.
     *
     * @param practicalNumber The practical number to identify which zip file to extract.
     */
    private static void unZIPTestCases(int practicalNumber) {
        String zipFileName = "Practical" + practicalNumber + "Tests.zip";
        List<String> extractedNewTests = new ArrayList<>();
        List<String> extractedOverwrittenTests = new ArrayList<>();
        try {
            ZipFile zipFile = new ZipFile(zipFileName);
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

            // Create the test case directory if it does not exist
            if(!Files.isDirectory(Path.of(TEST_CASE_DIRECTORY.substring(0,TEST_CASE_DIRECTORY.length()-1)))) {
                Files.createDirectory(Path.of(TEST_CASE_DIRECTORY.substring(0,TEST_CASE_DIRECTORY.length()-1)));
            }
            while (zipEntries.hasMoreElements()) {
                ZipEntry zipEntry = zipEntries.nextElement();
                if (zipEntry.isDirectory()) {
                    // Should not be creating directories, but just in case future changes require this.
                    Files.createDirectory(Path.of(TEST_CASE_DIRECTORY.substring(0,TEST_CASE_DIRECTORY.length()-2)
                                                + zipEntry.getName()));
                    continue;
                }

                if(Files.exists(Path.of(TEST_CASE_DIRECTORY + zipEntry.getName()))) {
                    extractedOverwrittenTests.add(zipEntry.getName());
                } else {
                    extractedNewTests.add(zipEntry.getName());
                }
                // Create the file if it doesn't exist.
                new File(TEST_CASE_DIRECTORY + "\\"+ zipEntry.getName());
                copyInputStream(zipFile.getInputStream(zipEntry),
                        new BufferedOutputStream(new FileOutputStream(TEST_CASE_DIRECTORY + zipEntry.getName())));
            }

            zipFile.close();
        } catch (IOException ioe) {
            System.err.println("Unhandled exception:");
            ioe.printStackTrace();
        }
        if(!extractedNewTests.isEmpty()) {
            System.out.println("Extracted new test files: " + Arrays.toString(extractedNewTests.toArray()));
        }
        if(!extractedOverwrittenTests.isEmpty()) {
            System.out.println("Extracted and overwrote test files: " + Arrays.toString(extractedOverwrittenTests.toArray()));
        }
    }

    /**
     * Copies all the data from in into out and closes the streams.
     *
     * @param in Input from some input source to be saved into the output stream.
     * @param out Output source to store all the data from the input stream.
     * @throws IOException When failing to read/write content or failing to close either stream.
     */
    private static void copyInputStream(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.close();
    }
}
