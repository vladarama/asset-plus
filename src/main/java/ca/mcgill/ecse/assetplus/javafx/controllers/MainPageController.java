package ca.mcgill.ecse.assetplus.javafx.controllers;

import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import ca.mcgill.ecse.assetplus.javafx.AssetPlusFxmlView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class MainPageController {
  @FXML
  private ToggleGroup dashboardGroup;
  @FXML
  private ToggleButton dashboardUsersButton;
  @FXML
  private ToggleButton dashboardAssetsButton;
  @FXML
  private ToggleButton dashboardAssetTypesButton;
  @FXML
  private ToggleButton dashboardTicketsButton;
  @FXML
  private ToggleButton dashboardImagesButton;
  // Panes
  @FXML
  private Pane usersPane;
  @FXML
  private Pane imagesPane;
  @FXML
  private Pane ticketsPane;
  @FXML
  private Pane assetTypesPane;
  @FXML
  private Pane assetsPane;
  @FXML
  private Pane maintenanceTicketsPane;

  @FXML
  public void initialize() {
    // always keep one button selected
    dashboardGroup.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
      if (newVal == null)
        oldVal.setSelected(true);
    });
  }

  @FXML
  public void dashboardUsersSelected(ActionEvent event) {
    usersPane.toFront();
    sceneSwitch((AnchorPane) usersPane, "../pages/UserView.fxml");
  }

  @FXML
  public void dashboardImagesSelected(ActionEvent event) {
    imagesPane.toFront();
    sceneSwitch((AnchorPane) imagesPane, "../pages/ImagesPage.fxml");
  }

  @FXML
  public void dashboardTicketsSelected(ActionEvent event) { 
    ticketsPane.toFront(); 
    sceneSwitch((AnchorPane) ticketsPane, "../pages/TicketsPage.fxml");
  }

  @FXML
  public void dashboardAssetTypesSelected(ActionEvent event) {
    assetTypesPane.toFront();
    sceneSwitch((AnchorPane) assetTypesPane, "../pages/AssetTypeView.fxml");
  }

  @FXML
  public void dashboardAssetsSelected(ActionEvent event) {
    assetsPane.toFront();
    sceneSwitch((AnchorPane) assetsPane, "../pages/AssetView.fxml");
  }

  public void dashboardViewMaintenanceTicketsSelected(ActionEvent event) {
    maintenanceTicketsPane.toFront();
    sceneSwitch((AnchorPane) maintenanceTicketsPane, "../pages/TicketsPage.fxml");
  }

}
