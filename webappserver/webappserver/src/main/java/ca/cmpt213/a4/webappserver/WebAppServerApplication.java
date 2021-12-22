package ca.cmpt213.a4.webappserver;

import ca.cmpt213.a4.webappserver.control.ConsumableManager;
import ca.cmpt213.a4.webappserver.model.Consumable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Driver for spring boot rest server application.
 */
@SpringBootApplication
public class WebAppServerApplication {

    public static void main(String[] args) {
        ConsumableManager consumableManager = new ConsumableManager();
        consumableManager.loadConsumableItems(consumableManager.getConsumablesList());
        SpringApplication.run(WebAppServerApplication.class, args);
    }

}
