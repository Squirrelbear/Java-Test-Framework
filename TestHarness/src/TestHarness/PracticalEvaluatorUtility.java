package TestHarness;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * PracticalEvaluatorUtility
 *
 * A simple utility to create and manage test cases for all practicals in the topic Computer Programming 1.
 * The application presents a tree structure to control actions for individual Practicals,
 * Checkpoints, and Test Cases.
 *
 * @author Peter Mitchell
 * @version 2021.1
 */
public class PracticalEvaluatorUtility implements TreeSelectionListener, ActionListener {
    /**
     * Entry point for the PracticalEvaluatorUtility.
     *
     * @param args Not used.
     */
    public static void main(String[] args) {
        new PracticalEvaluatorUtility();
    }

    /**
     * Defines the mapping of checkpoint numbers to functions to allow testing
     * via the generate output button on the test case card, and the running of
     * multiple tests on the Checkpoint and Practical cards.
     */
    private void prepareMethodHashSet() {
        checkpointToFunctionHashTable = new HashMap<>();
/*
        // Practical 1
        checkpointToFunctionHashTable.put(2,()-> Prac1.HelloWorld.main(new String[0]));
        checkpointToFunctionHashTable.put(3,()-> Prac1.SyntaxError.main(new String[0]));
        checkpointToFunctionHashTable.put(4,()-> Prac1.SemanticError.main(new String[0]));
        checkpointToFunctionHashTable.put(5,()-> Prac1.First.main(new String[0]));

        // Practical 2
        checkpointToFunctionHashTable.put(7,()-> Prac2.Assignment.main(new String[0]));
        checkpointToFunctionHashTable.put(8,()-> Prac2.Echo.main(new String[0]));
        checkpointToFunctionHashTable.put(9,()-> Prac2.Number.main(new String[0]));
        checkpointToFunctionHashTable.put(10,()-> Prac2.Division.main(new String[0]));
        checkpointToFunctionHashTable.put(201,()-> Prac2.Prac2Extension.main(new String[0])); // extension

        // Practical 3
        checkpointToFunctionHashTable.put(12,()-> Prac3.Ticket.main(new String[0]));
        checkpointToFunctionHashTable.put(13,()-> Prac3.NumberEvaluation.main(new String[0]));
        checkpointToFunctionHashTable.put(14,()-> Prac3.CalculateBMI.main(new String[0]));
        checkpointToFunctionHashTable.put(15,()-> Prac3.Biscuits.main(new String[0]));
        checkpointToFunctionHashTable.put(301,()-> Prac3.Prac3Extension.main(new String[0])); // extension

        // Practical 4
        checkpointToFunctionHashTable.put(17, Prac4.BoatMaker::runTask1);
        checkpointToFunctionHashTable.put(18, Prac4.BoatMaker::runTask2);
        checkpointToFunctionHashTable.put(19, Prac4.BoatMaker::runTask3);
        checkpointToFunctionHashTable.put(20, Prac4.BoatMaker::runTask4);
        checkpointToFunctionHashTable.put(401, Prac4.BoatMaker::runTask5); // extension

        // Practical 5
        checkpointToFunctionHashTable.put(22, Prac5.FrogProgram::runTask1);
        checkpointToFunctionHashTable.put(23, Prac5.FrogProgram::runTask2);
        checkpointToFunctionHashTable.put(24, Prac5.FrogProgram::runTask3);
        checkpointToFunctionHashTable.put(25, Prac5.FrogProgram::runTask4);
        checkpointToFunctionHashTable.put(501, Prac5.FrogProgram::runTask5); // extension

        // Practical 6
        checkpointToFunctionHashTable.put(27, Prac6.Number::runTask1);
        checkpointToFunctionHashTable.put(28, Prac6.Number::runTask2);
        checkpointToFunctionHashTable.put(29, Prac6.Number::runTask3);
        checkpointToFunctionHashTable.put(30, Prac6.Number::runTask4);
        checkpointToFunctionHashTable.put(601, Prac6.Number::runTask5); // extension

        // Practical 7
        checkpointToFunctionHashTable.put(32, Prac7.ArrayTask::runTask1);
        checkpointToFunctionHashTable.put(33, Prac7.ArrayTask::runTask2);
        checkpointToFunctionHashTable.put(34, Prac7.ArrayTask::runTask3);
        checkpointToFunctionHashTable.put(35, Prac7.ArrayTask::runTask4);
        checkpointToFunctionHashTable.put(701, Prac7.ArrayTask::runTask5); // extension

        // Practical 8
        checkpointToFunctionHashTable.put(37, Prac8.RecordEvents::runTask1);
        checkpointToFunctionHashTable.put(38, Prac8.RecordEvents::runTask2);
        checkpointToFunctionHashTable.put(39, Prac8.RecordEvents::runTask3);
        checkpointToFunctionHashTable.put(40, Prac8.RecordEvents::runTask4);
        checkpointToFunctionHashTable.put(801, Prac8.RecordEvents::runTask5); // extension

        // Practical 9
        checkpointToFunctionHashTable.put(42, Prac9.Task::runTask1);
        checkpointToFunctionHashTable.put(43, Prac9.Task::runTask2);
        checkpointToFunctionHashTable.put(44, Prac9.Task::runTask3);
        checkpointToFunctionHashTable.put(45, Prac9.Task::runTask4);
        checkpointToFunctionHashTable.put(901, Prac9.Task::runTask5); // extension
*/
        // Other Testing
        checkpointToFunctionHashTable.put(999,()-> InputTestExample.main(new String[0]));
    }

