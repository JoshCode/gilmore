package nl.codefox.gilmore.command.criticalrole;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import nl.codefox.gilmore.util.GoogleSheetsUtil;

import java.util.Arrays;
import java.util.List;

public class CriticalRoleTask extends Thread {

    public CriticalRoleTask() {
        this.start();
    }

    @Override
    public void run() {

        try {
            while (true) {
                Sheets service = GoogleSheetsUtil.getSheetsService();

                String sid = CriticalRoleConstants.SPREADSHEET_ID;
                String range = CriticalRoleConstants.SPREADSHEET_CHARACTER_RANGE;

                ValueRange response = service.spreadsheets().values().get(sid, range).execute();

                List<List<Object>> values = response.getValues();
                CriticalRoleCharacter.clear();

                if (values != null && values.size() != 0) {
                    for (int i = 0; i < values.size(); i++) {

                        List<Object> row = values.get(i);

                        if (row.size() != 19) {
                            continue;
                        }

                        if (!((String) row.get(CriticalRoleConstants.PLAYER_CHARACTER_COLUMN)).equalsIgnoreCase("Y")) {
                            continue;
                        }

                        CriticalRoleCharacter crc = new CriticalRoleCharacter();

                        crc.setName((String) row.get(CriticalRoleConstants.NAME_COLUMN));
                        crc.setCurrentHP((String) row.get(CriticalRoleConstants.CURRENT_HP_COLUMN));
                        crc.setMaxHP((String) row.get(CriticalRoleConstants.MAX_HP_COLUMN));
                        crc.setTempHP((String) row.get(CriticalRoleConstants.TEMP_HP_COLUMN));
                        crc.setArmourClass((String) row.get(CriticalRoleConstants.ARMOUR_CLASS_COLUMN));
                        crc.setStatus((String) row.get(CriticalRoleConstants.STATUS_COLUMN));
                        crc.setStrength((String) row.get(CriticalRoleConstants.STRENGTH_COLUMN));
                        crc.setDexerity((String) row.get(CriticalRoleConstants.DEXERITY_COLUMN));
                        crc.setConstitution((String) row.get(CriticalRoleConstants.CONSTITUTION_COLUMN));
                        crc.setIntelligence((String) row.get(CriticalRoleConstants.INTELLIGENCE_COLUMN));
                        crc.setWisdom((String) row.get(CriticalRoleConstants.WISDOM_COLUMN));
                        crc.setCharisma((String) row.get(CriticalRoleConstants.CHARISMA_COLUMN));

                        StringBuilder sbTitle = new StringBuilder();
                        sbTitle.append("the ");
                        sbTitle.append(row.get(CriticalRoleConstants.RACE_COLUMN)).append(" ");
                        sbTitle.append(row.get(CriticalRoleConstants.PRIMARY_CLASS_COLUMN)).append(" ");
                        sbTitle.append("(Lv. ").append(row.get(CriticalRoleConstants.PRIMARY_LEVEL_COLUMN)).append(")");

                        if (!row.get(CriticalRoleConstants.SECONDARY_CLASS_COLUMN).equals("")) {
                            sbTitle.append(", ");
                            sbTitle.append(row.get(CriticalRoleConstants.SECONDARY_CLASS_COLUMN)).append(" ");
                            sbTitle.append("(Lv. ").append(row.get(CriticalRoleConstants.SECONDARY_LEVEL_COLUMN)).append(")");
                        }
                        String title = sbTitle.toString();

                        crc.setTitle(title);
                        crc.setResource(String.format("/images/%s.png", crc.getName().toLowerCase()));
                        crc.setAliases(Arrays.asList(((String) row.get(CriticalRoleConstants.ALIAS_COLUMN)).split(",")));

                        CriticalRoleCharacter.addCharacter(crc);
                    }
                }

                Thread.sleep(5000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
