package de.helpmeifyoucan.helpmeifyoucan.models.dtos.request;

import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations;
import de.helpmeifyoucan.helpmeifyoucan.validation.Annotations.ValidName;

public class Register extends Credentials {

    @ValidName
    private String name;
    @ValidName
    private String lastName;

    @Annotations.ValidPhone
    protected String phoneNr;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public Register setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
        return this;
    }
}