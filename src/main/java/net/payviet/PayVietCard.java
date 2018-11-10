package net.payviet;

import com.google.gson.JsonObject;
import me.takahatashun.Library.Json.Converter;
import me.takahatashun.payviet.Main;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static me.takahatashun.payviet.Main.*;

public class PayVietCard {
    public static List<String> cardList = Main.card;
    public static List<String> cardNameList = CardLists.getCardName();
    public static List<Integer> amountList = Arrays.stream(CardLists.getAmount()).boxed().collect(Collectors.toList());
    public static JsonObject sendCard(int type, String seri, String code, int amount) throws Exception{
        Map<String,String> data = new HashMap<>();
        data.put("card_seri",seri);
        data.put("card_code",code);
        data.put("card_type", String.valueOf(type));
        data.put("card_amount", String.valueOf(amount));
        data.put("token",token);

        JSONObject simpleJSON = new JSONObject();

        simpleJSON.putAll(data);

        String jsonData = simpleJSON.toString();

        String param = PayVietAPI.encrypt(jsonData);
        String url = PayVietAPI.createURL("card",param);
        String result = PayVietAPI.sendData(url);

        return Converter.Encode(result);
    }
    public static String getCard(int input){
        String output = "";
        int i = 0;
        for(String value:cardList){
            if(value.contains(String.valueOf(input))){
                output = cardList.get(i).split(":",2)[0];
            }
            i++;
        }
        return output;
    }
}
