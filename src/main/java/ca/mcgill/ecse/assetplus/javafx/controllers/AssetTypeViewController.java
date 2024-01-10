package ca.mcgill.ecse.assetplus.javafx.controllers;


import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import ca.mcgill.ecse.assetplus.javafx.AssetPlusFxmlView;
import ca.mcgill.ecse.assetplus.javafx.controllers.UpdateUserController;
import ca.mcgill.ecse.assetplus.model.AssetType;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet2Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet4Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.TOAsset;
import ca.mcgill.ecse.assetplus.controller.TOAssetType;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceTicket;
import ca.mcgill.ecse.assetplus.controller.TOUser;
import ca.mcgill.ecse.assetplus.controller.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class AssetTypeViewController {

  @FXML
  private TableColumn<TOAssetType, String> name;

  @FXML
  private TableColumn<TOAssetType, Integer> expectedLifespan;

  @FXML
  private Button addAssetType;

  @FXML
  private Button updateAssetType;

  @FXML
  private Button deleteAssetType;

  @FXML
  private TableView<TOAssetType> assetTypeTableView;

  @FXML
  private AnchorPane assetTypeViewAnchorPane;

  ObservableList<TOAssetType> assetTypeList =
      FXCollections.observableArrayList(TOAssetType.getAllAssetTypes());

  public void refresh() {
    name.setCellValueFactory(new PropertyValueFactory<TOAssetType, String>("name"));
    expectedLifespan
        .setCellValueFactory(new PropertyValueFactory<TOAssetType, Integer>("expectedLifespan"));
    assetTypeTableView.setItems(assetTypeList);
  }

  public void initialize() {
    refresh();
  }


  @FXML
  void addAssetTypeClicked(ActionEvent event) {
    sceneSwitch(assetTypeViewAnchorPane, "../pages/AddAssetType.fxml");
  }

  @FXML
  void updateAssetTypeClicked(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/UpdateAssetType.fxml"));
      AnchorPane nextAnchorPane = (AnchorPane) loader.load();
      UpdateAssetTypeController updateAssetTypeController = loader.getController();
      updateAssetTypeController
          .setName((assetTypeTableView.getSelectionModel().getSelectedItem()).getName());
      updateAssetTypeController.setExpectedLifeSpan(
          (assetTypeTableView.getSelectionModel().getSelectedItem()).getExpectedLifespan());
      assetTypeViewAnchorPane.getChildren().removeAll();
      assetTypeViewAnchorPane.getChildren().setAll(nextAnchorPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  @FXML
  void deleteAssetTypeClicked(ActionEvent event) {
    try {
      TOAssetType selectedAssetType = assetTypeTableView.getSelectionModel().getSelectedItem();

      FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/TicketsPage.fxml"));
      AnchorPane nextAnchorPane = (AnchorPane) loader.load();
      TicketsPageController ticketsPageController = loader.getController();

      for (TOMaintenanceTicket ticket : ticketsPageController.getAllTickets()){
        if(ticket.getAssetName() != null && ticket.getAssetName().equals(selectedAssetType.getName())) {
                AssetPlusFeatureSet4Controller.deleteMaintenanceTicket(ticket.getId());
            }
      }

    ticketsPageController.refresh();

    AssetPlusFeatureSet2Controller.deleteAssetType(selectedAssetType.getName());
    sceneSwitch(assetTypeViewAnchorPane, "../pages/AssetTypeView.fxml");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
