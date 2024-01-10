package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import ca.mcgill.ecse.assetplus.controller.TOAsset;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import java.sql.Date;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.time.LocalDate;

public class AddAssetController {

    @FXML
    private TextField addAssetNumber;

    @FXML
    private TextField addAssetType;

    @FXML
    private TextField addFloorNumber;

    @FXML
    private TextField addRoomNumber;

    @FXML
    private DatePicker addPurchaseDate;

    @FXML
    private Button addAsset;

    @FXML
    private Button cancelAddAsset;

    @FXML
    private AnchorPane addAssetAnchorPane;

    @FXML
    void addAssetClicked(ActionEvent event) {
        try {
            Integer assetNumber = Integer.parseInt(addAssetNumber.getText());
            String assetType = addAssetType.getText();
            Integer floorNumber = Integer.parseInt(addFloorNumber.getText());
            Integer roomNumber = addRoomNumber.getText() != null && !addRoomNumber.getText().isEmpty() ? Integer.parseInt(addRoomNumber.getText()) : -1;
            Date purchaseDate = Date.valueOf(addPurchaseDate.getValue());

            if (successful(AssetPlusFeatureSet3Controller.addSpecificAsset(assetNumber, floorNumber, roomNumber, purchaseDate, assetType))) {
                addAssetNumber.setText("");
                addAssetType.setText("");
                addFloorNumber.setText("");
                addRoomNumber.setText("");
                addPurchaseDate.setValue(LocalDate.now());
                sceneSwitch(addAssetAnchorPane, "../pages/AssetView.fxml");
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format: " + e.getMessage() + " .Should be an integer.");
        } 
    }

    @FXML
    void cancelAddAssetClicked(ActionEvent event) {
            addAssetNumber.setText("");
            addAssetType.setText("");
            addFloorNumber.setText("");
            addRoomNumber.setText("");
            addPurchaseDate.setValue(LocalDate.now());
            sceneSwitch(addAssetAnchorPane, "../pages/AssetView.fxml");
    }

}