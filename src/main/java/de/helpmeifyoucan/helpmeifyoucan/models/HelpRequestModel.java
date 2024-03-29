package de.helpmeifyoucan.helpmeifyoucan.models;

import de.helpmeifyoucan.helpmeifyoucan.utils.HelpCategoryEnum;
import de.helpmeifyoucan.helpmeifyoucan.utils.PostStatusEnum;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;

public class HelpRequestModel extends AbstractHelpModel {


    protected Date dateDue;

    protected HelpCategoryEnum category;


    public HelpRequestModel setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public HelpRequestModel setUser(ObjectId user) {
        this.user = user;
        return this;
    }

    public Date getDatePublished() {
        return datePublished;
    }

    public HelpRequestModel setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
        return this;
    }

    public HelpRequestModel setStatus(PostStatusEnum status) {
        this.status = status;
        return this;
    }

    public Date getDateDue() {
        return dateDue;
    }

    public HelpRequestModel setDateDue(Date dateDue) {
        this.dateDue = dateDue;
        return this;
    }

    public HelpRequestModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public HelpCategoryEnum getCategory() {
        return category;
    }

    public HelpRequestModel setCategory(HelpCategoryEnum category) {
        this.category = category;
        return this;
    }

    public HelpRequestModel generateId() {
        this.setId(new ObjectId());
        this.datePublished = new Date();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HelpRequestModel that = (HelpRequestModel) o;
        return getDatePublished().equals(that.getDatePublished()) &&
                Objects.equals(getDateDue(), that.getDateDue()) &&
                getCategory() == that.getCategory();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDatePublished(), getDateDue(), getCategory());
    }
}
