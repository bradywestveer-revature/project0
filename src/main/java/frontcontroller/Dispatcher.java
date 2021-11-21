package frontcontroller;

import controllers.AccountController;
import controllers.ClientController;
import io.javalin.Javalin;

public class Dispatcher {
	public Dispatcher (Javalin webServer) {
		//clients
		webServer.get ("/clients", ClientController::getClients);
		webServer.get ("/clients/{clientId}", ClientController::getClient);
		
		webServer.post ("/clients", ClientController::createClient);
		
		webServer.put ("/clients/{clientId}", ClientController::updateClientName);
		
		webServer.delete ("/clients/{clientId}", ClientController::deleteClient);
		
		//accounts
		webServer.get ("/clients/{clientId}/accounts", AccountController::getAccounts);
		webServer.get ("/clients/{clientId}/accounts/{accountId}", AccountController::getAccount);
		
		webServer.post ("/clients/{clientId}/accounts", AccountController::createAccount);
		
		webServer.put ("/clients/{clientId}/accounts/{accountId}", AccountController::updateAccountName);
		
		webServer.patch ("/clients/{clientId}/accounts/{accountId}", AccountController::updateAccountBalance);
		webServer.patch ("/clients/{clientId}/accounts/{fromAccountId}/transfer/{toAccountId}", AccountController::transferBetweenAccounts);
	}
}
