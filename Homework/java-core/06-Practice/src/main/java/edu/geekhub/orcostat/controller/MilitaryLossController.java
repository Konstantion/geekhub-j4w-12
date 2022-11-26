package edu.geekhub.orcostat.controller;

import edu.geekhub.orcostat.model.entity.Drone;
import edu.geekhub.orcostat.model.entity.Missile;
import edu.geekhub.orcostat.model.entity.Orc;
import edu.geekhub.orcostat.model.entity.Tank;
import edu.geekhub.orcostat.model.request.Request;
import edu.geekhub.orcostat.model.request.Response;
import edu.geekhub.orcostat.service.MilitaryLossService;

public class MilitaryLossController {
    private final MilitaryLossService service;

    public MilitaryLossController(MilitaryLossService service) {
        this.service = service;
    }


    public Response addMilitary(Request request) {
        Object data = request.getData();
        try {
            if (tryToAddMilitaryToService(data)) {
                return Response.ok(String.format(
                        "Successfully saved [%s]",
                        data
                ));
            } else {
                return Response.fail(String.format(
                                "Cannot find Military unit [%s]",
                                data
                        )
                );
            }
        } catch (IllegalArgumentException e) {
            return Response.fail(String.format(
                    "Illegal argument [%s]%n" +
                    "Try again",
                    e.getMessage()
            ));
        }

    }


    private boolean tryToAddMilitaryToService(Object data) {
        if (data instanceof Orc orc) {
            service.addNegativelyAliveOrc(orc);
        } else if (data instanceof Tank tank) {
            service.addDestroyedTank(tank);
        } else if (data instanceof Drone drone) {
            service.addDestroyedDrone(drone);
        } else if (data instanceof Missile missile) {
            service.addDestroyedMissile(missile);
        } else {
            return false;
        }
        return true;
    }
}
