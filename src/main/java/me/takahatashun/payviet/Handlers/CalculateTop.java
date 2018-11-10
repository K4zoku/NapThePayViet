package me.takahatashun.payviet.Handlers;

import me.takahatashun.payviet.Logger.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class CalculateTop {
    private static final String Day = "dd/MM/yyyy";
    private static final String Month = "MM/yyyy";
    private static final String Year = "yyyy";
    private static final String Total = "total";
    private static ArrayList<String> getLogContent() throws Exception{
        File log = Log.getLog();
        Scanner s = new Scanner(log);
        ArrayList<String> lines = new ArrayList<>();
        while (s.hasNextLine()){
            lines.add(s.nextLine());
        }
        s.close();
        return lines;
    }
    public static Map<String,Double> execute(String type) throws Exception{
        ArrayList<String> log = getLogContent(); // Original log content
        ArrayList<String> dateLog = new ArrayList<>(); // Log contain only lines match date
        Map<String,Double> top = new HashMap<>(); // Result
        if(type.contains(Total)){
            top = getSuccess(top,log);
        } else {
            String format;
            switch (type){
                case "day":format = Day; break;
                case "month":format = Month; break;
                case "year":format = Year; break;
                default:format = Day; break;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date date = new Date();
            for (String line : log) {
                String[] ss = line.split("\\|");
                if (ss[0].trim().contains(dateFormat.format(date))) {
                    dateLog.add(line);
                }
            }
            top = getSuccess(top,dateLog);
        }
        top = SortDesc(top);
        return top;
    }
    private static Map<String,Double> getSuccess(Map<String,Double> inputmap, ArrayList<String> inputarray){
        for(String line:inputarray){
            String[] data = line.split("\\|",8);
            String name = data[1].replaceFirst(" NAME ","");
            double amount = Double.parseDouble(data[5].replaceFirst(" AMOUNT ","").trim());
            boolean success = Boolean.parseBoolean(data[6].replaceFirst(" SUCCESS ","").trim());
            if(success){
                if(!(inputmap.containsKey(name)))
                    inputmap.put(name,amount);
                else
                    inputmap.replace(name,inputmap.get(name)+amount);
            }
        }
        return inputmap;
    }
    private static Map<String,Double> SortAsc(Map<String,Double> map) {
        List<Map.Entry<String,Double>> list = new LinkedList<>(map.entrySet());
        list.sort(Comparator.comparing(o -> (o.getValue())));

        Map<String,Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String,Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
    private static Map<String,Double> SortDesc(Map<String,Double> map) {
        List<Map.Entry<String,Double>> list =
                new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
        Map<String,Double> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String,Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
