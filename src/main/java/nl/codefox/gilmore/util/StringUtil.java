package nl.codefox.gilmore.util;

import java.util.List;

public class StringUtil {

    public static String arrayToString(String[] array, int start, String delimiter) {
        String ret = "";
        for (int i = start; i < array.length; i++) {
            if (i < array.length - 1) {
                ret += array[i] + delimiter;
            } else {
                ret += array[i];
            }
        }
        return ret;
    }

    public static String listToString(List<String> list, String delimiter) {
        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret += list.get(i) + delimiter;
            } else {
                ret += list.get(i);
            }
        }
        return ret;
    }

}
