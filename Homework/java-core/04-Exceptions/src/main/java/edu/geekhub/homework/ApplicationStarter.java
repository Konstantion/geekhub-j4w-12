package edu.geekhub.homework;

import edu.geekhub.controller.Controller;
import edu.geekhub.models.request.Request;
import edu.geekhub.models.request.Response;
import edu.geekhub.utils.RandomRequestDataGenerator;
import edu.geekhub.utils.RequestDataGenerator;
import edu.geekhub.utils.datastructures.SimpleListImpl;
import edu.geekhub.utils.generator.ObedientRandomRequestGenerator;

// Don't move this class
public class ApplicationStarter {
    private static final Controller controller = new UserController();

    private static final RequestDataGenerator generator = new RandomRequestDataGenerator();
    private static final ObedientRandomRequestGenerator obedientGenerator = new ObedientRandomRequestGenerator();

    public static void main(String[] args) {

        //Obedient generator settings
        obedientGenerator.setValueCanBeNull(false);
        obedientGenerator.setValueCanBeEmpty(false);

        obedientGenerator.setUserCanBeNull(false);
        obedientGenerator.setRequestCanBeEmpty(false);

        obedientGenerator.setIdCanBeInvalid(true);
        obedientGenerator.setEmailCanBeInvalid(false);
        obedientGenerator.setUsernameCanBeInvalid(false);
        obedientGenerator.setFullNameCanBeInvalid(true);
        obedientGenerator.setAgeCanBeInvalid(true);
        obedientGenerator.setNoteCanBeInvalid(true);
        obedientGenerator.setFollowersCanBeInvalid(true);

        int i = 0;

        while (i < 10000) {

            Request request = obedientGenerator.generate();
            Response response = controller.process(request);


//            Request request = generator.generate();
//            Response response = controller.process(request);

            System.out.println(response);
            i++;
        }
    }
}