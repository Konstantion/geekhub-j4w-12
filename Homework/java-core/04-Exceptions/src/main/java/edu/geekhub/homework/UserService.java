package edu.geekhub.homework;

import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.exceptions.UserValidationException;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;
import edu.geekhub.storage.Repository;
import edu.geekhub.storage.MemoryStorage;
import edu.geekhub.utils.validation.UserValidator;

import static edu.geekhub.utils.response.ResponseMessageGenerator.*;

// Don't move this class
public class UserService {
    private final UserValidator userValidator = new UserValidator();
    private final Repository repository = new MemoryStorage();

    public Response saveUser(Request request) {
        try {
            User user = (User) request.getData();
            userValidator.isUserValid(user, repository.tryToGetAll());
            repository.tryToAdd(user);
            return Response.ok(
                    successSaveUserResponseMessage(user)
            );
        } catch (ClassCastException classCastException) {
            return Response.fail(
                    failedToCastDataToUser(
                            classCastException.getMessage(),
                            request.getData()
                    )
            );
        } catch (ConnectionInterruptedException storageException) {
            return Response.fail(
                    failedConnectToMemoryStorageResponseMessage(
                            storageException.getMessage(),
                            repository)
            );
        } catch (UserValidationException validationException) {
            return Response.fail(failedValidationResponseMessage(validationException.getMessage(),
                    validationException.getData()));
        }
    }
}
