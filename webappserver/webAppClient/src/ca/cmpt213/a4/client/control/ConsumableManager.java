package ca.cmpt213.a4.client.control;


import ca.cmpt213.a4.client.model.Consumable;
import ca.cmpt213.a4.client.model.ConsumableFactory;
import ca.cmpt213.a4.client.view.AddGUI;
import ca.cmpt213.a4.client.view.RemoveGUI;
import com.google.gson.*;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Holds logic for the operations that can be done on the swing GUI.
 */
public class ConsumableManager {
    private static List<Consumable> consumablesList = new ArrayList<>();

    /**
     * Provides panel populated with all the items and their information.
     *
     * @return panel populated with all items.
     */
    public JPanel allItems() throws IOException {

        // Set up dimensions of allItems panel.
        JPanel allItemsPanel = new JPanel();
        allItemsPanel.setLayout(new BoxLayout(allItemsPanel, BoxLayout.Y_AXIS));

        getHttpGeneral(consumablesList, "listAll");

        if (consumablesList.size() == 0) {
            JLabel noItemLabel = new JLabel("No items to show.");
            allItemsPanel.add(noItemLabel, BorderLayout.NORTH);
        }

        // Populate panel with more panels of items
        for (int i = 0; i < consumablesList.size(); ++i) {
            JPanel itemPanel = new JPanel();
            JLabel itemHeader = new JLabel("Item #" + (i + 1) +
                    " (" + consumablesList.get(i).getConsumableType() + ") ");

            // Get Expired by print
            String expireBy = setExpiryDate(consumablesList.get(i));

            JTextArea itemInfo = new JTextArea(consumablesList.get(i).toString()
                    + "\n" + expireBy);
            itemInfo.setEditable(false);
            itemPanel.add(itemHeader, BorderLayout.NORTH);
            itemPanel.add(itemInfo, BorderLayout.SOUTH);
            allItemsPanel.add(itemPanel);
        }
        allItemsPanel.add(Box.createVerticalGlue());

        return allItemsPanel;
    }

    /**
     * Adds an item to the consumables list.
     *
     * @param parent The application frame.
     */
    public void addItem(JFrame parent) throws IOException {
        AddGUI addItemDialog = new AddGUI(parent);
        Consumable item = addItemDialog.showAddDialog(parent);

        // Sends the item as http request to req body.
        if (item != null) {
            // Set up object to send http req (similar to cmd)
            HttpClient client = HttpClient.newHttpClient();

            String size;
            if (item.getConsumableType().equals("food")) {
                size = "weight";
            } else {
                size = "volume";
            }

            // convert item into Json String
            String itemGson = new JSONObject()
                    .put("consumableType", item.getConsumableType())
                    .put("name", item.getName())
                    .put("notes", item.getNotes())
                    .put("price", item.getPrice())
                    .put("expiryDate", item.getExpiryDate())
                    .put(size, item.getSize())
                    .toString();

            if (item.getConsumableType().equals("food")) {
                reqHelper(client, itemGson, "addFood");
            } else {
                reqHelper(client, itemGson, "addDrink");
            }
        }

        // Gets entire list from server.
        getHttpGeneral(consumablesList, "listAll");
    }

    /**
     * Removes an item from the consumable list.
     *
     * @param parent The application frame.
     */
    public void removeItem(JFrame parent) throws IOException {
        RemoveGUI removeItemDialog = new RemoveGUI(parent);
        int idx = removeItemDialog.showRemoveDialog();

        Consumable item = consumablesList.get(idx);
        // Set up object to send http req (similar to cmd)
        HttpClient client = HttpClient.newHttpClient();

        String size;
        if (item.getConsumableType().equals("food")) {
            size = "weight";
        } else {
            size = "volume";
        }

        // convert item into Json String
        String itemGson = new JSONObject()
                .put("consumableType", item.getConsumableType())
                .put("name", item.getName())
                .put("notes", item.getNotes())
                .put("price", item.getPrice())
                .put("expiryDate", item.getExpiryDate())
                .put(size, item.getSize())
                .toString();

        if (item.getConsumableType().equals("food")) {
            reqHelper(client, itemGson, "removeFood");
        } else {
            reqHelper(client, itemGson, "removeDrink");
        }
        getHttpGeneral(consumablesList, "listAll");
    }

