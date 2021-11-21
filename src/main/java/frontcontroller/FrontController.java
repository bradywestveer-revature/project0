package frontcontroller;

import io.javalin.Javalin;

public class FrontController {
	public FrontController (Javalin webServer) {
		new Dispatcher (webServer);
	}
}
