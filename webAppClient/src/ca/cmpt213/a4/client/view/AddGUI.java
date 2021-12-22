package ca.cmpt213.a4.client.view;

import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumableFactory;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Displays the Add item GUI.
 */
public class AddGUI extends JDialog implements ActionListener {
    private Consumable itemToAdd;
    private final ConsumableFactory consumableFactory = new ConsumableFactory();
    private double price;
    private double size;
    private final DatePicker datePicker = new DatePicker();

    // Components.
    private final JComboBox typeCombo;
    private final JLabel sizeLabel;
    private final JTextField nameField;
    private final JTextField notesField;
    private final JTextField priceField;
    private final JTextField sizeField;

    /**
     * Constructor for AddGUI.
     *
     * @param parent The main menu frame container.
     */
    public AddGUI(JFrame parent) {
        // Set up dimensions and structure of dialog.
        super(parent, true);
        setTitle("Add Item");
        setResizable(false);
        setSize(600, 400);

        // Sets up all our containers.
        // Containers.
        Box box = Box.createVerticalBox();
        JPanel typePanel = new JPanel();
        JPanel namePanel = new JPanel();
        JPanel notesPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel sizePanel = new JPanel();
        JPanel expiryDatePanel = new JPanel();
        JPanel buttonsPanel = new JPanel();
        setContentPane(box);

        // Sets up all our components.
        String[] itemOptions = {"Food", "Drink"};
        typeCombo = new JComboBox(itemOptions);
        JLabel nameLabel = new JLabel("Name: ");
        JLabel notesLabel = new JLabel("Notes: ");
        JLabel priceLabel = new JLabel("Price: ");
        sizeLabel = new JLabel("Weight: ");
        JLabel expiryDateLabel = new JLabel("Expiry Date: ");
        nameField = new JTextField(30);
        notesField = new JTextField(30);
        priceField = new JTextField(30);
        sizeField = new JTextField(30);
        JButton addButton = new JButton("Add Item");
        JButton cancelButton = new JButton("Cancel");

        // Add combo box to action listener.
        typeCombo.addActionListener(this);

        // Set up type selection panel.
        typePanel.add(typeCombo, BorderLayout.CENTER);

        // Set up name panel.
        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.EAST);

        // Set up notes panel.
        notesPanel.add(notesLabel, BorderLayout.WEST);
        notesPanel.add(notesField, BorderLayout.EAST);

        // Set up price panel.
        pricePanel.add(priceLabel, BorderLayout.WEST);
        pricePanel.add(priceField, BorderLayout.EAST);

        // Add key listener to price
        priceField.addKeyListener(new KeyListener() {
            @Override
            /**
             * Erases all signs of -.
             */
            public void keyTyped(KeyEvent e) {
                String currentPriceStr = priceField.getText();
                // Erase any "-" char.
                if (currentPriceStr.equals("-") || currentPriceStr.equals("")) {
                    priceField.setText("");
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        // Set up size panel.
        sizePanel.add(sizeLabel, BorderLayout.WEST);
        sizePanel.add(sizeField, BorderLayout.EAST);

        // Add key listener to the size.
        sizeField.addKeyListener(new KeyListener() {
            @Override
            /**
             *  Erases all signs of -.
             */
            public void keyTyped(KeyEvent e) {
                String currentSizeStr = sizeField.getText();
                // Erase any "-" char.
                if (currentSizeStr.equals("-") || currentSizeStr.equals("")) {
                    sizeField.setText("");
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        // Set up Expiry date panel.
        expiryDatePanel.add(expiryDateLabel, BorderLayout.WEST);
        expiryDatePanel.add(datePicker, BorderLayout.CENTER);

        // Set up buttons panel with add button functionality
        buttonsPanel.add(addButton, BorderLayout.WEST);
        addButton.addActionListener(this);


        // Set up buttons panel with cancel button functionality.
        buttonsPanel.add(cancelButton, BorderLayout.EAST);
        cancelButton.addActionListener(this);

        // Add all panels to box container.
        box.add(typePanel);
        box.add(namePanel);
        box.add(notesPanel);
        box.add(pricePanel);
        box.add(sizePanel);
        box.add(expiryDatePanel);
        box.add(buttonsPanel);
    }


    /**
     * Parses text fields and adds consumable.
     *
     * @return a Consumable to add.
     */
    public Consumable showAddDialog(JFrame parent) {
        setVisible(true);
        return itemToAdd;
    }

    /**
     * Invoked when registered combo box is clicked.
     *
     * @param e Used to check the view ID.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // For combo box.
        switch (typeCombo.getSelectedItem().toString()) {
            case "Food":
                sizeLabel.setText("Weight: ");
                break;
            case "Drink":
                sizeLabel.setText("Volume: ");
                break;
        }

        // For button clicks.
        switch (e.getActionCommand()) {
            case "Add Item":
                addHelper();
                break;
            case "Cancel":
                setVisible(false);
                break;
        }
    }

    /**
     * Parses fields and sets itemToAdd.
     */
    public void addHelper() {
        // Inputting the consumable type
        String consumableType;
        if (typeCombo.getSelectedItem().toString().equals("Food")) {
            consumableType = "food";
        } else {
            consumableType = "drink";
        }

        // Parses name and notes.
        String name = nameField.getText();
        String notes = notesField.getText();

        // Parse price and size.
        String priceStr = priceField.getText();
        String sizeStr = sizeField.getText();

        if (!priceStr.equals("") && !sizeStr.equals("")) {
            price = Double.parseDouble(priceStr);
            size = Double.parseDouble(sizeStr);
            String priceToTwoDecimals = String.format("%.2f", price);
            String sizeToTwoDecimals = String.format("%.2f", size);
            price = Double.parseDouble(priceToTwoDecimals);
            size = Double.parseDouble(sizeToTwoDecimals);
        }

        // Parse date data.
        LocalDate date = datePicker.getDate();

        if (!checkEmptyValidity(date)) {
        } else {
            LocalDateTime expiryDate = date.atStartOfDay();
            itemToAdd = consumableFactory.getInstance(consumableType, name, notes, price, expiryDate, size);
            setVisible(false);
        }
    }

    /**
     * Checks if inputted values are valid.
     * Name must be non-empty.
     * Valid Date.
     */
    public boolean checkEmptyValidity(LocalDate date) {
        JFrame dialogFrame = new JFrame();
        if (nameField.getText().isEmpty() || date == null ||
                priceField.getText().isEmpty() || sizeField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(dialogFrame, "<htmL> Invalid Input <br> " +
                    "Only Notes Can Be Empty");
            return false;
        } else {
            return true;
        }
    }

}

