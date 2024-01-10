package ca.mcgill.ecse.assetplus.javafx.controllers;


import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import ca.mcgill.ecse.assetplus.javafx.AssetPlusFxmlView;
import ca.mcgill.ecse.assetplus.javafx.controllers.UpdateAssetController;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.List;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet4Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.TOAsset;
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

public class AssetViewController {

    @FXML
    private TableColumn<TOAsset, Integer> assetNumber;

    @FXML
    private TableColumn<TOAsset, String> assetType;

    @FXML
    private TableColumn<TOAsset, Integer> floorNumber;

    @FXML
    private TableColumn<TOAsset, Integer> roomNumber;

    @FXML
    private TableColumn<TOAsset, Date> purchaseDate;

    @FXML
    private Button addAsset;

    @FXML
    private Button deleteAsset;

    @FXML
    private Button updateAsset;

    @FXML
    private TableView<TOAsset> assetTableView;

    @FXML
    private AnchorPane assetViewAnchorPane;

    ObservableList<TOAsset> AssetList = FXCollections.observableArrayList(TOAsset.getAllAssets());

    ObservableList<TOMaintenanceTicket> maintenanceTicketsList = FXCollections.observableArrayList(AssetPlusFeatureSet6Controller.getTickets());

    public void refresh() {
        assetNumber.setCellValueFactory(new PropertyValueFactory<TOAsset, Integer>("assetNumber"));
        assetType.setCellValueFactory(new PropertyValueFactory<TOAsset, String>("assetType"));
        floorNumber.setCellValueFactory(new PropertyValueFactory<TOAsset, Integer>("floorNumber"));
        roomNumber.setCellValueFactory(new PropertyValueFactory<TOAsset, Integer>("roomNumber"));
        purchaseDate.setCellValueFactory(new PropertyValueFactory<TOAsset, Date>("purchaseDate"));
        assetTableView.setItems(AssetList);
    }

    public void initialize() {
        refresh();
    }

    public List<TOAsset> getAllAssets() {
        return new ArrayList<>(AssetList);
    }

    @FXML
    void addAssetClicked(ActionEvent event) {
        sceneSwitch(assetViewAnchorPane, "../pages/AddAsset.fxml");
    }


    @FXML
    void deleteAssetClicked(ActionEvent event) {
        try {
            TOAsset selectedAsset = assetTableView.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/TicketsPage.fxml"));
            AnchorPane nextAnchorPane = (AnchorPane) loader.load();
            TicketsPageController ticketsPageController = loader.getController();

            for (TOMaintenanceTicket ticket : ticketsPageController.getAllTickets()){
                if(ticket.getAssetName() != null && ticket.getAssetName().equals(selectedAsset.getAssetType()) &&
                    ticket.getFloorNumber() == (selectedAsset.getFloorNumber()) &&
                    ticket.getRoomNumber() == (selectedAsset.getRoomNumber())) {
                        AssetPlusFeatureSet4Controller.deleteMaintenanceTicket(ticket.getId());
                    }
            }

            ticketsPageController.refresh();
            AssetPlusFeatureSet3Controller.deleteSpecificAsset(selectedAsset.getAssetNumber());
            
            sceneSwitch(assetViewAnchorPane, "../pages/AssetView.fxml");
        } catch (Exception e) {
            e.printStackTrace();
          }
    }


    @FXML
    void updateAssetClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/UpdateAsset.fxml"));
            AnchorPane nextAnchorPane = (AnchorPane) loader.load();
            UpdateAssetController updateAssetController = loader.getController();
            updateAssetController.setAssetNumber(
                    (assetTableView.getSelectionModel().getSelectedItem()).getAssetNumber());
            updateAssetController.setAssetType(
                    (assetTableView.getSelectionModel().getSelectedItem()).getAssetType());
            updateAssetController.setFloorNumber(
                    (assetTableView.getSelectionModel().getSelectedItem()).getFloorNumber());
            updateAssetController.setRoomNumber(
                    (assetTableView.getSelectionModel().getSelectedItem()).getRoomNumber());
            updateAssetController.setPurchaseDate(
                    (assetTableView.getSelectionModel().getSelectedItem()).getPurchaseDate());
            assetViewAnchorPane.getChildren().removeAll();
            assetViewAnchorPane.getChildren().setAll(nextAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
