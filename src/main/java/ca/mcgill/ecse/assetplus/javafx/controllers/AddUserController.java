package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddUserController {

    @FXML
    private TextField AddEmail;

    @FXML
    private TextField AddName;

    @FXML
    private TextField AddPassword;

    @FXML
    private TextField AddPhone;

    @FXML
    private Button AddUser;

    @FXML
    private Button cancelAddUser;

    @FXML
    private CheckBox isEmployeeCheckBox;

    @FXML
    private AnchorPane addUserAnchorPane;

    @FXML
    void addUserClicked(ActionEvent event) {
        String name = AddName.getText();
        String email = AddEmail.getText();
        String password = AddPassword.getText();
        String phone = AddPhone.getText();
        if (isEmployeeCheckBox.isSelected()) {
            if (successful(AssetPlusFeatureSet1Controller.addEmployeeOrGuest(email, password, name, phone, true))) {
                AddEmail.setText("");
                AddName.setText("");
                AddPassword.setText("");
                AddPhone.setText("");
                sceneSwitch(addUserAnchorPane, "../pages/UserView.fxml");
            }
        } else {
            if (successful(AssetPlusFeatureSet1Controller.addEmployeeOrGuest(email, password, name, phone, false))) {
                AddEmail.setText("");
                AddName.setText("");
                AddPassword.setText("");
                AddPhone.setText("");
                sceneSwitch(addUserAnchorPane, "../pages/UserView.fxml");
            }
        }
        

    }

    @FXML
    void cancelAddUserClicked(ActionEvent event) {
            AddEmail.setText("");
            AddName.setText("");
            AddPassword.setText("");
            AddPhone.setText("");
            sceneSwitch(addUserAnchorPane, "../pages/UserView.fxml");
    }

}
