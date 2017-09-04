package nl.codefox.gilmore.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Resources {

	private static Map<String, String> SQL = new HashMap<String, String>();

	static {
		loadSQL();
	}

	private static void loadSQL() {
		Logging.debug("[Resources] Getting SQL");

		loadSQLAsResourceList();
		if (!SQL.isEmpty())
			return;

		try {
			ClassLoader loader = Resources.class.getClassLoader();
			InputStream in = loader.getResourceAsStream("sql/");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				try (BufferedReader fr = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("sql/" + line)))) {
					String sql = "";
					String sqlLine;
					while ((sqlLine = fr.readLine()) != null) {
						sql += sqlLine + "\n";
					}

					SQL.put(line, sql);
					Logging.debug("[Resources] SQL '" + line + "' loaded in");
				}
			}
			br.close();
			Logging.debug("[Resources] " + SQL.size() + " SQL statements have been loaded in.");
		} catch (Exception ex) {
			Logging.log(ex);
		}
	}

	private static void loadSQLAsResourceList() {
		Class loader = Resources.class.getClass();

		Pattern pattern = Pattern.compile("^sql.*");
		Collection<String> list = ResourceList.getResources(pattern);

		for (String str : list) {
			String[] strarr = str.split("/");
			if (strarr.length > 1) {
				try (BufferedReader fr = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("/" + str)))) {
					String sql = "";
					String sqlLine;
					while ((sqlLine = fr.readLine()) != null) {
						sql += sqlLine + "\n";
					}

					String SQLName = strarr[strarr.length - 1];

					SQL.put(SQLName, sql);
					Logging.debug("[Resources] SQL '" + SQLName + "' loaded in");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Logging.debug("[Resources] " + SQL.size() + " SQL statements have been loaded in.");
	}
	public static String getSQL(String key) {
		return SQL.get(key);
	}

}
