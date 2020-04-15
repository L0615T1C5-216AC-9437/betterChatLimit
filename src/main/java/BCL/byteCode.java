package BCL;

import org.json.JSONObject;

public class byteCode {
    public static String censor(String string) {
        StringBuilder builder = new StringBuilder();
        String sentence[] = string.split(" ");
        JSONObject badList = Main.badList;
        for (String word: sentence) {
            if (badList.has(word.toLowerCase())) {
                builder.append(word.charAt(0));
                for (int i = 1; i < word.length(); i++) {
                    builder.append("*");
                }
            } else {
                builder.append(word);
            }
            builder.append(" ");
        }
        return builder.toString();
    }
}
