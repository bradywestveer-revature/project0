package frontcontroller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import controllers.AccountController;
import controllers.ClientController;
import exceptions.InsufficientBalanceException;
import exceptions.InvalidBodyException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedClientException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

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
		
		webServer.delete ("clients/{clientId}/accounts/{accountId}", AccountController::deleteAccount);
	}
	
	public static void handleException (Exception exception, Context context) {
		if (exception.getClass () == NumberFormatException.class) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid input!");
		}
		
		else if (exception.getClass () == SQLException.class || exception.getClass () == PSQLException.class) {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! SQL error");
		}
		
		else if (exception.getClass () == NotFoundException.class) {
			context.status (404);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		else if (exception.getClass () == UnauthorizedClientException.class) {
			context.status (401);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		else if (exception.getClass () == InsufficientBalanceException.class) {
			context.status (422);
			context.contentType ("text/plain");
			
			context.result (exception.getMessage ());
		}
		
		else if (exception.getClass () == InvalidBodyException.class || exception.getClass () == JsonProcessingException.class || exception.getClass () == JsonParseException.class || exception.getClass () == MismatchedInputException.class || exception.getClass () == UnrecognizedPropertyException.class || exception.getClass () == InvalidFormatException.class) {
			context.status (400);
			context.contentType ("text/plain");
			
			context.result ("Error! Invalid body");
		}
		
		else {
			context.status (500);
			context.contentType ("text/plain");
			
			context.result ("Error! " + exception.getClass ().toString ());
		}
	}
}
