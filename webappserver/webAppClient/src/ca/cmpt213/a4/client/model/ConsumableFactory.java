package ca.cmpt213.a4.client.model;


import java.time.LocalDateTime;

/**
 * Follows Factory-pattern design: Used to instantiate our consumable sub-classes.
 */
public class ConsumableFactory {

    /**
     * Gets an instance of either a food or drink item.
     *
     * @param consumableType Either "food" or "drink"
     * @param name           The name of the item.
     * @param notes          Any notes for item
     * @param price          The price of the item.
     * @param expiryDate     The expiry date of the item.
     * @param size           The size (either in volume or weight) of the item.
     * @return A fully formed item we can add to our consumable list.
     */
    public Consumable getInstance(String consumableType, String name, String notes,
                                  double price, LocalDateTime expiryDate, double size) {
        // Case for food consumable.
        if (consumableType.equals("food")) {
            return new FoodItem(consumableType, name, notes, expiryDate, price, size);
        }

        // Case for drink consumable.
        if (consumableType.equals("drink")) {
            return new DrinkItem(consumableType, name, notes, expiryDate, price, size);
        }

        return null;
    }

}
