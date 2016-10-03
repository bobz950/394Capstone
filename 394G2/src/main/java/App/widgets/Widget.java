package App.widgets;

import spark.Request;
import spark.Response;

public interface Widget {
	//call this method if you need to access the request or response
	public String display(Request req, Response res);
	//call this method if you don't
	public String display();
}
