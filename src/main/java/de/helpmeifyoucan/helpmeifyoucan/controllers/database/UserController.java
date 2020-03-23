package de.helpmeifyoucan.helpmeifyoucan.controllers.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import de.helpmeifyoucan.helpmeifyoucan.models.Address;
import de.helpmeifyoucan.helpmeifyoucan.models.User;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UserController extends AbstractEntityController<User> {

    private final MongoCollection<User> collection = database.getCollection("users", User.class);

    private final AddressController addressController = new AddressController();


    public UserController() {
        IndexOptions options = new IndexOptions();
        options.unique(true);
        collection.createIndex(Indexes.ascending("email"), options);

    }

    public void save(User user) {
        super.save(collection, user);
    }

    public User get(ObjectId id) {
        return super.getById(collection, id);
    }

    public void update(ObjectId id, User user) {
        var filter = new Document("_id", id);
        super.updateExisting(this.collection, filter, user);
    }

    public void delete(ObjectId id) {
        super.delete(this.collection, id);
    }

    private void addAddress(ObjectId userId, ObjectId addressId) {

        var user = super.getById(this.collection, userId);
        var addresses = getAddresses(user);

        addresses.add(addressId);
        this.update(user.getId(), user.setAddresses(addresses));
    }

    public void handleUserAddressAdd(ObjectId id, Address address) {
        var dbAddress = addressController.exists(address);
        if (dbAddress.isPresent()) {
            addAddress(id, dbAddress.get().getId());
        } else {
            addressController.save(address);
            addAddress(id, address.getId());
        }
    }

    public void deleteUserAddress(ObjectId userId, Address address) {

        var user = super.getById(this.collection, userId);
        var addresses = getAddresses(user);

        addresses.remove(addresses);
        this.update(user.getId(), user.setAddresses(addresses));

    }

    public List<ObjectId> getAddresses(User user) {

        List<ObjectId> addresses = new ArrayList<>(user.getAddresses());
        return addresses;
    }


}