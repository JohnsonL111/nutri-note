package ca.cmpt213.a4.client.view;


import ca.cmpt213.a4.client.control.ConsumableManager;
import ca.cmpt213.a4.client.model.Consumable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Displays the remove Item GUI.
 */
public class RemoveGUI extends JDialog implements ActionListener {
    private final ConsumableManager consumableManager = new ConsumableManager(); // operation logic
    private int idx;

    private final JTextField indexToRemoveField;

    /**
     * Constructor for RemoveGUI.
     *
     * @param parent the application frame.
     */
    public RemoveGUI(JFrame parent) {
        // Set up dimensions and structure of dialog.
        super(parent, true);
        setTitle("Remove Item");
        setResizable(false);
        setSize(250, 200);
        Box box = Box.createVerticalBox();
        setContentPane(box);

        // Instantiate and align top of remove components
        JLabel removeLabel = new JLabel("Item to Remove: ");
        indexToRemoveField = new JTextField(20);
        // Containers
        JPanel removePanel = new JPanel();
        removePanel.add(removeLabel, BorderLayout.WEST);
        removePanel.add(indexToRemoveField, BorderLayout.EAST);
        box.add(removePanel);

        // Instantiate and align bottom of remove components (buttons)
        JButton removeButton = new JButton("Remove Item");
        JButton cancelButton = new JButton("Cancel");
        removeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        JPanel removeOptions = new JPanel();
        removeOptions.add(removeButton, BorderLayout.WEST);
        removeOptions.add(cancelButton, BorderLayout.EAST);
        box.add(removeOptions);


    }

    /**
     * Used to show the GUI.
     *
     * @return an index to remove
     */
    public int showRemoveDialog() {
        setVisible(true);
        return idx;
    }

    /**
     * Invoked when bottom buttons are clicked
     *
     * @param e used to check the view ID.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Remove Item":
                int itemNum = Integer.parseInt(indexToRemoveField.getText());
                idx = itemNum - 1;
                if (checkValidIndex(idx)) {
                    List<Consumable> consumablesList = consumableManager.getConsumablesList();
                    setVisible(false);
                } else {
                    JFrame dialogFrame = new JFrame();
                    JOptionPane.showMessageDialog(dialogFrame, "<htmL> Invalid Item <br> " +
                            "Try again");
                }
                break;
            case "Cancel":
                setVisible(false);
                break;
        }
    }

    /**
     * Checks if given index is valid with the list.
     *
     * @param idx idx to validate.
     * @return true if valid, else false.
     */
    public Boolean checkValidIndex(int idx) {
        List<Consumable> consumableList = consumableManager.getConsumablesList();
        return idx >= 0 && idx < consumableList.size();
    }
}
