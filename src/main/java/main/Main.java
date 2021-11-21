package main;

import frontcontroller.FrontController;
import io.javalin.Javalin;

public class Main {
	public static void main (String[] args) {
		Javalin webServer = Javalin.create ().start (80);
		
		new FrontController (webServer);
	}
}
