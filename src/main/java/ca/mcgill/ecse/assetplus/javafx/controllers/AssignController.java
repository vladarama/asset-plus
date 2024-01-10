package ca.mcgill.ecse.assetplus.javafx.controllers;

import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.showError;
import java.util.ArrayList;
import ca.mcgill.ecse.assetplus.controller.TOUser;
import ca.mcgill.ecse.assetplus.controller.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class AssignController {

    @FXML
    private Button assign;

    @FXML
    private ChoiceBox<TOUser> assignedHotelStaffSelect;

    @FXML
    private ChoiceBox<String> prioritySelect;

    @FXML
    private CheckBox requireApprovalSelect;

    @FXML
    private ChoiceBox<String> timeEstimateSelect;


    @FXML
    private TextField ticketIDUpdate;

    @FXML
    void assignClicked(ActionEvent event) {
        String email = assignedHotelStaffSelect.getValue().getEmail();
        if (!email.substring(email.length()-7).equals("@ap.com")) {
            showError("Ticket fixer must be an a hotel staff.");
            return;
        }
        String time = timeEstimateSelect.getValue();
        String priority = prioritySelect.getValue();
        int id = Integer.valueOf(ticketIDUpdate.getText(),10);
        Util.ticketAssignment(id, email, time, priority, requireApprovalSelect.isSelected());
    }

    public void initialize() {
        ArrayList<TOUser> user = TOUser.getAllUsers();
        assignedHotelStaffSelect.getItems().addAll(user);
        prioritySelect.getItems().addAll(Util.getPriorities());
        timeEstimateSelect.getItems().addAll(Util.getTimeEstimate());

    }

}
