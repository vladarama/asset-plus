package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet2Controller;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UpdateAssetTypeController {

  @FXML
  private TextField updateName;

  @FXML
  private TextField updateExpectedLifespan;

  @FXML
  private Button cancelUpdateAssetType;

  @FXML
  private Button updateAssetType;

  @FXML
  private AnchorPane updateAssetTypeAnchorPane;

  @FXML
  private String oldName;

  @FXML
  void cancelUpdateAssetTypeClicked(ActionEvent event) {
    updateName.setText("");
    updateExpectedLifespan.setText("");
    sceneSwitch(updateAssetTypeAnchorPane, "../pages/AssetTypeView.fxml");
  }

  @FXML
  void updateAssetTypeClicked(ActionEvent event) {
    try {
      String name = updateName.getText();
      Integer expectedLifespan = Integer.parseInt(updateExpectedLifespan.getText());
      if (successful(
          AssetPlusFeatureSet2Controller.updateAssetType(this.oldName, name, expectedLifespan))) {
        updateName.setText("");
        updateExpectedLifespan.setText("");
        sceneSwitch(updateAssetTypeAnchorPane, "../pages/AssetTypeView.fxml");
      }
    } catch (NumberFormatException e) {
      showError("Invalid number format: " + e.getMessage() + " .Should be an integer.");
    }
  }

  public void setName(String name) {
    this.oldName = name;
    updateName.setText(name);
  }

  public void setExpectedLifeSpan(Integer expectedLifespan) {
    updateExpectedLifespan.setText(expectedLifespan != null ? expectedLifespan.toString() : "");
  }
}
