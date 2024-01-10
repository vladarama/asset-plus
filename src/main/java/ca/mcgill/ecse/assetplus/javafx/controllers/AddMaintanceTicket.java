package ca.mcgill.ecse.assetplus.javafx.controllers;

import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import java.sql.Date;
import java.time.LocalDate;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet4Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet5Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddMaintanceTicket {

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane addMaintanceTicketAnchor;

    @FXML
    private TextField assetNumberField;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField imageField;

    @FXML
    private TextField raiserField;

    @FXML
    private TextField ticketIdField;

    @FXML
    private DatePicker raisedByField;

    @FXML
    void addButtonClicked(ActionEvent event) {
        boolean s = false;

        try {
            s = successful(AssetPlusFeatureSet4Controller.addMaintenanceTicket(
                    Integer.parseInt(ticketIdField.getText()), Date.valueOf(raisedByField.getValue()),
                    descriptionField.getText(), raiserField.getText(),
                    Integer.valueOf(assetNumberField.getText())));
        } catch (Exception e) {
            showError(e.toString());
            return;
        }
        if (!s) {
            return;
        }

        if (!imageField.getText().isBlank()) {
            try {
                s = successful(AssetPlusFeatureSet5Controller.addImageToMaintenanceTicket(
                        imageField.getText(), Integer.parseInt(ticketIdField.getText())));
            } catch (Exception e) {
                showError(e.toString());
                return;
            }

            if (!s) {
                return;
            }
        }
        reset();
        sceneSwitch(addMaintanceTicketAnchor, "../pages/TicketsPage.fxml");
    }

    @FXML
    void cancelButtonClicked(ActionEvent event) {
        reset();
        sceneSwitch(addMaintanceTicketAnchor, "../pages/TicketsPage.fxml");
    }

    private void reset() {
        descriptionField.clear();
        imageField.clear();
        raiserField.clear();
        assetNumberField.clear();
        ticketIdField.clear();
        raisedByField.setValue(LocalDate.now());
    }
}
