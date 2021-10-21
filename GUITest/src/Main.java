import javax.swing.*;
import java.awt.*;

public class Main {

    private TestCaseCollectionPanel testCaseCollectionPanel;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        testCaseCollectionPanel = new TestCaseCollectionPanel();
        JScrollPane scrollPane = new JScrollPane(testCaseCollectionPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(730, 600));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        ToolPanel toolPanel = new ToolPanel(this);
        frame.getContentPane().add(toolPanel, BorderLayout.WEST);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    public TestCaseCollectionPanel getTestCaseCollectionPanel() {
        return testCaseCollectionPanel;
    }
}
