package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import java.sql.Date;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.time.LocalDate;


public class UpdateAssetController {

    @FXML
    private TextField updateAssetType;

    @FXML
    private TextField updateFloorNumber;

    @FXML
    private TextField updateRoomNumber;

    @FXML
    private DatePicker updatePurchaseDate;

    @FXML
    private Button cancelUpdateAsset;

    @FXML
    private Button updateAsset;

    @FXML
    private AnchorPane updateAssetAnchorPane;

    @FXML
    private Integer assetNumber;

    @FXML
    private LocalDate originalDate;

    @FXML
    void cancelUpdateAssetClicked(ActionEvent event) {
        updateAssetType.setText("");
        updateFloorNumber.setText("");
        updateRoomNumber.setText("");
        updatePurchaseDate.setValue(this.originalDate);
        sceneSwitch(updateAssetAnchorPane, "../pages/AssetView.fxml");
    }

    @FXML
    void updateAssetClicked(ActionEvent event) {
        try {
            String assetType = updateAssetType.getText();
            int floorNumber = Integer.parseInt(updateFloorNumber.getText());
            Integer roomNumber = updateRoomNumber.getText() != null && !updateRoomNumber.getText().isEmpty() ? Integer.parseInt(updateRoomNumber.getText()) : -1;
            Date purchaseDate = Date.valueOf(updatePurchaseDate.getValue());
            if (successful(AssetPlusFeatureSet3Controller.updateSpecificAsset(this.assetNumber, floorNumber, roomNumber, purchaseDate, assetType))) {
                updateAssetType.setText("");
                updateFloorNumber.setText("");
                updateRoomNumber.setText("");
                updatePurchaseDate.setValue(LocalDate.now());
                sceneSwitch(updateAssetAnchorPane, "../pages/AssetView.fxml");
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format: " + e.getMessage() + " .Should be an integer.");
        } 
        // catch (IllegalArgumentException e) {
        //     showError("Invalid date format. Correct format should be: YYYY-MM-DD.");
        // }
    }

    public void setAssetType(String assetType) {
        updateAssetType.setText(assetType);
    }

    public void setFloorNumber(Integer floorNumber) {
      updateFloorNumber.setText(floorNumber != null ? floorNumber.toString() : "");
    }

    public void setRoomNumber(Integer roomNumber) {
      updateRoomNumber.setText(roomNumber != null ? roomNumber.toString() : "");
    }

    public void setPurchaseDate(Date purchaseDate) {
      if (purchaseDate != null) {
        updatePurchaseDate.setValue(purchaseDate.toLocalDate());
    } else {
        updatePurchaseDate.setValue(this.originalDate);
    }
    }

    public void setAssetNumber(Integer sameAssetNumber) {
        this.assetNumber = sameAssetNumber;
      }

}