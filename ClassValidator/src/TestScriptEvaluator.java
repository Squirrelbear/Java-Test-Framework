import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;

public class TestScriptEvaluator {

    public static void main(String[] args) {
        TestScriptEvaluator testScriptEvaluator = new TestScriptEvaluator();

    }

    private HashMap<String, ObjectWrapper> storedData;

    public TestScriptEvaluator() {
        storedData = new HashMap<>();
        runAllFile("TestCases/test.commandtest");

        /*ObjectWrapper objectWrapper = new ObjectWrapper("Boat", "\"TestString\"", "\"SomeOtherString\"", "int:55");
        //objectWrapper.classRef = Boat.class;
        //objectWrapper.value = new Boat();
        Object resulta = objectWrapper.callMethodOnObject("getName");
        System.out.println(resulta);
        Object callResult = objectWrapper.callMethodOnObject("setName", "Wookie");
        Object result = objectWrapper.callMethodOnObject("getName");
        System.out.println(result);
        Object resultz = objectWrapper.getValueInField("bClass");
        System.out.println(resultz);

        executeCommand("SET myBoat Boat.constructor()");
        executeCommand("GET myBoat.getName()");
        executeCommand("SET myBoat Boat.constructor(\"Sample\",\"Other\",int:121)");
        executeCommand("GET myBoat.name");
        executeCommand("GET myBoat.bClass");
        executeCommand("GET myBoat.regNum");*/

//        executeCommand("SET myList IntList.constructor()");
//        executeCommand("RUN myList.insertAtEndofList(int:15)");
//        executeCommand("RUN myList.insertAtEndofList(int:23)");
//        executeCommand("RUN myList.insertAtEndofList(int:19)");
//        executeCommand("RUN myList.printList()");
//        executeCommand("ASSERT myList.getElementAt(int:2) == int:15");
//        executeCommand("ASSERT myList.getElementAt(int:0) == int:15");
//        executeCommand("ASSERT myList == int:15");
//        executeCommand("SET myList2 IntList.constructor()");
//        executeCommand("ASSERT myList != myList2");
    }

    public boolean runAllFile(String filePath) {
        Scanner scan = null;
        try {
            scan = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            System.out.println("Error! File was not found at: " + filePath);
            return false;
        }
        boolean result = true;
        while(scan.hasNextLine()) {
             if(!executeCommand(scan.nextLine())) {
                 result = false;
             }
        }
        return result;
    }

    public boolean executeCommand(String command) {
        String[] splitData = command.split(" ");
        switch(splitData[0]) {
            case "SET" -> setStoredData(splitData[1], variableToObject(splitData[2]));
            case "RUN" -> variableToObject(splitData[1]);
            case "PRINT" -> System.out.println(variableToObject(splitData[1]));
            case "ASSERT" -> {return assertCompare(splitData[2], variableToObject(splitData[1]), variableToObject(splitData[3]));}
            default -> System.out.println("Error command not defined.");
        }
        return true;
    }

    public boolean assertCompare(String comparison, Object value1, Object value2) {
        boolean result = switch(comparison) {
            case "==" -> value1.equals(value2);
            case "!=" -> !value1.equals(value2);
            case ">" -> compareLess(value2, value1);
            case "<" -> compareLess(value1, value2);
            case ">=" -> !compareLess(value1, value2);
            case "<=" -> !compareLess(value2, value1);
            default -> false;
        };
        System.out.println("Assert "+(result?"success":"failed")+": "
                            + value1.toString() + " " + comparison + " " + value2.toString());
        return true;
    }

    private boolean compareLess(Object value1, Object value2) {
        if(value1.getClass() == Integer.class && value2.getClass() == Integer.class) {
            return (Integer)value1 < (Integer)value2;
        } else if(value1.getClass() == Double.class && value2.getClass() == Double.class) {
            return (Double)value1 < (Double)value2;
        } else if(value1.getClass() == Character.class && value2.getClass() == Character.class) {
            return (Character)value1 < (Character)value2;
        } else {
            System.out.println("Unsupported comparison between objects of type "
                                + value1.getClass() + " and " + value2.getClass());
            return false;
        }
    }