    /**
     * Definitions of all the practicals with each element containing:
     * Practical Number, Min Checkpoint Number, Max Checkpoint Number
     * This data is used to generate the tree data structure and to look
     * up checkpoints when dealing with practical wide actions.
     */
    private static final int[][] PRACTICAL_DEFINITION = { {1,2,5}, {2,7,10,201}, {3,12,15,301}, {4,17,20,401},
            {5,22,25,501}, {6,27,30,601}, {7,32,35,701}, {8,37,40,801}, {9,42,45,901}, {999,999,999}};

    /**
     * A map of checkpoint numbers to functions that can be run to generate test output.
     */
    private HashMap<Integer, Runnable> checkpointToFunctionHashTable;

    /**
     * The tree data structure used to represent all available practicals, checkpoints, and tests.
     */
    private JTree menuTree;
    /**
     * The text area for storing input and output file content.
     */
    private JTextArea inputFileTextArea, expectedOutputFileTextArea;
    /**
     * Title labels on the various cards to clearly show the current state.
     */
    private JLabel testCaseTitle, inputAreaTitle, outputAreaTitle, practicalTitle, checkpointTitle;
    /**
     * Buttons to provide the functionality displayed on each of the cards.
     */
    private JButton refreshButton, deleteButton, saveButton, saveAsNewButton, generateOutputButton,
            runAllPracticalTestsButton, packageZIPButton, runAllCheckpointTestsButton,
            regenerateAllTestCasesPracticalButton, regenerateAllTestCasesCheckpointButton,
            deleteAllTestCasesButton;

    /**
     * Represents the currently selected element in the tree (or -1 to indicate a higher current selection).
     */
    private int currentPractical, currentCheckpoint, currentTestCase;
    /**
     * Layout manager used for changing the cardLayoutPanel to show elements based on the tree selections.
     */
    private CardLayout editPanelCardLayout;
    /**
     * The panel used for showing different cards using the editPanelCardLayout.
     */
    private JPanel cardLayoutPanel;

