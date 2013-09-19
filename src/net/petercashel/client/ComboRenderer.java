package net.petercashel.client;

import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.lightcouch.CouchDbClient;

import com.google.gson.JsonObject;

/**
 * Renderer Class
 */
public class ComboRenderer extends JLabel implements ListCellRenderer<Object> {

	public ComboRenderer() {

	}

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

		setFont(new Font("Consolas", Font.PLAIN, 14));
		setOpaque(true);

		String hex = null;

		if (value != null) {
			try {
				hex = packListContoller.packs.getAsJsonObject(value.toString()).get("humanName").toString().replace("\"", "");
				setText(hex);
			} catch (NullPointerException n) {
				setText("Error");
			}
		}
		return this;
	}
}