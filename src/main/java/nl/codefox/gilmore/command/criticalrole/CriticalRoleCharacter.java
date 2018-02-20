package nl.codefox.gilmore.command.criticalrole;

import java.util.ArrayList;
import java.util.List;

public class CriticalRoleCharacter {

	private static List<CriticalRoleCharacter> characters = new ArrayList<>();

	private List<String> aliases = new ArrayList<>();
	private String resource = "?";
	private String name = "?";
	private String title = "?";
	private String strength = "?";
	private String currentHP = "?";
	private String maxHP = "?";
	private String tempHP = "?";
	private String armourClass = "?";
	private String status = "?";
	private String dexerity = "?";
	private String constitution = "?";
	private String intelligence = "?";
	private String wisdom = "?";
	private String charisma = "?";

	public static void clear() {
		characters.clear();
	}

	public static void addCharacter(CriticalRoleCharacter character) {
		characters.add(character);
	}

	public static CriticalRoleCharacter getCharacter(String name) {
		for (CriticalRoleCharacter character : characters) {
			if (character.getAliases().contains(name)) {
				return character;
			}
		}
		return null;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		for (int i = 0; i < aliases.size(); i++) {
			aliases.set(i, aliases.get(i).toLowerCase());
		}
		this.aliases.addAll(aliases);
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.aliases.add(name.toLowerCase());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStrength() {
		return strength;
	}

	public void setStrength(String strength) {
		this.strength = strength;
	}

	public String getCurrentHP() {
		return currentHP;
	}

	public void setCurrentHP(String currentHP) {
		this.currentHP = currentHP;
	}

	public String getMaxHP() {
		return maxHP;
	}

	public void setMaxHP(String maxHP) {
		this.maxHP = maxHP;
	}

	public String getTempHP() {
		return tempHP;
	}

	public void setTempHP(String tempHP) {
		this.tempHP = tempHP;
	}

	public String getArmourClass() {
		return armourClass;
	}

	public void setArmourClass(String armourClass) {
		this.armourClass = armourClass;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDexerity() {
		return dexerity;
	}

	public void setDexerity(String dexerity) {
		this.dexerity = dexerity;
	}

	public String getConstitution() {
		return constitution;
	}

	public void setConstitution(String constitution) {
		this.constitution = constitution;
	}

	public String getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(String intelligence) {
		this.intelligence = intelligence;
	}

	public String getWisdom() {
		return wisdom;
	}

	public void setWisdom(String wisdom) {
		this.wisdom = wisdom;
	}

	public String getCharisma() {
		return charisma;
	}

	public void setCharisma(String charisma) {
		this.charisma = charisma;
	}

}
