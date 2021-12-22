package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.DrinkItem;
import ca.cmpt213.a4.webappserver.model.FoodItem;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.EventListener;
import java.util.List;

/**
 * Encapsulates all functionality for dealing with POST http requests.
 */
@RestController
public class PostRequests {
    ConsumableManager consumableManager = new ConsumableManager();

    /**
     * Responds to the addFood end point.
     *
     * @param consumable the item to be added.
     * @return the entire consumable list.
     */
    @PostMapping("/addFood")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Consumable> addFood(@RequestBody FoodItem consumable) {
        consumableManager.addItem(consumable);
        return consumableManager.allItems();
    }

    /**
     * Responds to the addDrink end point.
     *
     * @param consumable the item to be added.
     * @return the entire consumable list.
     */
    @PostMapping("/addDrink")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Consumable> addDrink(@RequestBody DrinkItem consumable) {
        consumableManager.addItem(consumable);
        return consumableManager.allItems();
    }

    /**
     * Responds to the removeFood end point.
     *
     * @param consumable the item to be removed.
     * @return the entire consumable list.
     */
    @PostMapping("/removeFood")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Consumable> removeFood(@RequestBody FoodItem consumable) {
        consumableManager.removeItem(consumable);
        return consumableManager.allItems();
    }

    /**
     * Responds to the removeDrink end point.
     *
     * @param consumable the item to be removed.
     * @return the entire consumable list.
     */
    @PostMapping("/removeDrink")
    @ResponseStatus(HttpStatus.CREATED)
    public List<Consumable> removeFood(@RequestBody DrinkItem consumable) {
        consumableManager.removeItem(consumable);
        return consumableManager.allItems();
    }
}
