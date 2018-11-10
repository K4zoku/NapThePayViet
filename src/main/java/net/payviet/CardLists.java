package net.payviet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.takahatashun.payviet.Configurations.YAML;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static me.takahatashun.Library.Chat.ChatAPI.upperCaseFirstLetter;
import static net.payviet.PayVietAPI.getStatusCard;


public class CardLists {
    private static List<String> Card = new ArrayList<>();
    private static List<String> CardName = new ArrayList<>();
    private static List<String> CardValue = new ArrayList<>();
    private static int[] AmountList = {10000,20000,30000,50000,100000,200000,300000,500000,1000000};

    public static void getCardStatus() throws Exception{
        JsonParser parser = new JsonParser();
        JsonArray array = parser.parse(getStatusCard()).getAsJsonArray();
        for(int i = 0; i<array.size();i++){
            JsonObject ci = array.get(i).getAsJsonObject();
            String card_type = ci.get("card_type").getAsString();
            String card_name =  upperCaseFirstLetter(
                                    ci.get("name").getAsString()
                                );
            String card_status = ci.get("status").getAsString();
            boolean card_sts = card_status.equalsIgnoreCase("1");
            if(card_sts){
                Card.add(card_name+":"+card_type);
            }
        }
    }
    public static int[] getAmount(){
        Set<String> section = YAML.getConfig().getConfigurationSection("Card").getKeys(false);
        List<String> keys = new ArrayList<>(section);
        try {
            for (int i = 0; i < keys.size(); i++) {
                if(keys.get(i).matches("(?m)[0-9]{1,100}"))
                AmountList[i] = Integer.parseInt(keys.get(i));
            }
            return AmountList;
        } catch (Exception e){
            return AmountList;
        }
    }
    public static List<String> getCard(){
        try {
            getCardStatus();
            return removeDump(Card);
        } catch (Exception e){
            return removeDump(Card);
        }
    }
    public static List<String> getCardName() {
        for (String card : getCard()) {
            String[] ci = card.split(":", 2);
            CardName.add(upperCaseFirstLetter(ci[0]));
        }
        return CardName;
    }
    public static List<String> getCardValue() {
        for (String card : getCard()) {
            String[] ci = card.split(":", 2);
            CardValue.add(upperCaseFirstLetter(ci[1]));
        }
        return CardValue;
    }
    private static List<String> removeDump(List<String> input){
        Set<String> hs = new HashSet<>(input);
        input.clear();
        input.addAll(hs);
        return input;
    }
}