    public Object getStoredData(String elementName) {
        if(!storedData.containsKey(elementName)) {
            System.out.println("Failed to access stored data named: " + elementName);
            return null;
        }

        return storedData.get(elementName).value;
    }

    public void setStoredData(String elementName, ObjectWrapper value) {
        storedData.put(elementName, value);
    }

    public void setStoredData(String elementName, Object value) {
        setStoredData(elementName, new ObjectWrapper(value));
    }

    private class ObjectWrapper {
        public Class<?> classRef;
        public Object value;

        public ObjectWrapper() {

        }

        public ObjectWrapper(Object object) {
            classRef = object.getClass();
            value = object;
        }

        public ObjectWrapper(String className, String ... paramList) {
            this(getClassForName(className), variableListToObjects(paramList));
        }

        public ObjectWrapper(Class<?> classRef, Object ... params) {
            this.classRef = classRef;
            Constructor<?> constructor = null;
            try {
                constructor = classRef.getConstructor(castObjectListToClassList(params));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                value = constructor.newInstance(params);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public Object callMethodOnObject(String methodName, Object ... params) {
            Method method;
            try {
                method = classRef.getMethod(methodName, castObjectListToClassList(params));
            } catch (NoSuchMethodException e) {
                System.out.println("Failed to find Method: " + methodName);
                return null;
            }
            Object result;
            try {
                result = method.invoke(value, params);
            } catch (IllegalAccessException e) {
                System.out.println("Failed to execute method: " + methodName);
                return null;
            } catch (InvocationTargetException e) {
                System.out.println("Failed to execute method: " + methodName);
                return null;
            }
            return result;
        }

        public Object getValueInField(String fieldName) {
            Field field = null;
            try {
                field = classRef.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            Object result = null;
            try {
                result = field.get(value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
    public Class<?>[] castObjectListToClassList(Object ... objects) {
        Class<?>[] result = new Class<?>[objects.length];
        for(int i = 0; i < objects.length; i++) {
            result[i] = objects[i].getClass();
            if(result[i].getName().contains("Integer")) {
                result[i] = int.class;
            }
        }
        return result;
    }

    public Class<?> getClassForName(String className) {
        Class<?> result = null;
        try {
            result = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Object[] variableListToObjects(String ... variableDefinitions) {
        Object[] result = new Object[variableDefinitions.length];
        for(int i = 0 ; i < variableDefinitions.length; i++) {
            result[i] = variableToObject(variableDefinitions[i]);
        }
        return result;
    }

    public Object variableToObject(String variableDefinition) {
        if(variableDefinition.startsWith("\"")) {
            return variableDefinition.substring(1,variableDefinition.length()-1);
        } else if(variableDefinition.startsWith("int:")) {
            return Integer.parseInt(variableDefinition.substring(4));
        } else if(variableDefinition.startsWith("double:")) {
            return Double.parseDouble(variableDefinition.substring(7));
        } else if(variableDefinition.startsWith("char:")) {
            return variableDefinition.charAt(variableDefinition.length()-1);
        } else {
            if(!variableDefinition.contains(".")) {
                return getStoredData(variableDefinition);
            } else {
                String variableName = variableDefinition.substring(0,variableDefinition.indexOf("."));
                String remainder = variableDefinition.substring(variableDefinition.indexOf(".")+1);
                if(remainder.contains("(")) {
                    String methodName = remainder.substring(0,remainder.indexOf("("));
                    String parameters = remainder.substring(methodName.length()+1, remainder.length()-1);
                    String[] parameterList = parameters.length() > 0 ? parameters.split(",") : new String[0];
                    if(methodName.equals("constructor")) {
                        return new ObjectWrapper(variableName, parameterList);
                    } else {
                        ObjectWrapper objectWrapper = (ObjectWrapper) getStoredData(variableName);
                        return objectWrapper.callMethodOnObject(methodName, variableListToObjects(parameterList));
                    }
                } else {
                    ObjectWrapper objectWrapper = (ObjectWrapper) getStoredData(variableName);
                    return objectWrapper.getValueInField(remainder);
                }
            }
        }
    }
}
