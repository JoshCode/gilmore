package nl.codefox.gilmore.util;

import java.util.List;

public class StringUtil {

	public static String arrayToString(String[] array, int start, String delimiter) {
		StringBuilder ret = new StringBuilder();
		for (int i = start; i < array.length; i++) {
			if (i < array.length - 1) {
				ret.append(array[i]).append(delimiter);
			} else {
				ret.append(array[i]);
			}
		}
		return ret.toString();
	}

	public static String listToString(List<String> list, String delimiter) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i < list.size() - 1) {
				ret.append(list.get(i)).append(delimiter);
			} else {
				ret.append(list.get(i));
			}
		}
		return ret.toString();
	}

}
