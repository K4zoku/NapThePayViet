package net.payviet;

import com.google.gson.JsonObject;
import me.takahatashun.Library.Json.Converter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static me.takahatashun.payviet.Main.merchantID;
import static me.takahatashun.payviet.Main.secretKey;

class PayVietAPI {
    //API Server
    private static final String api_status_card = "https://payviet.net/status_card/json";
    private static final String api_server= "http://api.payviet.net/";
    private static final String api_ver = "API-DOICARD-V1";
    static String getStatusCard() throws Exception{
        URL obj = new URL(api_status_card);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestProperty(CONTENT_TYPE, "application/x-www-form-urlencoded");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while((line = in.readLine()) != null)
            response.append(line);
        in.close();
        return response.toString();
    }
    static String createURL(String method, String param){
        return api_server+method+"?"+api_ver+"="+merchantID+"&param="+param;
    }

    static String encrypt(String input){
        String url = createURL("card/encrypt",input);
        url += "&merchantID="+ merchantID +"&secretKey="+ secretKey;
        String output;
        try {
            output = sendData(url);
            JsonObject jobj = Converter.Encode(output);
            output = jobj.get("result").getAsString();
            return output;
        } catch (Exception e) {
            return "";
        }
    }
    static String decrypt(String input){
        String url = createURL("card/decrypt",input);
        url += "&merchantID="+ merchantID +"&secretKey="+ secretKey;
        try {
            String output = sendData(url);
            JsonObject jobj = Converter.Encode(output);
            output = jobj.get("result").toString();
            return output;
        } catch (Exception e) {
            return "";
        }
    }

    static String sendData(String url) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty(CONTENT_TYPE, "application/x-www-form-urlencoded");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while ((line = in.readLine()) != null)
            response.append(line);

        in.close();
        return response.toString();
    }

}
