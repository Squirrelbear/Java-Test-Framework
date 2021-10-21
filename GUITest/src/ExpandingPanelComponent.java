import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpandingPanelComponent extends JPanel implements ActionListener {
    public JButton toggleButton;
    public JLabel title, status;
    public JPanel nestedPanel;
    public boolean isExpanded;
    private int nestedHeight = 210;
    private TestCaseCollectionPanel testCaseCollectionPanel;

    public ExpandingPanelComponent(int num, TestCaseCollectionPanel testCaseCollectionPanel) {
        this.testCaseCollectionPanel = testCaseCollectionPanel;
        setPreferredSize(new Dimension(700, 50));
        toggleButton = new JButton("+");
        toggleButton.addActionListener(this);
        title = new JLabel("Sample label #" + num);
        status = new JLabel("PENDING");
        setLayout(new BorderLayout());
        add(toggleButton, BorderLayout.WEST);
        add(title, BorderLayout.CENTER);
        add(status, BorderLayout.EAST);
        nestedPanel = new JPanel();
        nestedPanel.setPreferredSize(new Dimension(700, nestedHeight));
        nestedPanel.setBackground(Color.PINK);
        nestedPanel.setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.setBorder(new LineBorder(Color.BLACK));
        inputPanel.setPreferredSize(new Dimension(350, (nestedHeight-20)/2));
        inputPanel.add(new JLabel("Input:"), BorderLayout.NORTH);
        inputPanel.add(new JTextArea("Sample Text Here Blah blah blah", 5, 10), BorderLayout.CENTER);
        JPanel expectedOutputPanel = new JPanel();
        expectedOutputPanel.setLayout(new BorderLayout());
        expectedOutputPanel.setBorder(new LineBorder(Color.BLACK));
        expectedOutputPanel.setPreferredSize(new Dimension(350, (nestedHeight-20)/2));
        expectedOutputPanel.add(new JLabel("Expected Output:"), BorderLayout.NORTH);
        expectedOutputPanel.add(new JTextArea("Sample Text Here Blah blah blah", 5, 10), BorderLayout.CENTER);
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        resultPanel.setBorder(new LineBorder(Color.BLACK));
        resultPanel.setPreferredSize(new Dimension(700, (nestedHeight-20)/2));
        resultPanel.add(new JLabel("Result:"), BorderLayout.NORTH);
        resultPanel.add(new JTextArea("Sample Text Here Blah blah blah", 5, 10), BorderLayout.SOUTH);
        nestedPanel.add(new JLabel("Test"), BorderLayout.NORTH);
        nestedPanel.add(inputPanel, BorderLayout.CENTER);
        nestedPanel.add(expectedOutputPanel, BorderLayout.EAST);
        nestedPanel.add(resultPanel, BorderLayout.SOUTH);
        //add(nestedPanel, BorderLayout.SOUTH);
        isExpanded = false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == toggleButton) {
            if(isExpanded) {
                setPreferredSize(new Dimension(getWidth(), getHeight()-nestedHeight));
                remove(nestedPanel);
                testCaseCollectionPanel.updateSizeBy(-nestedHeight);
            } else {
                setPreferredSize(new Dimension(getWidth(), getHeight()+nestedHeight));
                add(nestedPanel, BorderLayout.SOUTH);
                testCaseCollectionPanel.updateSizeBy(nestedHeight);
            }
            revalidate();
            getParent().revalidate();
            isExpanded = !isExpanded;
        }
    }
}
