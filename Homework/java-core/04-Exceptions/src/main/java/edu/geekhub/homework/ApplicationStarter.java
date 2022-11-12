package edu.geekhub.homework;

import edu.geekhub.controller.Controller;
import edu.geekhub.models.User;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;
import edu.geekhub.utils.RandomRequestDataGenerator;
import edu.geekhub.utils.RequestDataGenerator;
import edu.geekhub.utils.generator.ObedientRequestGenerator;

import java.util.UUID;

// Don't move this class
public class ApplicationStarter {

    private static final Controller controller = new UserController();

    private static final RequestDataGenerator generator = new RandomRequestDataGenerator();
    private static final ObedientRequestGenerator obedientGenerator = new ObedientRequestGenerator();

    public static void main(String[] args) {
        obedientGenerator.VALUE_CAN_BE_NULL = false;
        obedientGenerator.EMAIL_CAN_BE_INVALID = false;

        int i = 0;

        while (i < 100) {
            User user = User.toBuilder()
                    .withId(UUID.randomUUID())
                    .withAge(20)
                    .withAmountOfFollowers(20L)
                    .withUserName("kostya")
                    .withFullName("Kostya Yagudin")
                    .withNotes("My note")
                    .withEmail(obedientGenerator.generateEmail())
                    .build();
            Request request = new Request(user);
            //Request request = generator.generate();

            Response response = controller.process(request);

            System.out.println(response);
            i++;
        }
    }
}