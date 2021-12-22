package ca.cmpt213.a4.webappserver.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A Concrete sub-class that contains information for a food item.
 */
public class FoodItem implements Consumable {
    private String consumableType;
    private String name;
    private String notes;
    private double price;
    private LocalDateTime expiryDate;
    private double weight;

    /**
     * Parameterized constructor.
     *
     * @param consumableType Either food or drink depending on user input.
     * @param name           The name of the food.
     * @param notes          Any notes for the food.
     * @param expiryDate     The expiry date of the food item.
     * @param price          The price of the food (in 2 decimal places)
     * @param weight         The weight of the food item in g.
     */
    public FoodItem(String consumableType, String name, String notes,
                    LocalDateTime expiryDate, double price, double weight) {
        this.consumableType = consumableType;
        this.name = name;
        this.notes = notes;
        this.price = price;
        this.expiryDate = expiryDate;
        this.weight = weight;
    }

    /**
     * Getter for the name.
     *
     * @return the item's name as a String.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Getter for the notes.
     *
     * @return the item's notes as a String.
     */
    @Override
    public String getNotes() {
        return notes;
    }

    /**
     * Getter for the price.
     *
     * @return the item's price as a double.
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Getter for the size (vol / size)
     *
     * @return the item's size as a double.
     */
    @Override
    public double getSize() {
        return weight;
    }

    /**
     * Getter for the expiry date.
     *
     * @return The expiry date of the item.
     */
    @Override
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }


    /**
     * Getter for the consumable type.
     *
     * @return The consumable type for the item.
     */
    @Override
    public String getConsumableType() {
        return consumableType;
    }

    /**
     * Overriding of the toString method to print out details about the item.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTimeString = expiryDate.format(formatter);
        String priceToTwoDecimals = String.format("%.2f", price);
        String weightToTwoDecimals = String.format("%.2f", weight);
        return
                "This is a " + consumableType + " item" + "\n" +
                        "Name: " + name + "\n" +
                        "Notes: " + notes + "\n" +
                        "Price: " + priceToTwoDecimals + "\n" +
                        "Weight: " + weightToTwoDecimals + "\n" +
                        "Expiry Date: " + dateTimeString;
    }

    /**
     * Implements the Comparable abstract method/interface.
     *
     * @param o This is the object to compare the expiry date to.
     *          This helped: https://stackoverflow.com/questions/3767090/what-do-the-return-values-of-comparable-compareto-mean-in-java
     * @return 0 if equivalent. A negative if this < that; else positive.
     */
    @Override
    public int compareTo(Consumable o) {
        return expiryDate.compareTo(o.getExpiryDate());
    }
}

