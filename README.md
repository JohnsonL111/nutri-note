**Built With**
-----------
- Java Swing GUI front-end
- Spring Boot back-end 
- HTTP Client API using RESTful methods to communicate between front and back-end

GUI (Client-side) Functionality
----------------------------------
Operation 1: List all items
- All the user must do is click the "all" button at the top left
- Will display "No items to show" if there are no items.

Operation 2: Add a new item
- The user must click the "Add Item" button at the bottom.
- A new GUI will prompt for item details, fill them in and click "Add Item" at the bottom
  to fully add the item to our collection. You should now be able to see the item in the main GUI.
- You may click cancel or the X button at the top right to exit adding an item
- If any of the fields except Notes is empty then a dialogue will pop up when you click
  Add Item and the item will not be added.
- If a "-" is typed into the Price or Weight/Volume field then it will be immediately replaced with
  an empty string (clear its field).

Operation 3: Remove an item
- The user must click the "Remove Item" button at the bottom.
- A new GUI will prompt for the item to remove. Supply an item # and, if valid,
  then the corresponding item will be removed and this will be visible on the main GUI.
- If an invalid item # is given then a dialog will pop up and no item will be removed.
- You may click cancel or the X button at the top right to exit removal.

Operation 4: List expired items
- All the user must do is click the "Expired" button at the top left
- Will display "No expired items to show" if there are no items.

Operation 5: List items that are not expired
- All the user must do is click the "Not Expired" button at the top left
- Will display "No non-expired items to show" if there are no items.

Operation 6: List items expiring in 7 days.
- All the user must do is click the "Expiring in 7 days" button at the top left
- Will display "No items expiring in 7 days to show" if there are no items.

Operation 7: Exit.
- When the user is done with the program they may click the X button at the top right
  to exit the program. Items will be saved via Gson (via server end) and loaded back in the next time
  the user uses the application.

**Server-side Functionality **
-------------------------------
# Note, tested on command prompt (cmd) terminal -> the one built-in to intellij

# Remove curl alias (run if it invokes-webRequest)
Remove-item alias:curl

POST http requests
-----------------
# Adding a food item
curl -i -H "Content-Type: application/json" -X POST -d "{"JSON array here"}" localhost:8080/addFood
E.g.,
curl -i -H "Content-Type: application/json" -X POST -d '{\"consumableType\": \"food\", \"name\": \"sushi\", \"notes\": \"nope\", \"price\": \"12.3\", \"expiryDate\": \"2021-12-15T00:00:00\", \"weight\": \"6\"}' localhost:8080/addFood

# Adding a drink item
curl -i -H "Content-Type: application/json" -X POST -d "{"JSON array here"}" localhost:8080/addDrink
E.g.,
curl -i -H "Content-Type: application/json" -X POST -d '{\"consumableType\": \"drink\", \"name\": \"coke\", \"notes\": \"nope\", \"price\": \"12.3\", \"expiryDate\": \"2021-12-15T00:00:00\", \"volume\": \"6\"}' localhost:8080/addDrink

# Removing a food item
curl -i -H "Content-Type: application/json" -X POST -d "{"JSON array here"}" localhost:8080/removeFood
E.g.,
curl -i -H "Content-Type: application/json" -X POST -d '{\"consumableType\": \"food\", \"name\": \"sushi\", \"notes\": \"nope\", \"price\": \"12.3\", \"expiryDate\": \"2021-12-15T00:00:00\", \"weight\": \"6\"}' localhost:8080/removeFood

# Removing a drink item
curl -i -H "Content-Type: application/json" -X POST -d "{"JSON array here"}" localhost:8080/removeDrink
E.g.,
curl -i -H "Content-Type: application/json" -X POST -d '{\"consumableType\": \"drink\", \"name\": \"coke\", \"notes\": \"nope\", \"price\": \"12.3\", \"expiryDate\": \"2021-12-15T00:00:00\", \"volume\": \"6\"}' localhost:8080/removeDrink

GET http requests
-----------------
# Ping end point
curl -i -H "Content-Type: application/json" -X GET localhost:8080/ping

# listing all items
curl -i -H "Content-Type: application/json" -X GET localhost:8080/listAll

# listing all expired items
curl -i -H "Content-Type: application/json" -X GET localhost:8080/listExpired

# listing all non-expired items
curl -i -H "Content-Type: application/json" -X GET localhost:8080/listNonExpired

# listing all items expiring in 7 days
curl -i -H "Content-Type: application/json" -X GET localhost:8080/listExpiringIn7Days

# exit the application (saves the consumable list!)
curl -i -H "Content-Type: application/json" -X GET localhost:8080/exit


