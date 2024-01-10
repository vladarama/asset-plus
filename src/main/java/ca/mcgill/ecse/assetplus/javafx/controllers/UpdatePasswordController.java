package ca.mcgill.ecse.assetplus.javafx.controllers;

import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.successful;
import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UpdatePasswordController {

    @FXML
    private Button cancelUpdatePassword;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField newPasswordConfirm;

    @FXML
    private Button updatePassword;

    @FXML
    private AnchorPane updatePasswordAnchorPane;

    @FXML
    void cancelUpdatePasswordClicked(ActionEvent event) {
        sceneSwitch(updatePasswordAnchorPane, "../pages/UserView.fxml");
    }

    @FXML
    void updatePasswordClicked(ActionEvent event) {
        String password = newPassword.getText();
        String passowrdConfirm = newPasswordConfirm.getText();
        if (!password.equals(passowrdConfirm)) {
            ViewUtils.showError("Please ensure that the new password match.");
        } else if (successful(AssetPlusFeatureSet1Controller.updateManager(password))) {
            newPassword.setText("");
            newPasswordConfirm.setText("");
            sceneSwitch(updatePasswordAnchorPane, "../pages/UserView.fxml");
        }
    }

}