    /**
     * Creates the JFrame and adds components via createComponents() before making it visible.
     */
    public PracticalEvaluatorUtility() {
        // Create the test case directory if it does not exist
        if(!Files.isDirectory(Path.of("TestCases"))) {
            try {
                Files.createDirectory(Path.of("TestCases"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JFrame frame = new JFrame("Practical Evaluator Utility");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(createComponents());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Combines the card layout with a tree scroll pane to create a single JPanel.
     *
     * @return A JPanel containing all the elements to be displayed as part of the JFrame.
     */
    private JPanel createComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(createCardLayout(), BorderLayout.EAST);
        mainPanel.add(createTreeScrollPane(), BorderLayout.WEST);
        prepareMethodHashSet();
        currentPractical = currentCheckpoint = currentTestCase = -1;
        return mainPanel;
    }

    /**
     * Configures the card layout by creating all the cards for the
     * default, practical, checkpoint, and test case cards.
     * Defaults to the default card that shows the title.
     */
    private JPanel createCardLayout() {
        cardLayoutPanel = new JPanel();
        editPanelCardLayout = new CardLayout();
        cardLayoutPanel.setLayout(editPanelCardLayout);
        cardLayoutPanel.add(createDefaultEditPanel(), "Default");
        cardLayoutPanel.add(createPracticalEditPanel(), "Practical");
        cardLayoutPanel.add(createCheckpointEditPanel(), "Checkpoint");
        cardLayoutPanel.add(createTestCaseEditPanel(), "TestCase");
        return cardLayoutPanel;
    }

    /**
     * Generates the JTree via createPracticalTree() and inserts it
     * into a JScrollPane to allow traversal when expanded.
     *
     * @return A JScrollPane with nested JTree.
     */
    private JScrollPane createTreeScrollPane() {
        DefaultMutableTreeNode currentNode = createPracticalTree();
        menuTree = new JTree(currentNode);
        JScrollPane menuTreeScrollPane = new JScrollPane(menuTree);
        menuTreeScrollPane.setPreferredSize(new Dimension(230,400));
        menuTree.addTreeSelectionListener(this);
        menuTree.setRootVisible(true);
        return menuTreeScrollPane;
    }

    /**
     * Creates a panel to be used for showing an individual test case when one is selected.
     * Shows the input and output files associated with the test case and buttons
     * to control what is done with the content.
     *
     * @return A JPanel with options for editing a specific test case.
     */
    private JPanel createTestCaseEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setPreferredSize(new Dimension(550,500));
        inputFileTextArea = new JTextArea(9,50);
        expectedOutputFileTextArea = new JTextArea(9,50);
        JScrollPane inputFileScrollPane = new JScrollPane(inputFileTextArea);
        JScrollPane outputFileScrollPane = new JScrollPane(expectedOutputFileTextArea);
        testCaseTitle = new JLabel("Select a Test Case Node, or Create New Test Case Node");
        testCaseTitle.setPreferredSize(new Dimension(500,30));
        testCaseTitle.setHorizontalAlignment(JLabel.LEFT);
        editPanel.add(testCaseTitle);
        inputAreaTitle = new JLabel("Input for Test Case ");
        inputAreaTitle.setPreferredSize(new Dimension(500,30));
        inputAreaTitle.setHorizontalAlignment(JLabel.LEFT);
        editPanel.add(inputAreaTitle);
        editPanel.add(inputFileScrollPane);
        outputAreaTitle = new JLabel("Output for Test Case ");
        outputAreaTitle.setPreferredSize(new Dimension(500,30));
        outputAreaTitle.setHorizontalAlignment(JLabel.LEFT);
        editPanel.add(outputAreaTitle);
        editPanel.add(outputFileScrollPane);
        generateOutputButton = new JButton("Generate Output");
        generateOutputButton.addActionListener(this);
        editPanel.add(generateOutputButton);
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this);
        editPanel.add(refreshButton);
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        editPanel.add(deleteButton);
        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        editPanel.add(saveButton);
        saveAsNewButton = new JButton("Save As New");
        saveAsNewButton.addActionListener(this);
        editPanel.add(saveAsNewButton);
        return editPanel;
    }

    /**
     * Creates a JPanel ready for when a Practical is selected.
     * Contains a title, a package as ZIP button to zip up the practical content,
     * and a button to run tests for every checkpoint in the practical.
     *
     * @return A JPanel with options for a currently selected Practical.
     */
    private JPanel createPracticalEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setPreferredSize(new Dimension(550,500));
        JLabel emptyFiller = new JLabel("");
        emptyFiller.setPreferredSize(new Dimension(520,150));
        editPanel.add(emptyFiller);
        practicalTitle = new JLabel("Practical Title Here");
        JPanel centredTextArea = new JPanel();
        centredTextArea.setPreferredSize(new Dimension(530,40));
        centredTextArea.add(practicalTitle);
        editPanel.add(centredTextArea);
        packageZIPButton = new JButton("Package as ZIP");
        packageZIPButton.addActionListener(this);
        editPanel.add(packageZIPButton);
        runAllPracticalTestsButton = new JButton("Run All Tests");
        runAllPracticalTestsButton.addActionListener(this);
        editPanel.add(runAllPracticalTestsButton);
        regenerateAllTestCasesPracticalButton = new JButton("Regenerate All Test Outputs");
        regenerateAllTestCasesPracticalButton.addActionListener(this);
        editPanel.add(regenerateAllTestCasesPracticalButton);
        return editPanel;
    }

    /**
     * Creates a JPanel ready for when a Checkpoint has been selected.
     * Showing a title, and a button to run all tests that are part of
     * that Checkpoint.
     *
     * @return A JPanel with options for a currently selected Checkpoint.
     */
    private JPanel createCheckpointEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setPreferredSize(new Dimension(550,500));
        JLabel emptyFiller = new JLabel("");
        emptyFiller.setPreferredSize(new Dimension(520,150));
        editPanel.add(emptyFiller);
        checkpointTitle = new JLabel("Checkpoint Title Here");
        JPanel centredTextArea = new JPanel();
        centredTextArea.setPreferredSize(new Dimension(530,40));
        centredTextArea.add(checkpointTitle);
        editPanel.add(centredTextArea);
        runAllCheckpointTestsButton = new JButton("Run All Tests");
        runAllCheckpointTestsButton.addActionListener(this);
        editPanel.add(runAllCheckpointTestsButton);
        regenerateAllTestCasesCheckpointButton = new JButton("Regenerate All Test Outputs");
        regenerateAllTestCasesCheckpointButton.addActionListener(this);
        editPanel.add(regenerateAllTestCasesCheckpointButton);
        deleteAllTestCasesButton = new JButton("Delete All Test Cases");
        deleteAllTestCasesButton.addActionListener(this);
        editPanel.add(deleteAllTestCasesButton);
        return editPanel;
    }

    /**
     * Creates a default panel to show when the root node is selected.
     * It contains a title, developer information, and basic instruction.
     *
     * @return A JPanel to show an introduction screen.
     */
    private JPanel createDefaultEditPanel() {
        JPanel editPanel = new JPanel();
        editPanel.setPreferredSize(new Dimension(550,500));
        JLabel emptyFiller = new JLabel("");
        emptyFiller.setPreferredSize(new Dimension(520,150));
        editPanel.add(emptyFiller);
        JPanel centredTextArea = new JPanel();
        centredTextArea.setPreferredSize(new Dimension(530,40));
        centredTextArea.add(new JLabel("Practical Evaluator Utility"));
        editPanel.add(centredTextArea);
        centredTextArea = new JPanel();
        centredTextArea.setPreferredSize(new Dimension(300,40));
        centredTextArea.add(new JLabel("Developed by Peter Mitchell (2021)"));
        editPanel.add(centredTextArea);
        editPanel.add(new JLabel("Select Practicals->Checkpoints->Test Cases to start."));
        return editPanel;
    }

    /**
     * Generates a tree of all the available options for every practical with all test cases listed
     * including test cases that are not fully valid and a leaf node for creating new test cases.
     *
     * @return Reference to the Root node of the tree.
     */
    private DefaultMutableTreeNode createPracticalTree() {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Practical Evaluator Test Cases");
        for (int[] singlePracticalDefinition : PRACTICAL_DEFINITION) {
            DefaultMutableTreeNode practicalHeadNode = new DefaultMutableTreeNode("Practical " + singlePracticalDefinition[0]);
            List<Integer> allCheckpointsForPractical = getAllCheckpointsForPractical(singlePracticalDefinition[0]);
            for (int checkpointNumber : allCheckpointsForPractical) {
                DefaultMutableTreeNode checkpointHeadNode = new DefaultMutableTreeNode("Checkpoint " + checkpointNumber);
                List<Integer> validTestCases = PracticalEvaluator.getAllValidTestCases(checkpointNumber, true);
                for (Integer validTestCase : validTestCases) {
                    checkpointHeadNode.add(new DefaultMutableTreeNode("Test Case " + validTestCase));
                }
                checkpointHeadNode.add(new DefaultMutableTreeNode("Create New Test Case"));
                practicalHeadNode.add(checkpointHeadNode);
            }
            rootNode.add(practicalHeadNode);
        }
        return rootNode;
    }

