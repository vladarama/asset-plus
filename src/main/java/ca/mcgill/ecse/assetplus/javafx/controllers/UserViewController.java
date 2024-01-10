package ca.mcgill.ecse.assetplus.javafx.controllers;


import static ca.mcgill.ecse.assetplus.javafx.controllers.ViewUtils.sceneSwitch;
import ca.mcgill.ecse.assetplus.javafx.AssetPlusFxmlView;
import ca.mcgill.ecse.assetplus.javafx.controllers.UpdateUserController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet1Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
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

public class UserViewController {

    @FXML
    private TableColumn<TOUser, String> email;

    @FXML
    private TableColumn<TOUser, String> name;

    @FXML
    private TableColumn<TOUser, String> password;

    @FXML
    private TableColumn<TOUser, String> phone;

    @FXML
    private TableColumn<TOUser, String> userType;

    @FXML
    private Button addUser;

    @FXML
    private Button updatePassword;

    @FXML
    private Button deleteSelectedUser;

    @FXML
    private Button updateSelectedUser;

    @FXML
    private TableView<TOUser> userTableView;

    @FXML
    private AnchorPane userViewAnchorPane;

    ObservableList<TOUser> userList = FXCollections.observableArrayList(
        TOUser.getAllUsers()
    );

    public void refresh() {
        email.setCellValueFactory(new PropertyValueFactory<TOUser, String>("email"));
        name.setCellValueFactory(new PropertyValueFactory<TOUser, String>("name"));
        password.setCellValueFactory(new PropertyValueFactory<TOUser, String>("password"));
        phone.setCellValueFactory(new PropertyValueFactory<TOUser, String>("phoneNumber"));
        userType.setCellValueFactory(new PropertyValueFactory<TOUser, String>("userType"));
        userTableView.setItems(userList);
    }

    public void initialize() {
        refresh();
    }


    @FXML
    void addUserClicked(ActionEvent event) {
        sceneSwitch(userViewAnchorPane, "../pages/AddUser.fxml");
    }

    @FXML
    void updatePasswordClicked(ActionEvent event) {
        sceneSwitch(userViewAnchorPane, "../pages/UpdatePassword.fxml");
    }

    
    @FXML
    void deleteSelectedUserClicked(ActionEvent event) {
        TOUser selectedUser = userTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedUser.getName());
        AssetPlusFeatureSet6Controller.deleteEmployeeOrGuest(selectedUser.getEmail());
        sceneSwitch(userViewAnchorPane, "../pages/UserView.fxml");
    }

    
    @FXML
    void updateSelectedUserClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../pages/UpdateUser.fxml"));
            AnchorPane nextAnchorPane = (AnchorPane) loader.load();
            UpdateUserController updateUserController = loader.getController();
            updateUserController.setEmail((userTableView.getSelectionModel().getSelectedItem()).getEmail());
            updateUserController.setName((userTableView.getSelectionModel().getSelectedItem()).getName());
            updateUserController.setPassword((userTableView.getSelectionModel().getSelectedItem()).getPassword());
            updateUserController.setPhone((userTableView.getSelectionModel().getSelectedItem()).getPhoneNumber());
            userViewAnchorPane.getChildren().removeAll();
            userViewAnchorPane.getChildren().setAll(nextAnchorPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
