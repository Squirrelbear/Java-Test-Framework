import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SimpleClassValidator {
    public static void main(String[] args) {
        new SimpleClassValidator("45.classtest");
    }

    private String currentClassPath = "ERROR_CLASS_PATH_NOT_SET!";

    public SimpleClassValidator(String filePath) {
        boolean success = validateFromFile(filePath);
        if(success) {
            System.out.println("Successfully validated class/method structure requirements.");
        }
    }

    private boolean validateFromFile(String filePath) {
        Scanner scan;
        try {
            scan = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open validation file: " + filePath);
            return false;
        }

        while(scan.hasNext()) {
            String[] lineData = scan.nextLine().split(" ");
            boolean success = isTestValid(lineData);
            if(!success) {
                if(scan.hasNext()) {
                    System.out.println("There were more test cases that were not checked until you fix the above issue.");
                }
                return false;
            }
        }
        return true;
    }

    private boolean isTestValid(String[] lineCommandData) {
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

        System.out.println("Malformed test attempted:" + Arrays.toString(lineCommandData));
        return false;
    }

    private boolean classExists(String classPath) {
        try {
            Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found: " + classPath
                    + "\nMake sure the class has the correct name and is in the correct location.");
            return false;
        }
        return true;
    }

    private boolean hasMethod(String classPath, String methodName, String expectedReturnType, String ... paramTypes) {
        Class<?> classRef;
        try {
            classRef = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found: " + classPath
                    + "\nMake sure the class has the correct name and is in the correct location.");
            return false;
        }

        if(paramTypes.length > 0) {
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
                    System.out.println("A problem was encountered preparing to test for a method with parameters in "
                            + classPath + " with method name " + methodName);
                    System.out.println(e.getMessage());
                    return false;
                }
            }

            String returnType;
            try {
                classRef.getMethod(methodName, paramClasses);
                returnType = classRef.getMethod(methodName, paramClasses).getReturnType().getName();
            } catch (NoSuchMethodException e) {
                System.out.println("Failed to find a method called "+ methodName +" with specific parameters for: " + classPath);
                System.out.print("Was expecting to the following parameter types: ");
                for(String param : paramTypes) {
                    System.out.print(param + " ");
                }
                System.out.println();
                return false;
            }

            if(expectedReturnType.equals("String")) {
                expectedReturnType = String.class.getName();
            }

            if(!returnType.equals(expectedReturnType)) {
                System.out.println("Expected return type for the method " + methodName + " was " + expectedReturnType
                        + " but the return type found was: " + returnType);
                return false;
            }
        } else {
            String returnType;
            try {
                classRef.getMethod(methodName);
                returnType = classRef.getMethod(methodName).getReturnType().getName();
            } catch (NoSuchMethodException e) {
                System.out.println("Failed to find a method called "+ methodName
                        +" with specific parameters for: " + classPath);
                System.out.println("Expected to find the method with no parameters.");
                return false;
            }

            if(expectedReturnType.equals("String")) {
                expectedReturnType = String.class.getName();
            }

            if(!returnType.equals(expectedReturnType)) {
                System.out.println("Expected return type for the method " + methodName + " was " + expectedReturnType
                        + " but the return type found was: " + returnType);
                return false;
            }
        }

        return true;
    }

    private boolean hasConstructor(String classPath, String ... paramTypes) {
        Class<?> classRef;
        try {
            classRef = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found: " + classPath
                    + "\nMake sure the class has the correct name and is in the correct location.");
            return false;
        }

        if(paramTypes.length > 0) {
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
                    System.out.println("A problem was encountered preparing to test for a constructor with parameters in " + classPath);
                    System.out.println(e.getMessage());
                    return false;
                }
            }

            try {
                classRef.getConstructor(paramClasses);
            } catch (NoSuchMethodException e) {
                System.out.println("Failed to find a constructor with specific parameters for: " + classPath);
                System.out.print("Was expecting to the following parameter types: ");
                for(String param : paramTypes) {
                    System.out.print(param + " ");
                }
                System.out.println();
                return false;
            }
        } else {
            try {
                classRef.getConstructor();
            } catch (NoSuchMethodException e) {
                System.out.println("Failed to find default constructor for: " + classPath
                        + "\nMake sure you include a default constructor for this task.");
                return false;
            }
        }

        return true;
    }

    private boolean noPublicVariables(String classPath) {
        Class<?> classRef;
        try {
            classRef = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found: " + classPath
                    + "\nMake sure the class has the correct name and is in the correct location.");
            return false;
        }

        Field[] fields = classRef.getDeclaredFields();
        for(Field field : fields) {
            if(!Modifier.isPrivate(field.getModifiers())) {
                System.out.println("Your variable " + field.getName() + " is public. You should have set your variables to all be private.");
                return false;
            }
        }
        return true;
    }

    private boolean onlySpecificVariables(String classPath, String[] variableNames) {
        Class<?> classRef;
        try {
            classRef = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found: " + classPath
                    + "\nMake sure the class has the correct name and is in the correct location.");
            return false;
        }
        List<String> unusedVariableNames = new LinkedList<>(Arrays.asList(variableNames.clone()));

        Field[] fields = classRef.getDeclaredFields();
        for(Field field : fields) {
            if(unusedVariableNames.contains(field.getName())) {
                unusedVariableNames.remove(field.getName());
            } else {
                System.out.println("You have an extra variable defined that should not exist.");
                System.out.println("Verify that your variable called \""+field.getName()+"\" is actually required for this practical.");
                return false;
            }
        }
        if(!unusedVariableNames.isEmpty()) {
            System.out.println("You did not have all the required variables defined in your class.");
            System.out.println("You were missing the following expected variables: " + Arrays.toString(unusedVariableNames.toArray()));
            return false;
        }

        return true;
    }

    private boolean hasSpecificVariable(String classPath, String variableName, String visibilityModifier, String variableType) {
        Class<?> classRef;
        try {
            classRef = Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            System.out.println("Class was not found: " + classPath
                    + "\nMake sure the class has the correct name and is in the correct location.");
            return false;
        }

        Field field;
        try {
            field = classRef.getDeclaredField(variableName);
        } catch (NoSuchFieldException e) {
            System.out.println("You failed to declare a required variable called: " + variableName);
            return false;
        }

        switch (visibilityModifier) {
            case "ignore":
                break;
            case "private":
                if (!Modifier.isPrivate(field.getModifiers())) {
                    System.out.println("The variable called " + variableName + " should be declared as private!");
                    return false;
                }
                break;
            case "public":
                if (!Modifier.isPublic(field.getModifiers())) {
                    System.out.println("The variable called " + variableName + " should be declared as public!");
                    return false;
                }
                break;
            case "protected":
                if (!Modifier.isProtected(field.getModifiers())) {
                    System.out.println("The variable called " + variableName + " should be declared as protected!");
                    return false;
                }
                break;
            default:
                System.out.println("Malformed test error. Invalid visibility modifier specified for variable: " + variableName + ".");
                return false;
        }

        String actualType = field.getType().getName();
        if(actualType.startsWith("[")) {
            actualType = field.getType().getComponentType().getName() + "[]";
        }
        if(variableType.contains("String")) {
            variableType = variableType.replace("String",String.class.getName());
        }
        if(!actualType.equals(variableType)) {
            System.out.println("The type of your variable called " + variableName + " expected the type \""
                    + variableType + "\", but it was found to have the type \"" + actualType + "\".");
            return false;
        }

        return true;
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
    }
}
