package services;

import ui.GameUI; import inventory.items.*; import java.util.*; import java.io.*;
/* * Loads defaults from the provided paths. For this assignment we attempt to locate the provided uploaded
files. If they cannot be parsed (PDF in this environment), we fall back to built-in defaults. */ 

public class
FileParserService { private final String[] paths; private final GameUI ui; private Weapon defaultWeapon =
null; private Armor defaultArmor = null;
public FileParserService(String[] paths, GameUI ui){ this.paths = paths;
this.ui = ui; }
public void loadAllDefaults(){
boolean loaded = false;
for (String p: paths){ File f = new File(p); if (f.exists()){
ui.println("Found data file: " + p + " — attempting to parse (best-effort)\n");
 // parsing PDFs is out-of-scope for a short parser; try extracting lines if it's a plain text wrapper
try (BufferedReader br = new BufferedReader(new FileReader(f)))
{ // may fail for PDF but try
String line; while ((line = br.readLine())!=null) { // simplistic detection
if (line.toLowerCase().contains("sword")) defaultWeapon =
new Weapon("Starter Sword", 150, 1, 100, 1);
if (line.toLowerCase().contains("leather")) defaultArmor =
new Armor("Leather Armor",50,1,30);
}
loaded = true; break;
} catch(Exception e) {
ui.println("Couldn't parse file (binary/pdf); falling back to internal defaults."); }
}
}
if (!loaded) ui.println("No data files parsed — using embedded default values.");
if (defaultWeapon == null) defaultWeapon = new Weapon("Starter Sword", 150,
1, 100, 1);
if (defaultArmor == null) defaultArmor = new Armor("Leather Armor", 50, 1,
20);
}
public Weapon getDefaultWeaponOrNull(){ return defaultWeapon; }
public Armor getDefaultArmorOrNull(){ return defaultArmor; }
}