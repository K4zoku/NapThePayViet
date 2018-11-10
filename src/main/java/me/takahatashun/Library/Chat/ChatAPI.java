package me.takahatashun.Library.Chat;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatAPI {
   public static TextComponent getTextComponent(@Nullable HoverEvent.Action hover, @Nullable BaseComponent[] hoverValue, @Nullable ClickEvent.Action click, @Nullable String clickValue, @NotNull String text){
       TextComponent tc = new TextComponent(text);
       if(click != null && !clickValue.isEmpty()) {
           tc.setClickEvent(new ClickEvent(click, clickValue));
       }
       if(hover != null && hoverValue != null) {
           tc.setHoverEvent(new HoverEvent(hover,hoverValue));
       }
       return tc;
   }
   public static String translateColorCode(String input) {
       Pattern pattern = Pattern.compile("(?ium)&([a-fk-or0-9])");
       return pattern.matcher(input).replaceAll("§$1");
   }
   public static String removeColorCode(String input){
       return input.replaceAll("(?ium)&[0-9a-fk-or]", "");
   }
   public static String upperCaseFirstLetter(String input){
       Pattern pattern = Pattern.compile("(?:^|)[^a-z]*[a-z]");
       Matcher matcher = pattern.matcher(input);
       StringBuffer sb = new StringBuffer();
       while (matcher.find()){
           matcher.appendReplacement(sb,matcher.group().toUpperCase());
       }
       matcher.appendTail(sb);
       return sb.toString();
   }
}
