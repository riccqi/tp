package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.buyer.Buyer;
import seedu.address.model.property.Property;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final UserPrefs userPrefs;
    private final PersonBook personBook;
    private final PropertyBook propertyBook;
    private final FilteredList<Buyer> filteredBuyers;
    private final FilteredList<Property> filteredProperties;


    /**
     * Initializes a ModelManager with the given personBook and userPrefs.
     */
    public ModelManager(ReadOnlyPersonBook personModel, ReadOnlyPropertyBook propertyModel,
                        ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(personModel, propertyModel, userPrefs);

        logger.fine("Initializing with buyer model: " + personModel + " and property model: " + propertyModel
                + " and user prefs " + userPrefs);

        this.personBook = new PersonBook(personModel);
        this.propertyBook = new PropertyBook(propertyModel);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredBuyers = new FilteredList<>(this.personBook.getPersonList());
        filteredProperties = new FilteredList<>(this.propertyBook.getPropertyList());
    }

    public ModelManager() {
        this(new PersonBook(), new PropertyBook(), new UserPrefs());
    }


    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getPersonModelFilePath() {
        return userPrefs.getPersonBookFilePath();
    }

    @Override
    public void setPersonModelFilePath(Path personModelFilePath) {
        requireNonNull(personModelFilePath);
        userPrefs.setPersonBookFilePath(personModelFilePath);
    }

    @Override
    public Path getPropertyModelFilePath() {
        return userPrefs.getPropertyBookFilePath();
    }

    @Override
    public void setPropertyModelFilePath(Path propertyModelFilePath) {
        requireNonNull(propertyModelFilePath);
        userPrefs.setPropertyBookFilePath(propertyModelFilePath);
    }

    //=========== PersonBook ================================================================================

    @Override
    public void setPersonModel(ReadOnlyPersonBook personModel) {
        this.personBook.resetData(personModel);
    }

    @Override
    public ReadOnlyPersonBook getPersonModel() {
        return personBook;
    }

    @Override
    public boolean hasPerson(Buyer buyer) {
        requireNonNull(buyer);
        return personBook.hasPerson(buyer);
    }

    @Override
    public void deletePerson(Buyer target) {
        personBook.removePerson(target);
    }

    @Override
    public void addPerson(Buyer buyer) {
        personBook.addPerson(buyer);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Buyer target, Buyer editedBuyer) {
        requireAllNonNull(target, editedBuyer);
        personBook.setPerson(target, editedBuyer);
    }

    //=========== Filtered Buyer List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Buyer} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Buyer> getFilteredPersonList() {
        return filteredBuyers;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Buyer> predicate) {
        requireNonNull(predicate);
        filteredBuyers.setPredicate(predicate);
    }

    @Override
    public void sortBuyerList(Comparator<Buyer> comparator) {
        requireNonNull(comparator);
        List<Buyer> sortedList = new ArrayList<>(personBook.getPersonList());
        sortedList.sort(comparator);
        personBook.setPersons(sortedList);
    }


    //=========== PropertyBook ================================================================================

    @Override
    public void setPropertyModel(ReadOnlyPropertyBook propertyModel) {
        this.propertyBook.resetData(propertyModel);
    }

    @Override
    public ReadOnlyPropertyBook getPropertyModel() {
        return propertyBook;
    }

    @Override
    public boolean hasProperty(Property property) {
        requireNonNull(property);
        return propertyBook.hasProperty(property);
    }

    @Override
    public void deleteProperty(Property target) {
        propertyBook.removeProperty(target);
    }

    @Override
    public void addProperty(Property property) {
        propertyBook.addProperty(property);
        updateFilteredPropertyList(PREDICATE_SHOW_ALL_PROPERTIES);
    }

    @Override
    public void setProperty(Property target, Property editedProperty) {
        requireAllNonNull(target, editedProperty);
        propertyBook.setProperty(target, editedProperty);
    }

    //=========== Filtered Property List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Property} backed by the internal list of
     * {@code PropertyBook}
     */
    @Override
    public ObservableList<Property> getFilteredPropertyList() {
        return filteredProperties;
    }

    @Override
    public void updateFilteredPropertyList(Predicate<Property> predicate) {
        requireNonNull(predicate);
        filteredProperties.setPredicate(predicate);
    }

    @Override
    public void sortPropertyList(Comparator<Property> comparator) {
        requireNonNull(comparator);
        List<Property> sortedList = new ArrayList<>(propertyBook.getPropertyList());
        sortedList.sort(comparator);
        propertyBook.setProperties(sortedList);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;

        return userPrefs.equals(other.userPrefs)
                && personBook.equals(other.personBook)
                && propertyBook.equals(other.propertyBook)
                && filteredBuyers.equals(other.filteredBuyers)
                && filteredProperties.equals(other.filteredProperties);
    }
}
