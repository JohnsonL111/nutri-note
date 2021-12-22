package ca.cmpt213.a4.webappserver.control;

import ca.cmpt213.a4.webappserver.model.Consumable;
import ca.cmpt213.a4.webappserver.model.ConsumableFactory;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Holds logic for the operations that is used by the POST and GET methods.
 */
public class ConsumableManager {
    private static final List<Consumable> consumablesList = new ArrayList<>();

    /**
     * Used by the addFood and addDrink POST handler methods.
     *
     * @param Item The consumable to add to the end of the list.
     */
    public void addItem(Consumable Item) {
        consumablesList.add(Item);
    }

    /**
     * Used by the removeItem POST handler method
     *
     * @param item Item to remove.
     */
    public void removeItem(Consumable item) {
        for (int i = 0; i < consumablesList.size(); ++i) {
            if (item.toString().equals(consumablesList.get(i).toString())) {
                consumablesList.remove(i);
                break;
            }
        }
    }

    /**
     * Used by the ListAllTimes GET handler method.
     *
     * @return The entire consumablesList.
     */
    public List<Consumable> allItems() {
        return consumablesList;
    }

    /**
     * Used by the listExpiredItems GET handler method.
     *
     * @return List of expired items.
     */
    public List<Consumable> expiredItems() {
        List<Consumable> expiredItems = new ArrayList<>();

        // Loop through and populate panel.
        for (int i = 0; i < consumablesList.size(); ++i) {
            Consumable currentItem = consumablesList.get(i);
            // Case for adding expired items.
            if (checkDaysExpired(currentItem) > 0) {
                expiredItems.add(consumablesList.get(i));
            }
        }
        return expiredItems;
    }

    /**
     * Used by the listNonExpiredItems GET handler method
     *
     * @return List of non-expired items.
     */
    public List<Consumable> nonExpiredItems() {
        List<Consumable> nonExpiredItems = new ArrayList<>();

        // Loop through and populate panel.
        for (int i = 0; i < consumablesList.size(); ++i) {
            Consumable currentItem = consumablesList.get(i);
            // Case for adding expired items.
            if (checkDaysExpired(currentItem) <= 0) {
                nonExpiredItems.add(consumablesList.get(i));
            }
        }
        return nonExpiredItems;
    }

    /**
     * Used by the listExpiringInSevenDaysItems GET handler method
     *
     * @return List of items expiring in 7 days.
     */
    public List<Consumable> expireInSevenDays() {
        List<Consumable> expireInSevenDaysItems = new ArrayList<>();

        // Loop through and populate panel.
        for (int i = 0; i < consumablesList.size(); ++i) {
            Consumable currentItem = consumablesList.get(i);
            // Case for adding expired items.
            if ((checkDaysExpired(currentItem) >= -7) && (checkDaysExpired(currentItem) < 0)) {
                expireInSevenDaysItems.add(consumablesList.get(i));
            }
        }
        return expireInSevenDaysItems;
    }


    /**
     * Checks how many days an item has expired for.
     *
     * @param consumableItem The item to check its expiry status of.
     * @return The number of days that an item has expired for.
     */
    public long checkDaysExpired(Consumable consumableItem) {
        LocalDateTime today = LocalDateTime.now();
        long days = ChronoUnit.DAYS.between(consumableItem.getExpiryDate(), today);
        return days;
    }


    /**
     * Helper that returns the list of items.
     *
     * @return Returns the consumable list.
     */
    public List<Consumable> getConsumablesList() {
        return consumablesList;
    }

    /**
     * Serializes our items (save from server to Json)
     *
     * @param consumableList An arraylist to store our consumable item sub-class.
     */
    public void savedConsumableItems(List<Consumable> consumableList) {
        String JSON_PATH = "./consumablesList.json";

        // From Professor to add support to writing to/from localDateTime objects.
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();

        try {
            // To write to the Json file.
            Writer writer = new FileWriter(JSON_PATH);

            // Writes all data to Json.
            myGson.toJson(consumableList, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * De-serializes our items (load from json to server)
     *
     * @param consumableList An arraylist to store our consumable item sub-class.
     */
    public void loadConsumableItems(List<Consumable> consumableList) {
        File file = new File("./consumablesList.json");
        ConsumableFactory consumableFactory = new ConsumableFactory();
        try {
            JsonElement fileElement = JsonParser.parseReader(new FileReader(file));

            // Loop and populate the json array with each consumable.
            JsonArray jsonArray = fileElement.getAsJsonArray();
            for (int i = 0; i < jsonArray.size(); i++) {

                JsonObject taskObj = jsonArray.get(i).getAsJsonObject();
                String taskDate = taskObj.get("expiryDate").getAsString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
                LocalDateTime localDateTime = LocalDateTime.parse(taskDate, formatter);

                // Case for if food item.
                if (taskObj.get("consumableType").getAsString().equals("food")) {
                    consumableList.add(consumableFactory.getInstance(
                            taskObj.get("consumableType").getAsString(),
                            taskObj.get("name").getAsString(),
                            taskObj.get("notes").getAsString(),
                            taskObj.get("price").getAsDouble(),
                            localDateTime,
                            taskObj.get("weight").getAsDouble()
                    ));

                    // Case for if drink item.
                } else {
                    consumableList.add(consumableFactory.getInstance(
                            taskObj.get("consumableType").getAsString(),
                            taskObj.get("name").getAsString(),
                            taskObj.get("notes").getAsString(),
                            taskObj.get("price").getAsDouble(),
                            localDateTime,
                            taskObj.get("volume").getAsDouble()
                    ));
                }
            }
        } catch (FileNotFoundException e) {
        }
    }
}

