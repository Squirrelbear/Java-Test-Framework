import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolPanel extends JPanel implements ActionListener {
    public JButton addButton, removeButton;
    public Main main;

    public ToolPanel(Main main) {
        this.main = main;
        setPreferredSize(new Dimension(200, 500));
        addButton = new JButton("Add");
        addButton.addActionListener(this);
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        add(addButton);
        add(removeButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addButton) {
            main.getTestCaseCollectionPanel().addPanel(true);
        } else if(e.getSource() == removeButton) {
            main.getTestCaseCollectionPanel().removePanel();
        }
    }
}
