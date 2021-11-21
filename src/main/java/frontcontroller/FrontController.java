package frontcontroller;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class FrontController {
	public FrontController (Javalin webServer) {
		new Dispatcher (webServer);
	}
}
