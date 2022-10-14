package server;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private static final String FILENAME_TEST_ENVIRONMENT = System.getProperty("user.dir") + "/src/server/data/db.json";
    public static final String FILENAME_LOCAL_ENVIRONMENT = System.getProperty("user.dir") + "\\JSON Database\\task\\src\\server\\data\\db.json";
    private static Map<String, JsonElement> database = new HashMap<>();

    private final Gson gson = new Gson();


    public Database() {
    }

    public String get(JsonElement key) {
        loadFromFile();
        if(database.isEmpty()) {
            return getJsonFormatError();
        }
        JsonElement jsonMsg;
        // No array key
        if (!key.isJsonArray()) {
            jsonMsg = database.get(key.getAsString());
        } else {
            JsonArray arr = key.getAsJsonArray();
            // One key array
            if (arr.size() == 1) {
                jsonMsg = database.get(arr.get(0).getAsString());
                // Multiple key array
            } else {
                jsonMsg = database.get(arr.get(0).getAsString());
                for (int i = 1; i < arr.size(); i++) {
                    jsonMsg = jsonMsg.getAsJsonObject().get(arr.get(i).getAsString());
                }
            }
        }
        return jsonMsg == null ? getJsonFormatError() : getJsonFormatWithValue(jsonMsg);
    }

    public String set(JsonElement key, JsonElement value) {
        loadFromFile();
        String jsonMsg;

        // No array key
        if (!key.isJsonArray()) {
            database.put(key.getAsString(), value);
            jsonMsg = getJsonFormatOk();
        } else {
            JsonArray arr = key.getAsJsonArray();

            // One key array
            if (arr.size() == 1) {
                database.put(arr.get(0).getAsString(), value);
                jsonMsg = getJsonFormatOk();
            } else {

                // Getting the root Json object
                JsonElement main = database.get(arr.get(0).getAsString());
                JsonElement current = main;

                // Searching for the sought-for element
                for (int i = 1; i < arr.size() - 1; i++) {
                    current = current.getAsJsonObject().get(arr.get(i).getAsString());
                }
                if (current != null) {

                    // Removal of the proper element
                    current.getAsJsonObject().remove(arr.get(arr.size() - 1).getAsString());

                    // Changing the value
                    current.getAsJsonObject().add(arr.get(arr.size() - 1).getAsString(), value);
                    database.put(arr.get(0).getAsString(), main);
                    jsonMsg = getJsonFormatOk();
                } else {
                    jsonMsg = getJsonFormatError();
                }
            }

        }
        saveToFile();
        return jsonMsg;
    }

    public String delete(JsonElement key) {
        loadFromFile();
        String jsonMsg;

        // No array key
        if (!key.isJsonArray()) {
            if(database.containsKey(key.getAsString())) {
                database.remove(key.getAsString());
                jsonMsg = getJsonFormatOk();
            } else {
                jsonMsg = getJsonFormatError();
            }
        } else {
            JsonArray arr = key.getAsJsonArray();

            // One key array
            if (arr.size() == 1) {
                if(database.containsKey(arr.get(0).getAsString())) {
                    database.remove(arr.get(0).getAsString());
                    jsonMsg = getJsonFormatOk();
                } else {
                    jsonMsg = getJsonFormatError();
                }
            } else {

                JsonElement main = database.get(arr.get(0).getAsString());
                JsonElement current = main;

                for (int i = 1; i < arr.size() - 1; i++) {
                    current = current.getAsJsonObject().get(arr.get(i).getAsString());
                }
                if (current != null && containsKey(arr)) {

                    // Removal of the proper element
                    current.getAsJsonObject().remove(arr.get(arr.size() - 1).getAsString());
                    database.put(arr.get(0).getAsString(), main);
                    jsonMsg = getJsonFormatOk();
                } else {
                    jsonMsg = getJsonFormatError();
                }
            }

        }
        saveToFile();
        return jsonMsg;

    }

    public String exit() {
        return getJsonFormatOk();
    }


    private void loadFromFile() {
        if (new File(FILENAME_TEST_ENVIRONMENT).length() == 0) return;
        try {
            database = gson.fromJson(new FileReader(FILENAME_TEST_ENVIRONMENT),
                    new TypeToken<HashMap<String, JsonElement>>() {}.getType());
        } catch (IOException e) {
            System.out.println("Json parsing exception");
        }
    }

    private void saveToFile() {
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(FILENAME_TEST_ENVIRONMENT))) {
            gson.toJson(database, writer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    private String getJsonFormatWithValue(JsonElement element) {
        JsonObject returnValue = new JsonObject();
        returnValue.addProperty("response", "OK");
        returnValue.add("value", element);
        return gson.toJson(returnValue);
    }

    private String getJsonFormatOk() {
        JsonObject returnValue = new JsonObject();
        returnValue.addProperty("response", "OK");
        return gson.toJson(returnValue);
    }

    private String getJsonFormatError() {
        JsonObject returnValue = new JsonObject();
        returnValue.addProperty("response", "Error");
        returnValue.addProperty("reason", "No such key");
        return gson.toJson(returnValue);
    }

    private boolean containsKey(JsonArray arr) {
        JsonElement result = database.get(arr.get(0).getAsString());
        for (int i = 1; i < arr.size(); i++) {
            result = result.getAsJsonObject().get(arr.get(i).getAsString());
        }
        return result != null;
    }


}
