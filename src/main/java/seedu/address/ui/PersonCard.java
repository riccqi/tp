package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.buyer.Buyer;
import seedu.address.model.characteristics.Characteristics;
import seedu.address.model.pricerange.PriceRange;

import javax.swing.text.DateFormatter;
import java.time.format.DateTimeFormatter;

/**
 * An UI component that displays information of a {@code Buyer}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on PersonBook level 4</a>
     */

    public final Buyer buyer;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;

    @FXML
    private Label createdTime;
    @FXML
    private FlowPane priority;

    // TODO: set the following labels as optional
    @FXML
    private Label priceRange;
    @FXML
    private Label desiredCharacteristics;

    /**
     * Creates a {@code PersonCode} with the given {@code Buyer} and index to display.
     */
    public PersonCard(Buyer buyer, int displayedIndex) {
        super(FXML);
        this.buyer = buyer;
        id.setText(displayedIndex + ". ");
        name.setText(buyer.getName().fullName);
        phone.setText(buyer.getPhone().value);
        address.setText(buyer.getAddress().value);
        email.setText(buyer.getEmail().value);
        //createdTime.setText(buyer.getCreatedTime().format(DateTimeFormatter.ISO_DATE_TIME));
        priority.getChildren().add(new Label(buyer.getPriority().toString()));
        priceRange.setText("Budget: " + buyer.getPriceRange()
                .map(PriceRange::toString).orElse("Not Specified"));
        desiredCharacteristics.setText("Desired Characteristics: " + buyer
                .getDesiredCharacteristics().map(Characteristics::toString)
                .orElse("Not Specified"));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && buyer.equals(card.buyer);
    }
}
