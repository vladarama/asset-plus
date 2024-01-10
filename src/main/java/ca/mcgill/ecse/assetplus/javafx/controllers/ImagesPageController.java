package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet5Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ImagesPageController {

  @FXML
  private AnchorPane imageAnchorPane;

  @FXML
  private TextField addImageUrlField;

  @FXML
  private TextField addTicketIdField;

  @FXML
  private TextField delImageUrlField;

  @FXML
  private TextField delTicketIdField;

  @FXML
  private Button addImageButton;

  @FXML
  private Button deleteImageButton;

  @FXML
  private Button addClearButton;

  @FXML
  private Button delClearButton;

  @FXML
  private Button addDoneButton;

  @FXML
  private Button delDoneButton;


  @FXML
  public void addDoClear(ActionEvent event) {
    addDoClear();
  }


  @FXML
  public void delDoClear(ActionEvent event) {
    delDoClear();
  }

  @FXML
  public void doAddImage(ActionEvent event) {
    try {
      String imageURL = addImageUrlField.getText();
      Integer ticketID = Integer.parseInt(addTicketIdField.getText());

      if (ViewUtils.successful(
          AssetPlusFeatureSet5Controller.addImageToMaintenanceTicket(imageURL, ticketID))) {
          addDoClear();
        // sceneSwitch(addAssetAnchorPane, "../pages/AssetView.fxml");
      }
    } catch (NumberFormatException e) {
      ViewUtils.showError("Invalid number format: " + e.getMessage() + " .Should be an integer.");
    }
  }

  @FXML
  public void doDelImage(ActionEvent event) {
    try {
      String imageURL = delImageUrlField.getText();
      Integer ticketID = Integer.parseInt(delTicketIdField.getText());
      AssetPlusFeatureSet5Controller.deleteImageFromMaintenanceTicket(imageURL, ticketID);
      delDoClear();

    } catch (NumberFormatException e) {
      ViewUtils.showError("Invalid number format: " + e.getMessage() + " .Should be an integer.");
    } catch (RuntimeException e) {
      ViewUtils.showError(e.getMessage());
    }
  }

  @FXML
  public void addDoDone(ActionEvent event) {
    addDoClear();
    ViewUtils.sceneSwitch(imageAnchorPane, "../pages/TicketsPage.fxml");
  }

  @FXML
  public void delDoDone(ActionEvent event) {
    delDoClear();
    ViewUtils.sceneSwitch(imageAnchorPane, "../pages/TicketsPage.fxml");
  }

  private void addDoClear() {
    addImageUrlField.clear();
    addTicketIdField.clear();
  }

  private void delDoClear() {
    delImageUrlField.clear();
    delTicketIdField.clear();
  }

}