    /**
     * Provides panel populated with expired items.
     *
     * @return panel populated with expire items.
     */
    public JPanel expiredItems() throws IOException {
        Boolean emptyPanel = true;
        int itemNum = 0;
        JPanel expiredItemsPanel = new JPanel();
        expiredItemsPanel.setLayout(new BoxLayout(expiredItemsPanel, BoxLayout.Y_AXIS));

        // Gets list from server and populates list.
        List<Consumable> expiredItems = new ArrayList<>();
        getHttpGeneral(expiredItems, "listExpired");

        // Loop through and populate panel.
        for (int i = 0; i < expiredItems.size(); ++i) {
            emptyPanel = false;
            JPanel itemPanel = new JPanel();
            JLabel itemHeader = new JLabel("Item #" + (itemNum + 1) +
                    " (" + expiredItems.get(i).getConsumableType() + ") ");
            itemNum++;

            // Get Expired by print
            String expireBy = setExpiryDate(expiredItems.get(i));

            JTextArea itemInfo = new JTextArea(expiredItems.get(i).toString()
                    + "\n" + expireBy);
            itemInfo.setEditable(false);
            itemPanel.add(itemHeader, BorderLayout.NORTH);
            itemPanel.add(itemInfo, BorderLayout.SOUTH);
            expiredItemsPanel.add(itemPanel);

        }
        expiredItemsPanel.add(Box.createVerticalGlue());

        // Case for no items.
        if (emptyPanel) {
            JLabel noItemLabel = new JLabel("No expired items to show.");
            expiredItemsPanel.add(noItemLabel, BorderLayout.NORTH);
        }

        return expiredItemsPanel;
    }

    /**
     * Panel populated by non-expired items.
     *
     * @return panel with non-expired items.
     */
    public JPanel nonExpiredItems() throws IOException {
        Boolean emptyPanel = true;
        int itemNum = 0;
        JPanel nonExpiredItemsPanel = new JPanel();
        nonExpiredItemsPanel.setLayout(new BoxLayout(nonExpiredItemsPanel, BoxLayout.Y_AXIS));

        // Gets list from server and populates list.
        List<Consumable> nonExpiredItems = new ArrayList<>();
        getHttpGeneral(nonExpiredItems, "listNonExpired");

        // Loop through and populate panel.
        for (int i = 0; i < nonExpiredItems.size(); ++i) {
            Consumable currentItem = nonExpiredItems.get(i);
            // Case for adding expired items.

            emptyPanel = false;
            JPanel itemPanel = new JPanel();
            JLabel itemHeader = new JLabel("Item #" + (itemNum + 1) +
                    " (" + nonExpiredItems.get(i).getConsumableType() + ") ");
            itemNum++;
            // Get Expired by print
            String expireBy = setExpiryDate(nonExpiredItems.get(i));

            JTextArea itemInfo = new JTextArea(nonExpiredItems.get(i).toString()
                    + "\n" + expireBy);
            itemInfo.setEditable(false);
            itemPanel.add(itemHeader, BorderLayout.NORTH);
            itemPanel.add(itemInfo, BorderLayout.SOUTH);
            nonExpiredItemsPanel.add(itemPanel);

            nonExpiredItemsPanel.add(Box.createVerticalGlue());
        }

        // Case for no items.
        if (emptyPanel) {
            JLabel noItemLabel = new JLabel("No non-expired items to show.");
            nonExpiredItemsPanel.add(noItemLabel, BorderLayout.NORTH);
        }

        return nonExpiredItemsPanel;

    }

    /**
     * Populates panel of items that expire in 7 days.
     *
     * @return Panel populated with items that expire in 7 days.
     */
    public JPanel expireInSevenDays() throws IOException {
        Boolean emptyPanel = true;
        int itemNum = 0;
        JPanel expireInSevenDaysPanel = new JPanel();
        expireInSevenDaysPanel.setLayout(new BoxLayout(expireInSevenDaysPanel, BoxLayout.Y_AXIS));

        // Gets list from server and populates list.
        List<Consumable> expireInSevenDaysItems = new ArrayList<>();
        getHttpGeneral(expireInSevenDaysItems, "listExpiringIn7Days");

        // Loop through and populate panel.
        for (int i = 0; i < expireInSevenDaysItems.size(); ++i) {
            emptyPanel = false;
            JPanel itemPanel = new JPanel();
            JLabel itemHeader = new JLabel("Item #" + (itemNum + 1) +
                    " (" + expireInSevenDaysItems.get(i).getConsumableType() + ") ");
            itemNum++;

            // Get Expired by print
            String expireBy = setExpiryDate(expireInSevenDaysItems.get(i));

            JTextArea itemInfo = new JTextArea(expireInSevenDaysItems.get(i).toString()
                    + "\n" + expireBy);
            itemInfo.setEditable(false);
            itemPanel.add(itemHeader, BorderLayout.NORTH);
            itemPanel.add(itemInfo, BorderLayout.SOUTH);
            expireInSevenDaysPanel.add(itemPanel);
        }
        expireInSevenDaysPanel.add(Box.createVerticalGlue());

        // Case for no items.
        if (emptyPanel) {
            JLabel noItemLabel = new JLabel("No items expiring in 7 days to show.");
            expireInSevenDaysPanel.add(noItemLabel);
        }
        return expireInSevenDaysPanel;
    }

