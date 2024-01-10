package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet2Controller;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import java.sql.Date;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddAssetTypeController {

    @FXML
    private TextField addName;

    @FXML
    private TextField addExpectedLifespan;

    @FXML
    private Button addAssetType;

    @FXML
    private Button cancelAddAssetType;

    @FXML
    private AnchorPane addAssetTypeAnchorPane;

    @FXML
    void addAssetTypeClicked(ActionEvent event) {
        try {
            String name = addName.getText();
            Integer expectedLifespan = Integer.parseInt(addExpectedLifespan.getText());
            if (successful(AssetPlusFeatureSet2Controller.addAssetType(name, expectedLifespan))) {
                addName.setText("");
                addExpectedLifespan.setText("");
                sceneSwitch(addAssetTypeAnchorPane, "../pages/AssetTypeView.fxml");
            }
        } catch (NumberFormatException e) {
            showError("Invalid number format: " + e.getMessage() + " .Should be an integer.");
        }
    }

    @FXML
    void cancelAddAssetTypeClicked(ActionEvent event) {
        addName.setText("");
        addExpectedLifespan.setText("");
        sceneSwitch(addAssetTypeAnchorPane, "../pages/AssetTypeView.fxml");
    }

}
