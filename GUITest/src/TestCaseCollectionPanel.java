import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestCaseCollectionPanel extends JPanel {
    private int panelNumber = 0;
    private int preferredHeight;
    private List<ExpandingPanelComponent> panelList;

    public TestCaseCollectionPanel() {
        setBackground(Color.orange);
        panelList = new ArrayList<>();
        preferredHeight = 5;
        for(int i = 0; i < 5; i++) {
            addPanel(false);
        }
        setPreferredSize(new Dimension(getWidth(), preferredHeight));
    }

    public void addPanel(boolean calculateResize) {
        panelNumber++;
        panelList.add(new ExpandingPanelComponent(panelNumber, this));
        add(panelList.get(panelList.size()-1));
        preferredHeight += 50 + 5;
        if(calculateResize) {
            setPreferredSize(new Dimension(getWidth(), preferredHeight));
        }
        revalidate();
    }

    public void removePanel() {
        if(panelNumber == 0) return;
        ExpandingPanelComponent removeItem = panelList.get(panelNumber-1);
        panelNumber--;
        preferredHeight -= removeItem.getHeight() + 5;
        setPreferredSize(new Dimension(getWidth(), preferredHeight));
        panelList.remove(removeItem);
        remove(removeItem);
        revalidate();
        getParent().revalidate();
        repaint();
    }

    public void updateSizeBy(int sizeChange) {
        preferredHeight += sizeChange;
        setPreferredSize(new Dimension(getWidth(), preferredHeight));
    }
}
