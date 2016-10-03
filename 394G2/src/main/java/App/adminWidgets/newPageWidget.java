package App.adminWidgets;

import spark.Request;
import spark.Response;
import java.sql.*;

public class newPageWidget implements App.widgets.Widget {

	@Override
	public String display(Request req, Response res) {
		return null;
	}

	@Override
	public String display() {
		String form = "<form action='notsureyet'><input type='text' maxlength='255' value='Type page name here'><br><br>"
				+ "<textarea></textarea><br><input type='submit'></form>";
		
		return form;
	}

}
