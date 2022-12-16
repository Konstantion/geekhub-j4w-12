package edu.geekhub.homework;

import edu.geekhub.controller.Controller;
import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.exceptions.UserValidationException;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;
import edu.geekhub.storage.MemoryStorage;

import static edu.geekhub.utils.response.ResponseMessageGenerator.*;
import static java.util.Objects.isNull;

// Don't move this class
public class UserController implements Controller {

    private final UserService service;

    public UserController() {
        this.service = new UserService(new MemoryStorage());
    }

    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    public Response process(Request request) {
        if (!isNull(request.getData()) && request.getData() instanceof User) {
            try {
                User user = service.saveUser((User) request.getData());
                return Response.ok(successSaveUserResponseMessage(user));
            } catch (ConnectionInterruptedException storageException) {
                return Response.fail(failedConnectToMemoryStorageResponseMessage(
                        storageException.getMessage()));
            } catch (UserValidationException userException) {
                return Response.fail(userException.getMessage());
            }
        } else {
            return Response.fail(badDataRequestMessage(
                    "Request data bad format",
                    request.getData() == null ? "NULL" : request.getData()
            ));
        }
    }
}
