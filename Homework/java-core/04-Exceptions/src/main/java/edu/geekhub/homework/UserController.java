package edu.geekhub.homework;

import edu.geekhub.controller.Controller;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;

import static edu.geekhub.utils.response.ResponseMessageGenerator.badDataRequestMessage;
import static java.util.Objects.isNull;

// Don't move this class
public class UserController implements Controller {

    private final UserService service = new UserService();

    @Override
    public Response process(Request request) {
        if (!isNull(request.getData()) && request.getData() instanceof User) {
            return service.saveUser((User) request.getData());
        } else {
            return Response.fail(badDataRequestMessage(
                    "Request data bad format",
                    request.getData() == null ? "NULL": request.getData()
            ));
        }
    }
}
