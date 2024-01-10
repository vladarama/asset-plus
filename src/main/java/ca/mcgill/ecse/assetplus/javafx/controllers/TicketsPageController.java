// package ca.mcgill.ecse.assetplus.controller;
package ca.mcgill.ecse.assetplus.javafx.controllers;

import java.io.IOException;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet4Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusTicketingController;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceNote;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceTicket;
import ca.mcgill.ecse.assetplus.controller.TOUser;
import ca.mcgill.ecse.assetplus.controller.Util;
import ca.mcgill.ecse.assetplus.javafx.AssetPlusFxmlView;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class TicketsPageController {

  @FXML
  private AnchorPane maintenanceTicketsViewAnchorPane;
  // Table and columns
  @FXML
  private TableView<TOMaintenanceTicket> ticketsTable;
  @FXML
  private TableColumn<TOMaintenanceTicket, Integer> id;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> status;
  @FXML
  private TableColumn<TOMaintenanceTicket, Date> raisedOnDate;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> description;
  @FXML
  private TableColumn<TOMaintenanceTicket, Integer> floorNumber;
  @FXML
  private TableColumn<TOMaintenanceTicket, Integer> roomNumber;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> assetName;
  @FXML
  private TableColumn<TOMaintenanceTicket, List<String>> imageURLs;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> raisedByEmail;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> priority;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> fixedByEmail;
  @FXML
  private TableColumn<TOMaintenanceTicket, String> timeToResolve;
  @FXML
  private TableColumn<TOMaintenanceTicket, Boolean> approvalRequired;
  @FXML
  private TableColumn<TOMaintenanceTicket, Integer> expectLifeSpanInDays;
  @FXML
  private TableColumn<TOMaintenanceTicket, List<TOMaintenanceNote>> notes;

  // Buttons and fields
  @FXML
  private TextField hotelStaffIDTextField;
  @FXML 
  private DatePicker datePicker;
  private Button updateSelectedButton;
  @FXML
  private TextField enteredstaffID;
  @FXML
  private Button startButton;
  @FXML
  private Button completeButton;
  @FXML
  private Button disapproveWork;
  @FXML
  private Button approveWork;
  private Button addTicketButton;
  

  @FXML
  private Button deleteButton;
  @FXML
  private Button imagesButton;




  // For accessing all the maintenance tickets
  ObservableList<TOMaintenanceTicket> maintenanceTicketsList;

  public void refresh() {
    // set text to ""
    maintenanceTicketsList =
        FXCollections.observableArrayList(AssetPlusFeatureSet6Controller.getTickets());
    id.setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, Integer>("id"));
    status.setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, String>("status"));
    raisedOnDate
        .setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, Date>("raisedOnDate"));
    description
        .setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, String>("description"));
    floorNumber
        .setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, Integer>("floorNumber"));
    roomNumber
        .setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, Integer>("roomNumber"));
    assetName
        .setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, String>("assetName"));
    imageURLs.setCellValueFactory(
        new PropertyValueFactory<TOMaintenanceTicket, List<String>>("imageURLs"));
    raisedByEmail.setCellValueFactory(
        new PropertyValueFactory<TOMaintenanceTicket, String>("raisedByEmail"));
    priority.setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, String>("priority"));
    fixedByEmail
        .setCellValueFactory(new PropertyValueFactory<TOMaintenanceTicket, String>("fixedByEmail"));
    timeToResolve.setCellValueFactory(
        new PropertyValueFactory<TOMaintenanceTicket, String>("timeToResolve"));
    approvalRequired.setCellValueFactory(
        new PropertyValueFactory<TOMaintenanceTicket, Boolean>("approvalRequired"));
    expectLifeSpanInDays.setCellValueFactory(
        new PropertyValueFactory<TOMaintenanceTicket, Integer>("expectLifeSpanInDays"));
    notes.setCellValueFactory(
        new PropertyValueFactory<TOMaintenanceTicket, List<TOMaintenanceNote>>("notes"));
    ticketsTable.setItems(maintenanceTicketsList);
  }

  public void initialize() {
    refresh();
  }

  public List<TOMaintenanceTicket> getAllTickets() {
    return new ArrayList<>(maintenanceTicketsList);
  }

  @FXML
  public void sortByStaffIDButton(ActionEvent event) { 
    String staffIdentifier = hotelStaffIDTextField.getText();
    if (staffIdentifier == "") {
      refresh();
    } else {
      List<TOMaintenanceTicket> sortedTickets = new ArrayList<>();
      for (TOMaintenanceTicket ticket : AssetPlusFeatureSet6Controller.getTickets()) {
        String theEmail = ticket.getRaisedByEmail(); 
        if (theEmail != null){
        if (theEmail.equals(staffIdentifier)) {
          sortedTickets.add(ticket);
        }}
      }
      ticketsTable.setItems(FXCollections.observableArrayList(sortedTickets));
    }
  }
  @FXML
  public void sortByDate(ActionEvent event) { 
    LocalDate selectedDate = datePicker.getValue();
    if (selectedDate == null) {
      ticketsTable.setItems(FXCollections.observableArrayList(AssetPlusFeatureSet6Controller.getTickets()));
    }
    else{
      var date = Date.valueOf(selectedDate);
      List<TOMaintenanceTicket> sortedTickets = new ArrayList<>();
      for (TOMaintenanceTicket ticket : AssetPlusFeatureSet6Controller.getTickets()) {
        Date theDate = ticket.getRaisedOnDate();
        if (theDate.equals(date)) {
          sortedTickets.add(ticket);
        }
      }
      ticketsTable.setItems(FXCollections.observableArrayList(sortedTickets));
    }    
  }
  
  @FXML
  void deleteButtonClicked(ActionEvent event) {
    try {
      AssetPlusFeatureSet4Controller.deleteMaintenanceTicket(ticketsTable.getSelectionModel().getSelectedItem().getId());
      refresh();
    } catch (Exception e) {
      showError(e.toString());
    }
  }

  @FXML
  void startClicked(ActionEvent event) throws IOException {
    Integer ticketId = ticketsTable.getSelectionModel().getSelectedItem().getId();
    if (successful(Util.startTicket(ticketId))) {
      sceneSwitch(maintenanceTicketsViewAnchorPane, "../pages/TicketsPage.fxml");
    }
  }

  @FXML
  void addButtonClicked(ActionEvent event) {
    sceneSwitch(maintenanceTicketsViewAnchorPane, "../pages/AddMaintanceTicket.fxml");
  }

  @FXML
  void updateButtonClicked(ActionEvent event) {
    try {
      FXMLLoader l =
          new FXMLLoader(getClass().getResource("../pages/ReviewMaintenanceTicket.fxml"));
      AnchorPane nextPane = (AnchorPane) l.load();
      ReviewMaintenanceTicket c = l.getController();
      c.initialize(ticketsTable.getSelectionModel().getSelectedItem());
      maintenanceTicketsViewAnchorPane.getChildren().removeAll();
      System.out.println("Reached");
      maintenanceTicketsViewAnchorPane.getChildren().setAll(nextPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void completeClicked(ActionEvent event) {
    Integer ticketId = ticketsTable.getSelectionModel().getSelectedItem().getId();
    if (successful(Util.completeTicket(ticketId))) {
      sceneSwitch(maintenanceTicketsViewAnchorPane, "../pages/TicketsPage.fxml");
    }
  }

  @FXML
  void disapproveSelectedTicket(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/DisapproveWork.fxml"));
      AnchorPane nextAnchorPane = (AnchorPane) loader.load();
      DisapproveWorkController disapproveWorkController = loader.getController();
      disapproveWorkController
          .getSelectedTicketID((ticketsTable.getSelectionModel().getSelectedItem()).getId());
      maintenanceTicketsViewAnchorPane.getChildren().removeAll();
      maintenanceTicketsViewAnchorPane.getChildren().setAll(nextAnchorPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void approveSelectedTicket(ActionEvent event) {
    Integer ticketId = ticketsTable.getSelectionModel().getSelectedItem().getId();
    if (successful(AssetPlusTicketingController.approveWorkOnMaintenanceTicket(ticketId))) {
      sceneSwitch(maintenanceTicketsViewAnchorPane, "../pages/TicketsPage.fxml");
    }
  }

  @FXML
  void goAddDelImage(ActionEvent event) {
      sceneSwitch(maintenanceTicketsViewAnchorPane, "../pages/ImagesPage.fxml");
  }
}


