# Java Test Framework

Source code I developed to provide a simple automated test system for university students. The code provided has example test cases that are only testable with completion of practicals at Flinders University. No code has been provided to test against to prevent students plagiarising example solutions. It is easy to develop custom test cases using any of the provided systems though. 

Three separate folders are provided with some work in progress code, and some that is immediately functional.

* TestHarness Folder: This folder contains both a creation utility [PracticalEvaluatorUtility.java](/TestHarness/src/TestHarness/PracticalEvaluatorUtility.java) and the file that could be included in student projects [PracticalEvaluator.java](/TestHarness/src/TestHarness/PracticalEvaluator.java) to test checkpoints. Files are documented with comments to describe use. And will be [described further below](#TestHarness).

* ClassValidator: This contains prototype code that takes the concepts used in TestHarness further by providing a framework for testing using either a [ClassValidator.java](ClassValidator/src/ClassValidator.java) that can be used to define a set of automated tests the validate correct class construction based on a ruleset. The approach used was also extended to provide a simple scripting system that can be parsed for assertion of arbitrary code executed through reflection as can be found in [TestScriptEvaluator.java](ClassValidator/src/TestScriptEvaluator.java).

* GUITest: Represents initial stages of developing the automated testing into a GUI.

TestHarnessOverview.pdf shows some earlier documentation demonstrating use of the test harness. It did go through additional iteration after the version demonstrated.

# Test Harness

Test cases are defined in the TestCases folder as part of the main project directory. Each test case has a file with naming following a system of X_Y.in or X_Y.out where X is the checkpoint number and Y is a test for that checkpoint. Providing a ".in" file is optional for test case 1, but required for any additional test cases. This is due to the difference in execution can't realistically be different if there is not input. The ".in" files are parsed in as an input stream that simulates keyboard entry. The output is captured and tested line by line against the expected output in the associated ".out" file. 

The additional utility allows for easy creation of test cases. This can be linked up to automatically generate test output and run test output for everything, but you would need to update the prepareMethodHashSet() method by providing references to all necessary methods.