    /**
     * Helper that sends the POST http req to a certain endpoint url depending on item type.
     *
     * @param client      the client (think like cmd)
     * @param itemGson    the gson string item
     * @param endPointUrl the endpoint url (either addFood or addDrink)
     */
    public void reqHelper(HttpClient client, String itemGson, String endPointUrl) {
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://localhost:8080/" + endPointUrl))
                .header("content-type", "application/json") // specify sending info
                .POST(HttpRequest.BodyPublishers.ofString(itemGson)) // specify request body
                .build();
        try {
            // Send the request
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * To send GET http requests to retrieve different lists from the server via end points.
     *
     * @param listToPopulate the list to populate with the server list.
     * @param endPoint       the end point to retrieve the list from
     * @throws IOException an I/O exception case
     */
    public void getHttpGeneral(List<Consumable> listToPopulate, String endPoint) throws IOException {
        // Sends Http request to listAll to get all items to list.
        URL url = new URL("http://localhost:8080/" + endPoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        // Reads all data from server endpoint.
        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();

        // Loads data into our consumable list.
        JsonElement fileValue = JsonParser.parseString(inline.toString());
        if (!listToPopulate.isEmpty()) {
            listToPopulate.clear();
        }
        loadConsumableItems(listToPopulate, fileValue);
        Collections.sort(listToPopulate);

    }

    /**
     * Prints out message depending on expiry date.
     *
     * @param currentItem The current item to print the expiry date of.
     */
    public String setExpiryDate(Consumable currentItem) {
        // calculate the difference in days
        LocalDateTime today = LocalDateTime.now();
        long daysDiff = ChronoUnit.DAYS.between(currentItem.getExpiryDate(), today);

        // Figures out if the item is a food or drink item and prints aptly.
        String consumableType = currentItem.getConsumableType();
        String setExpiryDate = setExpiryDateHelper(daysDiff, consumableType);

        return setExpiryDate;

    }

    /**
     * Prints to UI for expiry date.
     *
     * @param daysDiff       days from today.
     * @param consumableType Food if is food and drink if is drink.
     */
    public String setExpiryDateHelper(long daysDiff, String consumableType) {
        String setExpiryDate;
        // Ladder that decides the expiry message to set to string.
        if (daysDiff == 0) {
            setExpiryDate = "This " + consumableType + " item will expire today.";
        } else if (daysDiff < 0) {
            setExpiryDate = "This " + consumableType + " item will expire in " + abs(daysDiff) + " days.";
        } else {
            setExpiryDate = "This " + consumableType + " item is expired for " + daysDiff + " days.";
        }
        return setExpiryDate;
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
     * Sends an http request to save the file on the server side.
     */
    public void savedConsumableItems() throws IOException {
        // Sends Http request to listAll to get all items to list.
        URL url = new URL("http://localhost:8080/exit");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        StringBuilder inline = new StringBuilder();
        Scanner scanner = new Scanner(url.openStream());

        // Reads all data from server endpoint.
        while (scanner.hasNext()) {
            inline.append(scanner.nextLine());
        }
        scanner.close();
    }

    /**
     * Loads consumables from a JsonElement to a consumableList.
     *
     * @param consumableList     the list to load to.
     * @param jsonFileToLoadFrom the json element to load from.
     */
    public void loadConsumableItems(List<Consumable> consumableList, JsonElement jsonFileToLoadFrom) {
        ConsumableFactory consumableFactory = new ConsumableFactory();
        // Loop and populate the json array with each consumable.
        JsonArray jsonArray = jsonFileToLoadFrom.getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject taskObj = jsonArray.get(i).getAsJsonObject();
            String taskDate = taskObj.get("expiryDate").getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(taskDate, formatter);

            // Case for if food item.
            if (taskObj.get("consumableType").getAsString().equals("food")) {
                consumableList.add(consumableFactory.getInstance(
                        taskObj.get("consumableType").getAsString(),
                        taskObj.get("name").getAsString(),
                        taskObj.get("notes").getAsString(),
                        taskObj.get("price").getAsDouble(),
                        localDateTime,
                        taskObj.get("size").getAsDouble()
                ));

                // Case for if drink item.
            } else {
                consumableList.add(consumableFactory.getInstance(
                        taskObj.get("consumableType").getAsString(),
                        taskObj.get("name").getAsString(),
                        taskObj.get("notes").getAsString(),
                        taskObj.get("price").getAsDouble(),
                        localDateTime,
                        taskObj.get("size").getAsDouble()
                ));
            }
        }
    }
}


