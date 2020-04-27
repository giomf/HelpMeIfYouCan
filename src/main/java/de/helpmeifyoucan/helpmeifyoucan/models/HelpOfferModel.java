package de.helpmeifyoucan.helpmeifyoucan.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.listSerializers.ListAcceptedApplicationsSerializer;
import org.bson.types.ObjectId;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelpOfferModel extends AbstractHelpModel {

    @JsonSerialize(using = ListAcceptedApplicationsSerializer.class)
    protected List<HelpModelApplication> acceptedApplications;

    protected List<HelpCategoryEnum> categories;


    public HelpOfferModel() {
        this.categories = new LinkedList<>();
        this.acceptedApplications = new LinkedList<>();
    }

    @Override
    public HelpOfferModel setUserName(String userName) {
        this.userName = userName;
        return this;
    }


    public HelpOfferModel setApplications(List<HelpModelApplication> applications) {
        this.applications = applications;
        return this;
    }

    public List<HelpModelApplication> getAcceptedApplications() {
        return acceptedApplications;
    }

    public HelpOfferModel setAcceptedApplications(List<HelpModelApplication> acceptedApplications) {
        this.acceptedApplications = acceptedApplications;
        return this;
    }

    public HelpOfferModel setUser(ObjectId user) {
        this.user = user;
        return this;
    }

    public HelpOfferModel setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }


    public HelpOfferModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public HelpOfferModel setStatus(PostStatusEnum status) {
        this.status = status;
        return this;
    }

    @Override
    public List<HelpModelApplication> combineApplications() {
        return Stream.of(this.applications, this.acceptedApplications).flatMap(Collection::stream).collect(Collectors.toList());
    }

    public HelpOfferModel setDatePublished(Date date) {
        this.datePublished = date;
        return this;
    }

    public List<HelpCategoryEnum> getCategories() {
        return categories;
    }

    public HelpOfferModel setCategories(List<HelpCategoryEnum> categories) {
        this.categories = categories;
        return this;
    }

    public HelpOfferModel addCategory(HelpCategoryEnum category) {
        this.categories.add(category);

        return this;
    }

    public HelpModelApplication acceptApplication(ObjectId application) {
        //TODO
        var acceptedApplication = this.applications.stream().filter(x -> x.getId().equals(application)).findFirst().orElseThrow();
        this.acceptedApplications.add(acceptedApplication);
        this.applications.remove(acceptedApplication);
        return acceptedApplication;
    }


    public HelpOfferModel generateId() {
        this.setId(new ObjectId());
        this.datePublished = new Date();
        return this;
    }

    @Override
    public String toString() {
        return "HelpOfferModel{" +
                " id=" + id +
                ", categories=" + categories +
                ", user=" + user +
                ", coordinates=" + coordinates +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", datePublished=" + datePublished +

                '}';
    }
}