    /**
     * Handles the events when the JTree is interacted with or changed.
     * Checks the current selection and updates the program's view to an
     * appropriate card with relevant detail based on the selection.
     *
     * @param e Not used.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
        if(selectedNode == null || selectedNode.isRoot())  {
            editPanelCardLayout.show(cardLayoutPanel, "Default");
            return;
        }

        String nodeStr = selectedNode.toString();
        if(nodeStr.startsWith("Test Case") || nodeStr.equals("Create New Test Case")) {
            editPanelCardLayout.show(cardLayoutPanel, "TestCase");
            setupTestCaseEdit(selectedNode.getParent().getParent().toString(), selectedNode.getParent().toString(), nodeStr);
        } else if(nodeStr.startsWith("Practical")){
            setupPracticalEdit(nodeStr);
        } else if(nodeStr.startsWith("Checkpoint")){
            setupCheckpointEdit(selectedNode.getParent().toString(), nodeStr);
        }
    }

    /**
     * Updates the current view to show the test case editing card as either a create new or current.
     * Loads in existing content if able, and updates available interactions based on whether the
     * view of an existing test or a new one.
     *
     * @param practicalNumberStr A String in the form "Practical X" where X is the practical number.
     * @param checkpointNumberStr A String in the form "Checkpoint X" where X is the checkpoint number.
     * @param testNumberStr A String in the form "Test Case X" where X is the test case number.
     */
    private void setupTestCaseEdit(String practicalNumberStr, String checkpointNumberStr, String testNumberStr) {
        currentPractical = getNumberFromString(practicalNumberStr, "Practical ");
        currentCheckpoint = getNumberFromString(checkpointNumberStr, "Checkpoint ");
        currentTestCase = testNumberStr.startsWith("Create") ? -1 : getNumberFromString(testNumberStr, "Test Case ");

        if(currentTestCase == -1) {
            expectedOutputFileTextArea.setText("");
            inputFileTextArea.setText("");
        } else {
            populateTextAreaFromFile(getTestFileName(currentCheckpoint,currentTestCase,true, true), inputFileTextArea);
            populateTextAreaFromFile(getTestFileName(currentCheckpoint,currentTestCase,false, true), expectedOutputFileTextArea);
        }
        deleteButton.setEnabled(currentTestCase != -1);
        saveButton.setEnabled(currentTestCase != -1);
        refreshButton.setEnabled(currentTestCase != -1);
        generateOutputButton.setEnabled(checkpointToFunctionHashTable.get(currentCheckpoint) != null);
        testCaseTitle.setText("Editing " + practicalNumberStr + " " + checkpointNumberStr + " " + testNumberStr);
        inputAreaTitle.setText("Input for " + testNumberStr);
        outputAreaTitle.setText("Output for " + testNumberStr);
    }

    /**
     * Reads specified file and dumps the content into the specified text area.
     *
     * @param filePath Path of the file to load content from.
     * @param textArea The text area to store the loaded content into.
     */
    private void populateTextAreaFromFile(String filePath, JTextArea textArea) {
        try {
            FileReader fileReader = new FileReader(filePath);
            textArea.read(fileReader, null);
            fileReader.close();
        } catch (FileNotFoundException e) {
            textArea.setText("");
            System.out.println("Could not find: " + filePath);
        } catch (IOException e) {
            textArea.setText("");
            System.out.println("Failed to read: " + filePath);
        }
    }

    /**
     * Handles swapping to a Practical with detection of the current Practical number.
     * Sets the labelling to match the selection and changes the card layout to show the Practical card.
     *
     * @param practicalNumberStr A String in the form "Practical X" where X is the practical number.
     */
    private void setupPracticalEdit(String practicalNumberStr) {
        currentPractical = getNumberFromString(practicalNumberStr, "Practical ");
        currentCheckpoint = currentTestCase = -1;
        practicalTitle.setText("Options for " + practicalNumberStr);
        editPanelCardLayout.show(cardLayoutPanel, "Practical");
    }

    /**
     * Handles swapping to a Checkpoint with detection of the current Practical and Checkpoint number.
     * Sets the labelling to match the selection and changes the card layout to show the Checkpoint card.
     *
     * @param practicalNumberStr A String in the form "Practical X" where X is the practical number.
     * @param checkpointNumberStr A String in the form "Checkpoint X" where X is the checkpoint number.
     */
    private void setupCheckpointEdit(String practicalNumberStr, String checkpointNumberStr) {
        currentPractical = getNumberFromString(practicalNumberStr, "Practical ");
        currentCheckpoint = getNumberFromString(checkpointNumberStr, "Checkpoint ");
        currentTestCase = -1;
        checkpointTitle.setText("Options for " + practicalNumberStr + " " + checkpointNumberStr);
        editPanelCardLayout.show(cardLayoutPanel, "Checkpoint");
    }

    /**
     * Removes the prefix from str and converts the remainder to int.
     *
     * @param str String to get a number from.
     * @param prefix Prefix to remove from the String str.
     * @return An integer number that appears at the end of str.
     */
    private int getNumberFromString(String str, String prefix) {
        return Integer.parseInt(str.replace(prefix,""));
    }

