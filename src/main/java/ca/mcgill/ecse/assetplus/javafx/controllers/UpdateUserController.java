package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UpdateUserController {

    @FXML
    private TextField UpdateEmail;

    @FXML
    private TextField UpdateName;

    @FXML
    private TextField UpdatePassword;

    @FXML
    private TextField UpdatePhone;

    @FXML
    private Button cancelUpdateUser;

    @FXML
    private Button updateUser;

    @FXML
    private AnchorPane updateUserAnchorPane;

    @FXML
    void cancelUpdateUserClicked(ActionEvent event) {
        UpdateEmail.setText("");
        UpdateName.setText("");
        UpdatePassword.setText("");
        UpdatePhone.setText("");
        sceneSwitch(updateUserAnchorPane, "../pages/UserView.fxml");
    }

    @FXML
    void updateUserClicked(ActionEvent event) {
        String name = UpdateName.getText();
        String email = UpdateEmail.getText();
        String password = UpdatePassword.getText();
        String phone = UpdatePhone.getText();
        if (successful(AssetPlusFeatureSet1Controller.updateEmployeeOrGuest(email, password, name, phone))) {
            UpdateEmail.setText("");
            UpdateName.setText("");
            UpdatePassword.setText("");
            UpdatePhone.setText("");
            sceneSwitch(updateUserAnchorPane, "../pages/UserView.fxml");
        }
    }

    public void setEmail(String email) {
        UpdateEmail.setText(email);
    }

    public void setName(String name) {
        UpdateName.setText(name);
    }

    public void setPhone(String phone) {
        UpdatePhone.setText(phone);
    }

    public void setPassword(String password) {
        UpdatePassword.setText(password);
    }

}
