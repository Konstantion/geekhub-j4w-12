package edu.geekhub.homework;

import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Response;
import edu.geekhub.storage.MemoryStorage;
import edu.geekhub.storage.Repository;
import edu.geekhub.utils.datastructures.SimpleListImpl;
import edu.geekhub.utils.validation.UserValidator;

import static edu.geekhub.utils.response.ResponseMessageGenerator.*;
import static edu.geekhub.utils.validation.messages.ValidationParameter.SUCCESS_VALIDATION;

// Don't move this class
public class UserService {
    private final Repository repository = new MemoryStorage();
    private final UserValidator userValidator = new UserValidator(repository);

    public Response saveUser(User user) {
        try {
            Object result = userValidator.isUserValid(user);
            if (result instanceof Boolean) {

                repository.tryToAdd(user);
                return Response.ok(successSaveUserResponseMessage(user));
            } else if (result instanceof SimpleListImpl list) {
                list.add(user.toString());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < list.size(); i++) {
                    if (!list.get(i).equals(SUCCESS_VALIDATION.getParameter())) {
                        stringBuilder.append(list.get(i) + "\n");
                    }
                }
                return Response.fail(failedValidationResponseMessage(
                        stringBuilder.toString()
                ));
            } else {
                return Response.fail(user);
            }
        } catch (ConnectionInterruptedException e) {
            return Response.fail(
                    failedConnectToMemoryStorageResponseMessage(
                            e.getMessage(), repository)
            );
        }
    }
}