    /**
     * Handles all the different button actions in this class for the different card layouts.
     *
     * @param e Reference to the event that caused the action.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == generateOutputButton) {
            generateOutputForTestCase();
        } else if(e.getSource() == refreshButton) {
            valueChanged(null);
        } else if(e.getSource() == saveButton) {
            saveCurrentTest();
        } else if(e.getSource() == saveAsNewButton) {
            saveCurrentTestAsNew();
        } else if(e.getSource() == deleteButton) {
            deleteCurrentTestCase();
        } else if(e.getSource() == runAllPracticalTestsButton) {
            runAllPracticalTests();
        } else if (e.getSource() == packageZIPButton) {
            createZIPFromPractical();
        } else if(e.getSource() == runAllCheckpointTestsButton) {
            runAllCheckpointTests();
        } else if(e.getSource() == deleteAllTestCasesButton) {
            deleteAllTestCases();
        } else if(e.getSource() == regenerateAllTestCasesCheckpointButton) {
            regenerateAllTestCasesForCheckpoint();
        } else if(e.getSource() == regenerateAllTestCasesPracticalButton) {
            regenerateAllTestCasesForPractical();
        }
    }

    /**
     * Finds the first valid test case number to use that does not have any
     * associated input or output files. And then uses saveCurrentTest() to
     * perform the save. Once successful, the JTree is updated to include the new
     * test case. This new node is inserted just before the create node.
     */
    private void saveCurrentTestAsNew() {
        int oldCurrentTestCase = currentTestCase;
        currentTestCase = 1;
        // Keep looping until the first unique test case number is found.
        while(Files.exists(Path.of(getTestFileName(currentCheckpoint,currentTestCase,true, true)))
        || Files.exists(Path.of(getTestFileName(currentCheckpoint,currentTestCase,false, true)))) {
            currentTestCase++;
        }
        boolean success = saveCurrentTest();
        if(success) {
            // Add the new tree node for the created case and select it.
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("Test Case " + currentTestCase);
            // Insert based on correct numerical order
            ((DefaultMutableTreeNode) selectedNode.getParent()).insert(newNode, currentTestCase - 1);
            menuTree.setSelectionPath(new TreePath(newNode.getPath()));
            menuTree.updateUI();
        } else {
            currentTestCase = oldCurrentTestCase;
        }
    }

    /**
     * Collects the data from the input and output text areas to be saved
     * into files corresponding to the current checkpoint and test case.
     * Checks if the data to be stored is empty for either field and validates
     * any empty cases with the user to ensure they want to save them.
     *
     * @return True if the test was successfully saved. Only false when the user cancels.
     */
    private boolean saveCurrentTest() {
        String outputData = expectedOutputFileTextArea.getText();
        String inputData = inputFileTextArea.getText();
        if(!confirmSavingWhenEmpty(inputData.isEmpty(),outputData.isEmpty())) {
            return false;
        }
        String inputFilePath = getTestFileName(currentCheckpoint,currentTestCase,true, true);
        String outputFilePath = getTestFileName(currentCheckpoint,currentTestCase,false, true);
        dumpTestCaseToFiles(inputData, inputFilePath, outputData, outputFilePath);
        return true;
    }

    /**
     * Shows a message asking if the test case should be saved in the case where
     * there are any empty data elements to be stored into the target files.
     *
     * @param inputDataEmpty True if the data supplied for input is empty.
     * @param outputDataEmpty True if the data supplied for output is empty.
     * @return True if the fields both are not empty, or if the user chooses to go ahead anyway.
     */
    private boolean confirmSavingWhenEmpty(boolean inputDataEmpty, boolean outputDataEmpty) {
        if(outputDataEmpty || inputDataEmpty) {
            String message;
            if(outputDataEmpty && inputDataEmpty) {
                message = "input and output?";
            } else if(outputDataEmpty) {
                message = "output?";
            } else {
                message = "input?";
            }
            int choice = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to create a test with empty " + message);
            return choice == JOptionPane.YES_OPTION;
        }
        return true;
    }

