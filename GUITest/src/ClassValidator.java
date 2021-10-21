import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ClassValidator {
    public static void main(String[] args) {
        for(int i = 20; i <= 45; i+=5) {
            System.out.println("Checkpoint number " + i);
            new ClassValidator(i, true);
        }
        System.out.println();
        System.out.println();
        for(int i = 20; i <= 45; i+=5) {
            System.out.println("Checkpoint number " + i);
            new ClassValidator(i, false);
        }

        System.out.println();
        System.out.println();
        new ClassValidator(21, false);
    }

    private String currentClassPath = "ERROR_CLASS_PATH_NOT_SET!";

    public ClassValidator(int checkpointNumber, boolean showAllTests) {
        boolean success = validateFromFile("TestCases/"+checkpointNumber+".classtest", showAllTests);
        if(success) {
            System.out.println("\u001B[32mSuccessfully validated class/method structure requirements.\u001B[0m");
        } else {
            System.out.println("\u001B[31mReview the failed test case(s) and correct the issues with your code.\u001B[0m");
        }
    }

    private boolean validateFromFile(String filePath, boolean showAll) {
        Scanner scan;
        try {
            scan = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open validation file: " + filePath);
            return false;
        }

        int testNumber = 1;
        boolean anyFailed = false;
        while(scan.hasNext()) {
            String[] lineData = scan.nextLine().split(" ");
            TestResult testResult = isTestValid(lineData);
            if(showAll || !testResult.passed) {
                System.out.println("Test #" + testNumber + ". " + testResult.testCase + " "
                        + (testResult.passed ? "\u001B[32mPASSED\u001B[0m" : "\u001B[31mFAILED\u001B[0m") + " "
                        + (testResult.resultMessage.length() > 0 ? "Reason: " + testResult.resultMessage : ""));
            }
            if(!testResult.passed && !showAll) {
                if(scan.hasNext()) {
                    System.out.println("There were more test cases that were not checked until you fix the above issue.");
                }
                return false;
            }
            if(!testResult.passed) {
                anyFailed = true;
            }
            testNumber++;
        }
        return !anyFailed;
    }

    private TestResult isTestValid(String[] lineCommandData) {
        switch (lineCommandData[0]) {
            case "class":
                currentClassPath = lineCommandData[1];
                return classExists(currentClassPath);
            case "constructor": {
                String[] params = new String[lineCommandData.length - 1];
                System.arraycopy(lineCommandData, 1, params, 0, params.length);
                return hasConstructor(currentClassPath, params);
            }
            case "defaultconstructor":
                return hasConstructor(currentClassPath);
            case "method": {
                String methodName = lineCommandData[1];
                String expectedReturnType = lineCommandData[2];
                String[] params = new String[lineCommandData.length - 3];
                System.arraycopy(lineCommandData, 3, params, 0, params.length);
                return hasMethod(currentClassPath, methodName, expectedReturnType, params);
            }
            case "nopublicvariables":
                return noPublicVariables(currentClassPath);
            case "onlyvariables":
                String[] variableNames = new String[lineCommandData.length - 1];
                System.arraycopy(lineCommandData, 1, variableNames, 0, variableNames.length);
                return onlySpecificVariables(currentClassPath, variableNames);
            case "variable":
                return hasSpecificVariable(currentClassPath, lineCommandData[1], lineCommandData[2], lineCommandData[3]);
        }

        TestResult testResult = new TestResult("Malformed test attempted");
        testResult.resultMessage = Arrays.toString(lineCommandData);
        testResult.passed = false;
        return testResult;
    }

    private TestResult classExists(String classPath) {
        TestResult result = new TestResult("Class \"" + classPath + "\" Exists");
        getClass(classPath, result);
        return result;
    }

    private Class<?> getClass(String classPath, TestResult result) {
        Class<?> loadedClass = null;
        try {
            loadedClass = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            result.resultMessage = "Class \""+classPath+"\" was not found. Make sure the class has the correct name and is in the correct location.";
            result.passed = false;
        }
        return loadedClass;
    }

    private Class<?>[] convertStringArrayToClassArray(String classPath, String methodName, String[] paramTypes, TestResult testResult) {
        Class<?>[] paramClasses = new Class[paramTypes.length];
        for(int i = 0; i < paramClasses.length; i++) {
            try {
                if(paramTypes[i].equals("int")) {
                    paramClasses[i] = int.class;
                } else if(paramTypes[i].equals("String")) {
                    paramClasses[i] = String.class;
                } else {
                    paramClasses[i] = Class.forName(paramTypes[i]);
                }
            } catch (ClassNotFoundException e) {
                testResult.resultMessage = "A problem was encountered preparing to test for a method with parameters in "
                        + classPath + " with method name " + methodName + " Message: " + e.getMessage();
                testResult.passed = false;
                return paramClasses;
            }
        }
        return paramClasses;
    }

    private String getReturnType(Class<?> classRef, String methodName, Class<?>[] paramClasses, String[] paramTypes, TestResult result) {
        String returnType = "INVALID";
        try {
            returnType = classRef.getMethod(methodName, paramClasses).getReturnType().getName();
        } catch (NoSuchMethodException e) {
            result.resultMessage = "Failed to find a method called "+ methodName +" with specific parameter types: ";
            if(paramTypes.length == 0) {
                result.resultMessage += "No parameters";
            } else {
                for (String param : paramTypes) {
                    System.out.print(param + " ");
                }
            }
            result.passed = false;
        }
        return returnType;
    }

    private TestResult hasMethod(String classPath, String methodName, String expectedReturnType, String ... paramTypes) {
        TestResult result = new TestResult("Method ", expectedReturnType, methodName, paramTypes, "Exists");
        Class<?> classRef = getClass(classPath, result);
        if(!result.passed) {
            return result;
        }

        Class<?>[] paramClasses = convertStringArrayToClassArray(classPath, methodName, paramTypes, result);
        if(!result.passed) {
            return result;
        }
        String returnType = getReturnType(classRef, methodName, paramClasses, paramTypes, result);
        if(!result.passed) {
            return result;
        }

        if(expectedReturnType.equals("String")) {
            expectedReturnType = String.class.getName();
        }

        if(!returnType.equals(expectedReturnType)) {
            result.resultMessage = "Expected return type for the method " + methodName + " was " + expectedReturnType
                    + " but the return type found was: " + returnType;
            result.passed = false;
        }
        return result;
    }

    private TestResult hasValidConstructor(String classPath, Class<?> classRef, String[] paramTypes, TestResult result) {
        Class<?>[] paramClasses = convertStringArrayToClassArray(classPath, "Constructor", paramTypes, result);

        try {
            classRef.getConstructor(paramClasses);
        } catch (NoSuchMethodException e) {
            if(paramTypes.length > 0) {
                result.resultMessage = "Failed to find a constructor with specific parameters for \"" + classPath + "\" with types: ";
                for(String param : paramTypes) {
                    result.resultMessage += param + " ";
                }
            } else {
                result.resultMessage = "Failed to find default constructor for \"" + classPath
                        + "\". Make sure you include a default constructor for this task.";
            }
            result.passed = false;
        }

        return result;
    }

    private TestResult hasConstructor(String classPath, String ... paramTypes) {
        TestResult result = new TestResult("Constructor", "", classPath, paramTypes, " Exists");

        Class<?> classRef = getClass(classPath, result);
        if(!result.passed) {
            return result;
        }

        return hasValidConstructor(classPath, classRef, paramTypes, result);
    }

    private TestResult noPublicVariables(String classPath) {
        TestResult result = new TestResult("No Public Variables in " + classPath);
        Class<?> classRef = getClass(classPath, result);
        if(!result.passed) {
            return result;
        }

        Field[] fields = classRef.getDeclaredFields();
        for(Field field : fields) {
            if(!Modifier.isPrivate(field.getModifiers())) {
                result.resultMessage = "Your variable " + field.getName() + " is public. You should have set your variables to all be private.";
                result.passed = false;
                return result;
            }
        }
        return result;
    }

    private TestResult onlySpecificVariables(String classPath, String[] variableNames) {
        TestResult result = new TestResult("Only Specific Variables Allowed");
        Class<?> classRef = getClass(classPath, result);
        if(!result.passed) {
            return result;
        }
        List<String> unusedVariableNames = new LinkedList<>(Arrays.asList(variableNames.clone()));

        Field[] fields = classRef.getDeclaredFields();
        for(Field field : fields) {
            if(unusedVariableNames.contains(field.getName())) {
                unusedVariableNames.remove(field.getName());
            } else {
                result.resultMessage = "You have an extra variable defined that should not exist. ";
                result.resultMessage += "Verify that your variable called \""+field.getName()+"\" is actually required for this practical.";
                result.passed = false;
                return result;
            }
        }
        if(!unusedVariableNames.isEmpty()) {
            result.resultMessage = "You did not have all the required variables defined in your class. ";
            result.resultMessage += "You were missing the following expected variables: " + Arrays.toString(unusedVariableNames.toArray());
            result.passed = false;
        }

        return result;
    }

    private boolean hasExpectedVisibilityModifier(String variableName, Field field, String visibilityModifier, TestResult result) {
        switch (visibilityModifier) {
            case "ignore":
                break;
            case "private":
                if (!Modifier.isPrivate(field.getModifiers())) {
                    result.resultMessage = "The variable called " + variableName + " should be declared as private!";
                    result.passed = false;
                }
                break;
            case "public":
                if (!Modifier.isPublic(field.getModifiers())) {
                    result.resultMessage = "The variable called " + variableName + " should be declared as public!";
                    result.passed = false;
                }
                break;
            case "protected":
                if (!Modifier.isProtected(field.getModifiers())) {
                    result.resultMessage = "The variable called " + variableName + " should be declared as protected!";
                    result.passed = false;
                }
                break;
            default:
                result.resultMessage = "Malformed test error. Invalid visibility modifier specified for variable: " + variableName + ".";
                result.passed = false;
        }
        return result.passed;
    }

    private TestResult hasSpecificVariable(String classPath, String variableName, String visibilityModifier, String variableType) {
        TestResult result = new TestResult("Variable \""
                + (!visibilityModifier.equals("ignore") ? visibilityModifier + " " : "")
                + variableType + " " + variableName + ";\" Exists");
        Class<?> classRef = getClass(classPath, result);

        if(!result.passed) {
            return result;
        }

        Field field;
        try {
            field = classRef.getDeclaredField(variableName);
        } catch (NoSuchFieldException e) {
            result.resultMessage ="You failed to declare a required variable called: " + variableName;
            result.passed = false;
            return result;
        }

        if(!hasExpectedVisibilityModifier(variableName, field, visibilityModifier, result)) {
            return result;
        }

        String actualType = field.getType().getName();
        if(actualType.startsWith("[")) {
            actualType = field.getType().getComponentType().getName() + "[]";
        }
        if(variableType.contains("String")) {
            variableType = variableType.replace("String",String.class.getName());
        }
        if(!actualType.equals(variableType)) {
            result.resultMessage = "The type of your variable called " + variableName + " expected the type \""
                    + variableType + "\", but it was found to have the type \"" + actualType + "\".";
            result.passed = false;
        }

        return result;
    }

    private class TestResult {
        public String testCase;
        public boolean passed;
        public String resultMessage;

        public TestResult(String testCase) {
            this.testCase = testCase;
            passed = true;
            resultMessage = "";
        }

        public TestResult(String prefix, String returnType, String methodName, String[] paramTypes, String postfix) {
            testCase = prefix + " ";
            if(returnType.length() > 0) {
                testCase += returnType + " ";
            }
            testCase += methodName + "(";
            for(int i = 0; i < paramTypes.length; i++) {
                testCase += paramTypes[i];
                if(i < paramTypes.length - 1) {
                    testCase += ", ";
                }
            }
            testCase += ") " + postfix;
            passed = true;
            resultMessage = "";
        }
    }
}
