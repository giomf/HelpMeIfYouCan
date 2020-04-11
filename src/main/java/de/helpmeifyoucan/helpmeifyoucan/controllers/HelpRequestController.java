package de.helpmeifyoucan.helpmeifyoucan.controllers;

import de.helpmeifyoucan.helpmeifyoucan.models.HelpModelApplication;
import de.helpmeifyoucan.helpmeifyoucan.models.HelpRequestModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.CoordinatesUpdate;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.request.HelpRequestUpdate;
import de.helpmeifyoucan.helpmeifyoucan.services.HelpRequestModelService;
import de.helpmeifyoucan.helpmeifyoucan.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/requests")
public class HelpRequestController {

    private HelpRequestModelService helpRequestModelService;


    @Autowired
    public HelpRequestController(HelpRequestModelService helpRequestModelService, UserService userService) {
        this.helpRequestModelService = helpRequestModelService;
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel save(@RequestBody HelpRequestModel request) {
        return this.helpRequestModelService.saveNewModel(request, getIdFromContext());
    }


    @PatchMapping(path = "/{requestId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel update(@PathVariable ObjectId requestId, @RequestBody HelpRequestUpdate update) {
        return this.helpRequestModelService.update(requestId, update, getIdFromContext());
    }

    @PatchMapping(path = "/{requestId}/updateCoords/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel updateCoords(@PathVariable ObjectId requestId, @RequestBody CoordinatesUpdate update) {
        return this.helpRequestModelService.handleCoordinatesUpdate(requestId, update, getIdFromContext());
    }

    @DeleteMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel delete(@PathVariable ObjectId requestId) {
        return this.helpRequestModelService.deleteModel(requestId, getIdFromContext());
    }

    @GetMapping(path = "/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpRequestModel get(@PathVariable ObjectId requestId) {
        return this.helpRequestModelService.getById(requestId);
    }

    @GetMapping(path = "/getall", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HelpRequestModel> getAllById(@RequestBody List<ObjectId> ids) {
        return this.helpRequestModelService.getAllById(ids);
    }

    @PostMapping(path = "/{requestId}/apply", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HelpModelApplication applyToRequest(@PathVariable ObjectId requestId,
            @RequestBody HelpModelApplication application) {
        return this.helpRequestModelService.saveNewApplication(requestId, application, getIdFromContext());
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{requestId}/unapply")
    public void unApplyFromRequest(@PathVariable ObjectId requestId) {
        this.helpRequestModelService.deleteApplication(requestId, getIdFromContext());
    }

    @PatchMapping(path = "/{requestId}/{applicationId}/accept")
    public HelpModelApplication acceptApplication(@PathVariable ObjectId requestId,
                                                  @PathVariable ObjectId applicationId) {
        return this.helpRequestModelService.acceptApplication(requestId, applicationId, getIdFromContext());
    }


    //TODO get by user

    //TODO get by status

    private ObjectId getIdFromContext() {
        return (ObjectId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
