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
import org.apache.log4j.Logger;
import org.postgresql.util.PSQLException;

import java.sql.SQLException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Dispatcher {
	public Dispatcher (Javalin webServer) {
		webServer.routes (() -> {
			path ("clients", () -> {
				get (ClientController::getClients);
				post (ClientController::createClient);
				
				path ("{clientId}", () -> {
					get (ClientController::getClient);
					put (ClientController::updateClientName);
					delete (ClientController::deleteClient);
					
					path ("accounts", () -> {
						get (AccountController::getAccounts);
						post (AccountController::createAccount);
						
						path ("{accountId}", () -> {
							get (AccountController::getAccount);
							put (AccountController::updateAccountName);
							patch (AccountController::updateAccountBalance);
							delete (AccountController::deleteAccount);
						});
						
						path ("{fromAccountId}/transfer/{toAccountId}", () -> {
							patch (AccountController::transferBetweenAccounts);
						});
					});
				});
			});
		});
		
		Logger logger = Logger.getLogger (Dispatcher.class);
		
		webServer.exception (Exception.class, (exception, context) -> {
			logger.error ("Error!", exception);
			
			if (exception.getClass () == NumberFormatException.class) {
				context.status (400);
				
				context.result ("Error! Invalid input!");
			}
			
			else if (exception.getClass () == SQLException.class || exception.getClass () == PSQLException.class) {
				context.status (500);
				
				context.result ("Error! SQL error");
			}
			
			else if (exception.getClass () == NotFoundException.class) {
				context.status (404);
				
				context.result (exception.getMessage ());
			}
			
			else if (exception.getClass () == UnauthorizedClientException.class) {
				context.status (401);
				
				context.result (exception.getMessage ());
			}
			
			else if (exception.getClass () == InsufficientBalanceException.class) {
				context.status (422);
				
				context.result (exception.getMessage ());
			}
			
			else if (exception.getClass () == InvalidBodyException.class || exception.getClass () == JsonProcessingException.class || exception.getClass () == JsonParseException.class || exception.getClass () == MismatchedInputException.class || exception.getClass () == UnrecognizedPropertyException.class || exception.getClass () == InvalidFormatException.class) {
				context.status (400);
				
				context.result ("Error! Invalid body");
			}
			
			else {
				context.status (500);
				
				context.result ("Error! " + exception.getClass ().toString ());
			}
		});
	}
}
