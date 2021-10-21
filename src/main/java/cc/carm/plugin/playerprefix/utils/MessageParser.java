package cc.carm.plugin.playerprefix.utils;

public class MessageParser {

    public static String parseColor(String text) {
        return text.replaceAll("&", "§").replace("§§", "&");
    }


}
