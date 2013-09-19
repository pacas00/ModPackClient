package net.petercashel.client;

import java.util.List;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

public class packListContoller {
	
	public static String packDB = "";
	public static boolean packPublic = true;
	public static JsonObject packs = new JsonObject();
	
	
	public static void getpackList() {
		
		CouchDbClient dbClient = new CouchDbClient("packlist", false, 
				Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
				Integer.parseInt(Configuration.getString("apiServerPort")), null, null);
		
		List<JsonObject> allDocs = dbClient.view("_all_docs").query(JsonObject.class);
		
		settingsDialog.comboBox.removeAllItems();
		packs = null;
		packs = new JsonObject();
		
		for (int i = 0; i < allDocs.size(); i++) {
			if (!allDocs.get(i).get("id").toString().replace("\"", "").contains("_design/auth")) {
			settingsDialog.comboBox.addItem(allDocs.get(i).get("id").toString().replace("\"", ""));
			JsonObject packJson = dbClient.find(JsonObject.class, allDocs.get(i).get("id").toString().replace("\"", ""));
			packs.add(allDocs.get(i).get("id").toString().replace("\"", ""), packJson);
			}
		}


		dbClient.shutdown();
		
	}
        
        public static void getpackListLaunch() {
		
		CouchDbClient dbClient = new CouchDbClient("packlist", false, 
				Configuration.getString("apiServerProtocol").toLowerCase(), Configuration.getString("apiServer"),
				Integer.parseInt(Configuration.getString("apiServerPort")), null, null);
		
		List<JsonObject> allDocs = dbClient.view("_all_docs").query(JsonObject.class);
		
		packs = null;
		packs = new JsonObject();
		
		for (int i = 0; i < allDocs.size(); i++) {
			if (!allDocs.get(i).get("id").toString().replace("\"", "").contains("_design/auth")) {
			JsonObject packJson = dbClient.find(JsonObject.class, allDocs.get(i).get("id").toString().replace("\"", ""));
			packs.add(allDocs.get(i).get("id").toString().replace("\"", ""), packJson);
			}
		}


		dbClient.shutdown();
		
	}

	public static void saveSelectedPack() {
		Configuration.setString("packName", settingsDialog.comboBox.getSelectedItem().toString());
	}
	
	
	public static void getPackData() {
		packDB = packs.getAsJsonObject(Configuration.getString("packName")).get("db_name").toString();
		packPublic = packs.getAsJsonObject(Configuration.getString("packName")).get("public").getAsBoolean();
	}
	
	
	
}
