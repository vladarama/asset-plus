package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet4Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusTicketingController;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import java.sql.Date;
import java.time.LocalDate;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.util.Calendar;



public class DisapproveWorkController {

    private int ticketID;

    @FXML
    private TextField disapproveWorkComments;

    @FXML
    private Button cancelDisapproveWork;

    @FXML
    private Button updateDisapproveWork;

    @FXML 
    private DatePicker disapproveWorkDate;

    @FXML
    private AnchorPane disapproveWorkAnchorPane;

    @FXML
    void cancelDisapproveWorkClicked(ActionEvent event) {
        disapproveWorkComments.setText("");
        sceneSwitch(disapproveWorkAnchorPane, "../pages/TicketsPage.fxml");
    }

    @FXML
    void updateDisapproveWorkClicked(ActionEvent event) { 
        String reason = disapproveWorkComments.getText();
        LocalDate theLocalDate = disapproveWorkDate.getValue();
        var theDate = Date.valueOf(theLocalDate);
        if (successful(AssetPlusTicketingController.disapproveWorkOnMaintenanceTicket(ticketID, theDate, reason))){ 
            disapproveWorkComments.setText(""); 
            sceneSwitch(disapproveWorkAnchorPane, "../pages/TicketsPage.fxml");
        }
    }

    public void getSelectedTicketID(int id) {
        ticketID = id;
    }


}
