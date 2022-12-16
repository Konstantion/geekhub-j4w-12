package edu.geekhub.homework;

import edu.geekhub.exceptions.ConnectionInterruptedException;
import edu.geekhub.exceptions.UserValidationException;
import edu.geekhub.models.User;
import edu.geekhub.storage.MemoryStorage;
import edu.geekhub.storage.Repository;
import edu.geekhub.utils.datastructures.SimpleListImpl;
import edu.geekhub.utils.validation.UserValidator;

import static edu.geekhub.utils.response.ResponseMessageGenerator.failedValidationResponseMessage;
import static edu.geekhub.utils.validation.messages.ValidationParameter.SUCCESS_VALIDATION;

// Don't move this class
public class UserService {
    private final Repository repository;
    private final UserValidator userValidator;

    public UserService(Repository repository) {
        this.repository = repository;
        this.userValidator = new UserValidator(repository);
    }

    public User saveUser(User user) throws ConnectionInterruptedException, UserValidationException {
        Object result = userValidator.isUserValid(user);
        if (result instanceof Boolean) {
            repository.tryToAdd(user);
            return user;
        } else if (result instanceof SimpleListImpl list) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).equals(SUCCESS_VALIDATION.getParameter())) {
                    stringBuilder.append(list.get(i) + "\n");
                }
            }
            throw new UserValidationException(failedValidationResponseMessage(
                    stringBuilder.toString(),
                    user
            ));
        } else {
            throw new IllegalArgumentException(user.toString());
        }
    }
}
