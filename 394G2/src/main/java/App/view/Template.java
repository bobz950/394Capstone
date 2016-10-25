package App.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Template {

	public static String getContent() {
		String html;
		try {
			html = new String(Files.readAllBytes(Paths.get("layouts/template.html")));
		} 
		catch (IOException s) {
			html = System.getProperty("user.dir");
		}
		
		return html;
	}
}
