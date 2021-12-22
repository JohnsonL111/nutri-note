package ca.cmpt213.a4.webappserver.controllers;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Encapsulates all functionality for dealing with GET http requests.
 */
@RestController
public class GetRequests {
    ConsumableManager consumableManager = new ConsumableManager();

    /**
     * Responds to the ping end point.
     *
     * @return string of "System is Up!"
     */
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    public String getPing() {
        return "System is Up!";
    }

    /**
     * Responds to the listAll end point.
     *
     * @return the consumable list.
     */
    @GetMapping("/listAll")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> listAllItems() {
        return consumableManager.allItems();
    }

    /**
     * Responds to the listExpired end point
     *
     * @return the list of expired items.
     */
    @GetMapping("/listExpired")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> listExpiredItems() {
        return consumableManager.expiredItems();
    }

    /**
     * Responds to the listNonExpired end point.
     *
     * @return the list of non-expired items.
     */
    @GetMapping("/listNonExpired")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> listNonExpiredItems() {
        return consumableManager.nonExpiredItems();
    }

    /** Responds to the listExpiringIn7Days end point.
     * @return the list of items expiring in 7 days.
     */
    @GetMapping("/listExpiringIn7Days")
    @ResponseStatus(HttpStatus.OK)
    public List<Consumable> listExpiringInSevenDaysItems() {
        return consumableManager.expireInSevenDays();
    }

    /**
     * Responds to the exit end point. Saves server items into json file.
     */
    @GetMapping("/exit")
    @ResponseStatus(HttpStatus.OK)
    public void exit() {
        System.out.println("exiting ....");
        consumableManager.savedConsumableItems(consumableManager.getConsumablesList());
    }
}