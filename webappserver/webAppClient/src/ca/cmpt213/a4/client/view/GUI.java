package ca.cmpt213.a4.client.view;


import ca.cmpt213.a4.client.control.ConsumableManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Implements the GUI for the application.
 */
public class GUI implements ActionListener {
    private final ConsumableManager consumableManager = new ConsumableManager(); // operation logic
    // Containers.
    private final JFrame appFrame;
    private JScrollPane mainPane;

    // String to keep track of what pane you are on.
    private String currentPane = "allPane";


    /**
     * Default Constructor to form GUI.
     */
    public GUI() {

        // Initialize information about application frame
        appFrame = new JFrame("bob");
        appFrame.setSize(600, 200);
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setTitle("My Consumable Tracker");
        appFrame.setVisible(true);
        appFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Adding an overriding to windowListener
        appFrame.addWindowListener(new WindowListener() {

            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    consumableManager.savedConsumableItems();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        // Initialize all buttons and add them to actionListener
        JButton listALlItemsButton = new JButton("All");
        listALlItemsButton.addActionListener(this);
        JButton expiredItemsButton = new JButton("Expired");
        expiredItemsButton.addActionListener(this);
        JButton notExpiredButton = new JButton("Not Expired");
        notExpiredButton.addActionListener(this);
        JButton expireInSevenDaysButton = new JButton("Expiring in 7 Days");
        expireInSevenDaysButton.addActionListener(this);
        JButton addButton = new JButton("Add Item");
        addButton.addActionListener(this);
        JButton removeButton = new JButton("Remove Item");
        removeButton.addActionListener(this);

        // Create the selection (top) panel, set its layout, and add button components.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout());
        buttonPanel.add(listALlItemsButton);
        buttonPanel.add(expiredItemsButton);
        buttonPanel.add(notExpiredButton);
        buttonPanel.add(expireInSevenDaysButton);
        appFrame.add(buttonPanel, BorderLayout.NORTH);

        // Create the Initial item view (center) panel and set its layout.
        try {
            mainPane = new JScrollPane(consumableManager.allItems());
        } catch (IOException e) {
            e.printStackTrace();
        }
        appFrame.add(mainPane, BorderLayout.CENTER);

        // Create the edit view w/ add/remove buttons (bottom) panel and set its layout.
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(removeButton);
        appFrame.add(bottomPanel, BorderLayout.SOUTH);

        appFrame.pack();
    }

    /**
     * Invoked when registered button is clicked.
     *
     * @param e Used to check the view ID.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "All":
                try {
                    update(consumableManager.allItems());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                currentPane = "allPane";
                break;
            case "Expired":
                try {
                    update(consumableManager.expiredItems());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                currentPane = "expiredPane";
                break;
            case "Not Expired":
                try {
                    update(consumableManager.nonExpiredItems());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                currentPane = "notExpiredPane";
                break;
            case "Expiring in 7 Days":
                try {
                    update(consumableManager.expireInSevenDays());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                currentPane = "expiringIn7DaysPane";
                break;
            case "Add Item":
                try {
                    consumableManager.addItem(appFrame);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    updateCurrPane(currentPane);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Remove Item":
                try {
                    consumableManager.removeItem(appFrame);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                try {
                    updateCurrPane(currentPane);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                break;
        }
    }

    /**
     * Updates the mainPane content. Used after add and remove.
     *
     * @param currentPane current pane that mainPane is on.
     */
    public void updateCurrPane(String currentPane) throws IOException {
        switch (currentPane) {
            case "allPane":
                update(consumableManager.allItems());
                break;
            case "expiredPane":
                update(consumableManager.expiredItems());
                break;
            case "notExpiredPane":
                update(consumableManager.nonExpiredItems());
                break;
            case "expiringIn7DaysPane":
                update(consumableManager.expireInSevenDays());
                break;
        }
    }

    /**
     * Resets and repopulates the pane you switch to.
     *
     * @param panelToSwitchTo Self-Explanatory.
     */
    public void update(JPanel panelToSwitchTo) {
        appFrame.remove(mainPane);
        mainPane = new JScrollPane(panelToSwitchTo);
        appFrame.add(mainPane);
        appFrame.pack();
    }

}
