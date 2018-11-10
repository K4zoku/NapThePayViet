package me.takahatashun.Library.Json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Converter {
    public static JsonObject Encode(String input){
        JsonParser parser = new JsonParser();
        return parser.parse(input).getAsJsonObject();
    }
    public static String Decode(JsonObject input){
        return input.getAsString();
    }

}
