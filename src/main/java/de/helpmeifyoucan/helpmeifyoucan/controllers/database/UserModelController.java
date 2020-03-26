package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.AddressModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import de.helpmeifyoucan.helpmeifyoucan.models.dtos.UserUpdate;
import de.helpmeifyoucan.helpmeifyoucan.utils.ErrorMessages;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

@Service
public class UserModelController extends AbstractModelController<UserModel> {

    private AddressModelController addressModelController;


    @Autowired
    public UserModelController(MongoDatabase database) {
        super(database);
        super.createCollection("users", UserModel.class);
        createIndex();
    }

    private void createIndex() {
        IndexOptions options = new IndexOptions();
        options.unique(true);

        super.createIndex(Indexes.ascending("email"), options);
    }

    public UserModel save(UserModel user) {
        return super.save(user);
    }

    public UserModel get(ObjectId id) {
        var user = super.getById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return user;
    }

    public UserModel getByEmail(String email) {
        var filter = Filters.eq("email", email);
        var user = super.getByFilter(filter);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return user;
    }

    public UserModel update(UserUpdate updatedFields, ObjectId id) {
        var filter = Filters.eq("_id", id);
        var updatedUser = super.updateExistingFields(filter, updatedFields.toFilter());
        if (updatedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
        return updatedUser;
    }

    public Optional<UserModel> getOptional(Bson filter) {
        return super.getOptional(filter);
    }

    public void delete(ObjectId id) {
        if (!super.delete(Filters.eq("_id", id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessages.USER_NOT_FOUND);
        }
    }

    public void handleUserAddressAddRequest(ObjectId id, AddressModel address) {
        var addressFilter = eq("hashCode", address.calculateHash().getHashCode());
        var dbAddress = addressModelController.getOptional(addressFilter);
        var user = this.get(id);
        if (dbAddress.isPresent()) {
            this.addAddressToUser(user, dbAddress.get());
        } else {
            addressModelController.save(address);
            this.addAddressToUser(user, address);
        }
    }

    public void handleUserAddressDeleteRequest(ObjectId userId, ObjectId addressId) {
        this.deleteAddressFromUser(this.get(userId), addressId);
    }

    // user address operations
    private void addAddressToUser(UserModel user, AddressModel address) {
        user.addAddress(address.getId());
        this.updateUserAddressField(user);
        addressModelController.addUserToAddress(address, user.getId());
    }

    public void exchangeAddress(ObjectId userId, ObjectId addressToDelete, ObjectId addressToAdd) {
        var user = this.get(userId);
        updateUserAddressField(user.removeAddress(addressToDelete).addAddress(addressToAdd));

    }

    public void deleteAddressFromUser(UserModel user, ObjectId address) {
        this.updateUserAddressField(user.removeAddress(address));
        addressModelController.handleUserControllerAddressDelete(address, user.getId());
    }

    private UserModel updateUserAddressField(UserModel user) {
        Bson updatedFields = set("address", user.getAddresses());

        var filter = Filters.eq("_id", user.getId());
        return this.updateExistingFields(filter, updatedFields);
    }

    @Autowired
    public void setAddressModelController(AddressModelController addressModelController) {
        this.addressModelController = addressModelController;
    }
}
