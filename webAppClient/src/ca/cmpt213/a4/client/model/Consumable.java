package ca.cmpt213.a4.client.model;


import java.time.LocalDateTime;

/**
 * Interface to produce consumables (DrinkItem/FoodItem concrete classes)
 * FoodItem and DrinkItem implement this interface via implementing all of its
 * abstract methods.
 */
public interface Consumable extends Comparable<Consumable> {

    /**
     * Getter for the expiry date.
     *
     * @return The expiry date of the item.
     */
    LocalDateTime getExpiryDate();

    /**
     * Getter for the consumable type.
     *
     * @return The consumable type for the item.
     */
    String getConsumableType();

    /**
     * Getter for the name.
     *
     * @return the item's name as a String.
     */
    String getName();

    /**
     * Getter for the notes.
     *
     * @return the item's notes as a String.
     */
    String getNotes();

    /**
     * Getter for the price.
     *
     * @return the item's price as a double.
     */
    double getPrice();

    /**
     * Getter for the size (vol / size)
     *
     * @return the item's size as a double.
     */
    double getSize();

    /**
     * Overriding of the toString method to print out details about the food item.
     *
     * @return The new toString print.
     */
    String toString();

    /**
     * Implements the Comparable abstract method/interface.
     *
     * @param o This is the object to compare the expiry date to.
     * @return 0 if equivalent. A negative if this < that; else positive.
     */
    int compareTo(Consumable o);
}