    /**
     * Dumps the input and output data for a test case into the specified file paths.
     * The input file is skipped if the data supplied is empty, but only if it doesn't already exist.
     * This is to prevent cases where an input file did exist and the goal is to remove the content.
     *
     * @param inputData Data representing test case input to be stored in the inputFile.
     * @param inputFilePath Path to store the inputData into.
     * @param outputData Data representing test case output to be stored in the outputFile.
     * @param outputFilePath Path to store the outputData into.
     */
    private void dumpTestCaseToFiles(String inputData, String inputFilePath, String outputData, String outputFilePath) {
        try {
            if(!inputData.isEmpty() || Files.exists(Path.of(inputFilePath))) {
                PrintWriter inputFilePrintWriter = new PrintWriter(inputFilePath);
                inputFilePrintWriter.write(inputData);
                inputFilePrintWriter.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Failed to save Input File: " + inputFilePath);
            e.printStackTrace();
        }
        try {
            PrintWriter outputFilePrintWriter = new PrintWriter(outputFilePath);
            outputFilePrintWriter.write(outputData);
            outputFilePrintWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to save Output File: " + outputFilePath);
            e.printStackTrace();
        }
    }

    /**
     * Verifies what should be deleted via getDeletionMode().
     * Then attempts to delete the files if requested, and updates
     * the view of the tree and content depending on whether
     * any files remain for the current test case.
     */
    private void deleteCurrentTestCase() {
        int deletionMode = getDeletionMode();
        if(deletionMode == 1 || deletionMode == 3) {
            try {
                Files.delete(Path.of(getTestFileName(currentCheckpoint,currentTestCase,true, true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(deletionMode == 2 || deletionMode == 3) {
            try {
                Files.delete(Path.of(getTestFileName(currentCheckpoint,currentTestCase,false, true)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateViewAfterDeletion(deletionMode);
    }

    /**
     * Checks if files were all deleted to update the JTree if necessary.
     * Otherwise if a deletion was requested and at least one of the files
     * still exists the view is updated through a valueChanged call.
     *
     * @param deletionMode -1 for cancel, 1 for only input, 2 for only output, 3 for both.
     */
    private void updateViewAfterDeletion(int deletionMode) {
        boolean inExists = Files.exists(Path.of(getTestFileName(currentCheckpoint,currentTestCase,true, true)));
        boolean outExists = Files.exists(Path.of(getTestFileName(currentCheckpoint,currentTestCase,false, true)));
        // If both no longer exist, remove the tree node and select the parent.
        if(!inExists && !outExists) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
            TreePath parentPath = new TreePath(((DefaultMutableTreeNode)selectedNode.getParent()).getPath());
            ((DefaultMutableTreeNode) selectedNode.getParent()).remove(selectedNode);
            menuTree.setSelectionPath(parentPath);
            menuTree.updateUI();
        } else if(deletionMode != -1) {
            // Update to show the content that was deleted (only happens if at least one in/output file still exists)
            valueChanged(null);
        }
    }

    /**
     * Verifies the user actually wants to delete associated files for the current test.
     * Options are shown only for available input/output files that exist.
     * If both are present the user can choose to delete just one, or both of them.
     *
     * @return -1 for cancel, 1 for only input, 2 for only output, 3 for both.
     */
    private int getDeletionMode() {
        boolean inExists = Files.exists(Path.of(getTestFileName(currentCheckpoint,currentTestCase,true, true)));
        boolean outExists = Files.exists(Path.of(getTestFileName(currentCheckpoint,currentTestCase,false, true)));

        String[] options;
        if(inExists && outExists) {
            options = new String[]{"Delete Input Only", "Delete Output Only", "Delete Both", "Cancel"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Confirm deletion by choosing what to delete below.", "Confirm Deletion",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[3]);
            if(choice == 3) {
                return -1;
            } else {
                return choice+1;
            }
        } else if(inExists || outExists) {
            options = new String[]{"Delete " +(inExists?"Input":"Output"), "Cancel"};
            int choice = JOptionPane.showOptionDialog(null,
                    "Confirm deletion by choosing what to delete below.", "Confirm Deletion",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[1]);
            if(choice == 1) {
                return -1;
            } else {
                return (inExists) ? 1 : 2;
            }
        } else {
            JOptionPane.showMessageDialog(null, "There is no files matching to delete for this item.");
            return -1;
        }
    }

    /**
     * Runs all tests for the current checkpoint if there is a function mapped to its number.
     * Otherwise an error is displayed as a popup to show the user that it could not be completed.
     */
    private void runAllCheckpointTests() {
        if(checkpointToFunctionHashTable.get(currentCheckpoint) == null) {
            JOptionPane.showMessageDialog(null, "There is no associated method link in the hash map for this checkpoint.");
            return;
        }
        PracticalEvaluator.testAllCases(currentCheckpoint, checkpointToFunctionHashTable.get(currentCheckpoint));
    }

    /**
     * Finds all checkpoints for the current practical. Any checkpoints with
     * no specified functions to call are logged and shown as a message at the end.
     * For all checkpoints that do have test cases, those tests are run with
     * regular output.
     */
    private void runAllPracticalTests() {
        List<Integer> skippedCheckpoints = new ArrayList<>();
        List<Integer> allCheckpointsForPractical = getAllCheckpointsForPractical(currentPractical);
        for (int checkpointNumber : allCheckpointsForPractical) {
            if(checkpointToFunctionHashTable.get(checkpointNumber) == null) {
                skippedCheckpoints.add(checkpointNumber);
                continue;
            }
            PracticalEvaluator.testAllCases(checkpointNumber, checkpointToFunctionHashTable.get(checkpointNumber));
        }
        if(!skippedCheckpoints.isEmpty()) {
            JOptionPane.showMessageDialog(null, "There is no associated method link in the hash map for checkpoint(s): "
                    + Arrays.toString(skippedCheckpoints.toArray()) + ".");
        }
    }

    /**
     * Attempts to run the associated practical code to generate output based
     * on the input supplied in the inputFileTextArea. The result overwrites the
     * content in the expectedOutputFileTextArea.
     */
    private void generateOutputForTestCase() {
        if(checkpointToFunctionHashTable.get(currentCheckpoint) == null) {
            JOptionPane.showMessageDialog(null, "There is no associated method link in the hash map for this checkpoint.");
            return;
        }
        // Store current input/output streams and redirect them.
        PrintStream defaultConsole = System.out;
        InputStream defaultInput = System.in;
        ByteArrayOutputStream customConsole = new ByteArrayOutputStream();
        System.setOut(new PrintStream(customConsole));
        System.setIn(new ByteArrayInputStream(inputFileTextArea.getText().getBytes()));
        // Execute the method and dump the result to the text area, then reset back to regular output streams
        checkpointToFunctionHashTable.get(currentCheckpoint).run();
        expectedOutputFileTextArea.setText(customConsole.toString());
        System.setOut(defaultConsole);
        System.setIn(defaultInput);
    }

    /**
     * Creates a ZIP file named "PracticalXXXTests.zip" where XXX is the current
     * practical number. All valid test cases for each of the checkpoints identified
     * to be part of this practical are added automatically to the ZIP file.
     * When complete a popup tells the user it has been completed.
     */
    private void createZIPFromPractical() {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("Practical" + currentPractical + "Tests.zip");
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open output stream to: " + "Practical" + currentPractical + "Tests.zip");
            e.printStackTrace();
            return;
        }
        ZipOutputStream zipOut = new ZipOutputStream(fos);
        addAllPracticalCheckpointsToZip(zipOut);
        try {
            zipOut.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Failed to close ZIP output stream.");
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(null, "Successfully generated "
                +"Practical" + currentPractical + "Tests.zip");
    }

    /**
     * Gets all the tests for each checkpoint considered part of the current practical and
     * adds their associated files to the zip output stream.
     *
     * @param zipOut Reference to the resulting ZIP file.
     */
    private void addAllPracticalCheckpointsToZip(ZipOutputStream zipOut) {
        List<Integer> allCheckpointsForPractical = getAllCheckpointsForPractical(currentPractical);
        // Add all valid test cases to the zip file
        for(int checkpointNumber : allCheckpointsForPractical) {
            List<Integer> validTests = PracticalEvaluator.getAllValidTestCases(checkpointNumber,false);
            for(int validTest : validTests) {
                String inputFileName = getTestFileName(checkpointNumber,validTest,true,true);
                String outputFileName = getTestFileName(checkpointNumber,validTest,false,true);
                if(Files.exists(Path.of(inputFileName))) {
                    zipFile(new File(inputFileName), getTestFileName(checkpointNumber,validTest,true,false), zipOut);
                }
                if(Files.exists(Path.of(outputFileName))) {
                    zipFile(new File(outputFileName), getTestFileName(checkpointNumber,validTest,false,false), zipOut);
                }
            }
        }
    }

    /**
     * Adds a single file to the ZIP file referenced by zipOut.
     *
     * @param fileToZip The file to be zipped.
     * @param fileName The file name to store the file as in the zip.
     * @param zipOut Reference to the output stream for the resulting zip file.
     */
    private void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) {
        if (fileToZip.isDirectory()) {
            return;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToZip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipEntry zipEntry = new ZipEntry(fileName);
        try {
            zipOut.putNextEntry(zipEntry);
            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates file names that match valid forms for the test cases.
     * Example:
     * Path included, checkpoint 5, test number 3, input
     * TestCases/5_3.in
     * Path not included, checkpoint 7, test number 4, output
     * 7_4.out
     *
     * @param checkpointNumber Number of the checkpoint that the test applies to.
     * @param testNumber Number of the test for the specific checkpoint.
     * @param input When true applies .in, otherwise applies .out.
     * @param includePath When true the folder is included in the filepath.
     * @return A String representing either the file name or the file path for the test case.
     */
    private String getTestFileName(int checkpointNumber, int testNumber, boolean input, boolean includePath) {
        StringBuilder result = new StringBuilder();
        if(includePath) result.append("TestCases/");
        result.append(checkpointNumber);
        result.append("_");
        result.append(testNumber);
        result.append((input)?".in":".out");
        return result.toString();
    }

    /**
     * Processes the PRACTICAL_DEFINITION to find the specified practical number.
     * Then adds all the checkpoints associated with that practical to the resulting list.
     *
     * @param practicalNumber Number of the practical to get a list of valid checkpoints for.
     * @return A list containing all checkpoints considered to be part of the specified practical.
     */
    private List<Integer> getAllCheckpointsForPractical(int practicalNumber) {
        // Find the valid range of checkpoint numbers for the current practical
        int i = 0;
        for(; i < PRACTICAL_DEFINITION.length; i++) {
            if(practicalNumber == PRACTICAL_DEFINITION[i][0]) break;
        }
        List<Integer> result = new ArrayList<>();
        for(int checkpointNumber = PRACTICAL_DEFINITION[i][1]; checkpointNumber <= PRACTICAL_DEFINITION[i][2]; checkpointNumber++) {
            result.add(checkpointNumber);
        }
        for(int extraIndexes = 3; extraIndexes < PRACTICAL_DEFINITION[i].length; extraIndexes++) {
            result.add(PRACTICAL_DEFINITION[i][extraIndexes]);
        }
        return result;
    }

    /**
     * Iterates over every checkpoint associated with the current practical,
     * and for every checkpoint the test cases are regenerated. The total number
     * of modified output files is shown in a summary popup at the end.
     */
    private void regenerateAllTestCasesForPractical() {
        int totalUpdated = 0;
        List<Integer> allCheckpointsForPractical = getAllCheckpointsForPractical(currentPractical);
        for(int checkpoint : allCheckpointsForPractical) {
            totalUpdated += regenerateAllTestCasesForCheckpoint(checkpoint);
        }
        JOptionPane.showMessageDialog(null,"Finished regenerating " + totalUpdated + " test cases.");
    }

    /**
     * Regenerates every test case output for the current checkpoint.
     * The total number of updated test cases are displayed in a popup at the end.
     */
    private void regenerateAllTestCasesForCheckpoint() {
        int totalUpdated = regenerateAllTestCasesForCheckpoint(currentCheckpoint);
        JOptionPane.showMessageDialog(null,"Finished regenerating " + totalUpdated + " test cases.");
    }

    /**
     * Iterates over all test cases for the specified checkpoint and regenerates
     * a the output based on any associated input file (if there is one).
     *
     * @param checkpoint The checkpoint to regenerate all test case outputs for.
     * @return A count of how many were successfully regenerated.
     */
    private int regenerateAllTestCasesForCheckpoint(int checkpoint) {
        int totalUpdated = 0;
        List<Integer> validTestCases = PracticalEvaluator.getAllValidTestCases(checkpoint,false);
        for(int testCase : validTestCases) {
            boolean success = regenerateSingleTestCaseOutput(checkpoint, testCase);
            if(success) {
                System.out.println("Regenerated Checkpoint " + checkpoint + " Test Case " + testCase);
                totalUpdated++;
            } else {
                System.out.println("Failed to regenerate Checkpoint " + checkpoint + " Test Case " + testCase);
            }
        }
        return totalUpdated;
    }

    /**
     * Validates the existence of an input file and if there is one it is used to
     * regenerate a matching output file using the associated function from the hash map.
     *
     * @param checkpointNumber The checkpoint to regenerate all test case outputs for.
     * @param testCaseNumber The specific test case in the checkpoint to regenerate.
     * @return True if the file was successfully updated.
     */
    private boolean regenerateSingleTestCaseOutput(int checkpointNumber, int testCaseNumber) {
        if(checkpointToFunctionHashTable.get(checkpointNumber) == null) {
            return false;
        }
        String inputFilePath = getTestFileName(checkpointNumber,testCaseNumber,true,true);
        // Store current input/output streams and redirect them.
        PrintStream defaultConsole = System.out;
        InputStream defaultInput = System.in;
        FileInputStream inputFileStream = null;
        if(Files.exists(Path.of(inputFilePath))) {
            try {
                inputFileStream = new FileInputStream(inputFilePath);
                System.setIn(inputFileStream);
            } catch (FileNotFoundException e) {
                return false;
            }
        }
        PrintStream outputFileStream;
        try {
            outputFileStream = new PrintStream(getTestFileName(checkpointNumber,testCaseNumber,false,true));
        } catch (FileNotFoundException e) {
            return false;
        }
        System.setOut(outputFileStream);
        checkpointToFunctionHashTable.get(checkpointNumber).run();
        outputFileStream.close();
        System.setOut(defaultConsole);
        System.setIn(defaultInput);
        if(inputFileStream != null) {
            try {
                inputFileStream.close();
            } catch (IOException e) {
                // do nothing, don't care that it failed here.
            }
        }
        return true;
    }

    /**
     * Gets a list of all files that will be deleted to verify with the user first.
     * Upon confirmation the files are all deleted and the user is told the action has
     * been completed successfully.
     */
    private void deleteAllTestCases() {
        List<String> filesToBeDeleted = getAllFilesForCurrentCheckpoint();
        if(filesToBeDeleted.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No files were found that could be deleted.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(null,
                "You are about to delete all test cases for Checkpoint " + currentCheckpoint + "\n"
                    + Arrays.toString(filesToBeDeleted.toArray())+"\nAre you certain you want to do this?");
        if(choice == JOptionPane.YES_OPTION) {
            deleteAllTestFilesInList(filesToBeDeleted);
            JOptionPane.showMessageDialog(null, "All files for Checkpoint " + currentCheckpoint
                                        + " have been deleted.");
        } else {
            System.out.println("Aborted deletion of all test cases for Checkpoint " + currentCheckpoint);
        }
    }

    /**
     * Iterates over all files in the TestCases folder to find any that start
     * with the current checkpoint number.
     * @return A list of all files associated with the current checkpoint.
     */
    private List<String> getAllFilesForCurrentCheckpoint() {
        List<String> filesToBeDeleted = new ArrayList<>();
        File testCaseDirectory = new File("TestCases");
        String[] testCaseFileNames = testCaseDirectory.list();
        if(testCaseFileNames == null) {
            System.out.println("\u001B[31mWarning! Test case directory missing.\u001B[0m");
            return filesToBeDeleted;
        }
        for(String testCaseFileName : testCaseFileNames) {
            if(testCaseFileName.startsWith(currentCheckpoint+"_")) {
                filesToBeDeleted.add(testCaseFileName);
            }
        }
        return filesToBeDeleted;
    }

    /**
     * Iterates over the list of supplied files and deletes them all.
     * Then iterates over the tree and deletes all the test cases.
     * Note that the tree will show test cases are deleted if they failed.
     * This requires the program be reopened to view them again.
     *
     * @param filesToBeDeleted List of files to delete.
     */
    private void deleteAllTestFilesInList(List<String> filesToBeDeleted) {
        for(String fileName : filesToBeDeleted) {
            try {
                Files.delete(Path.of("TestCases/" + fileName));
            } catch (IOException e) {
                System.out.println("Failed to delete " + fileName);
            }
        }
        DefaultMutableTreeNode selectedCheckpointNode = (DefaultMutableTreeNode) menuTree.getLastSelectedPathComponent();
        int checkpointChildCount = selectedCheckpointNode.getChildCount();
        for(int i = 0; i < checkpointChildCount; i++) {
            while(selectedCheckpointNode.getChildCount() > 1) {
                selectedCheckpointNode.remove(0);
            }
        }
        menuTree.updateUI();
    }
}
