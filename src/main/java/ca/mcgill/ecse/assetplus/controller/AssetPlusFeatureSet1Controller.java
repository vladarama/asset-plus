package ca.mcgill.ecse.assetplus.controller;

import java.util.List;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.Guest;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import ca.mcgill.ecse.assetplus.model.Employee;



public class AssetPlusFeatureSet1Controller {
  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();
  private AssetPlusFeatureSet1Controller() {};


  /**
   * Updates the manager's password
   * 
   * @author Li Yang Lei
   * @param password
   * @return Error message for failure and empty string for success
   */
  public static String updateManager(String password) {
    var error = "";
    try {
      // validates that the new password meet the requirements
      if (password==null || password.length()==0) {
        error = "Password cannot be empty";
      }
      else if (password.length()<4) {
        error = "Password must be at least four characters long";
      }
      else if (password.equals(password.toLowerCase())) {
        error = "Password must contain one upper-case character";
      }
      else if (password.equals(password.toUpperCase())) {
        error = "Password must contain one lower-case character";
      }
      else if (!(password.contains("!")|| password.contains("#") || password.contains("$"))) {
        error = "Password must contain one character out of !#$";
      }
      // return error for password that does not meet requirements
      if (error!="") {
        return error;
      }
      // otherwise, set update the password
      ap.getManager().setPassword(password);
      AssetPlusPersistence.save();
      return error;
    } catch (Exception e) {
      error = "An error has occured. Please try again: " + e.getMessage();
      return error;
    }
  }


  /**
   * Adds new employee or guest to the system
   * 
   * @author Li Yang Lei
   * @param email
   * @param password
   * @param name
   * @param phoneNumber
   * @param isEmployee
   * @return Error message if unsuccessful and empty string if successful
   */

  public static String addEmployeeOrGuest(String email, String password, String name, String phoneNumber,
        boolean isEmployee) {
          var error = "";
    try {
      // check if user is an employee
      if (isEmployee) {
        // input validation steps below
        if (email==null ||email.length()==0) {
          error = "Email cannot be empty";
          return error;
        }
        // check if employee already exists
        boolean employeeExists = false;
        for (Employee e: ap.getEmployees()) {
          if (email.equals(e.getEmail())) {
            employeeExists = true;
            break;
          }
        }
        // validations steps for the email
        int atCount = 0;
        boolean dot = false;
        boolean at = false;
        boolean inverted = false;
        for (int i=0; i<email.length(); i++) {
          if (email.charAt(i)=='@') {
            atCount++;
            at = true;
          }
          if (email.charAt(i)=='.') {
            dot = true;
          }
          // check if the order of @ and . is correct
          if (dot && !at) {
            inverted = true;
          }
        }
        if (email.equals("manager@ap.com")) {
          error = "Email cannot be manager@ap.com";
        } else if (employeeExists) {
          error = "Email already linked to an employee account";
        } else if (email.contains(" ")) {
          error = "Email must not contain any spaces";
        } else if (atCount>1 || email.charAt(email.length()-1)=='.' || email.charAt(0)=='@' || inverted || email.indexOf('.')-email.indexOf('@')==1) {
          error = "Invalid email";
        } else if (!(email.substring(email.length()-7).equals("@ap.com"))) {
          error = "Email domain must be @ap.com";
        } else if (password == null || password.equals("")) {
          error = "Password cannot be empty";
        }
        // if there is an error, return it; otherwise, add the new employee
        if (error!="") {
          return error;
        }
        ap.addEmployee(ap.addEmployee(email, name, password, phoneNumber));
      } else {
        // input validation for a new guest
        if (email==null ||email.length()==0) {
          error = "Email cannot be empty";
          return error;
        }
        boolean guestExists = false;
        for (Guest g: ap.getGuests()) {
          if (email.equals(g.getEmail())) {
            guestExists = true;
            break;
          }
        }
        int atCount = 0;
        boolean dot = false;
        boolean at = false;
        boolean inverted = false;
        for (int i=0; i<email.length(); i++) {
          if (email.charAt(i)=='@') {
            atCount++;
            at = true;
          }
          if (email.charAt(i)=='.') {
            dot = true;
          }
          if (dot && !at) {
            inverted = true;
          }
        }
        if (email.equals("manager@ap.com")) {
          error = "Email cannot be manager@ap.com";
        } else if (guestExists) {
          error = "Email already linked to a guest account";
        } else if (email.contains(" ")) {
          error = "Email must not contain any spaces";
        } else if (atCount>1 || email.charAt(email.length()-1)=='.' || email.charAt(0)=='@' || inverted || email.indexOf('.')-email.indexOf('@')==1) {
          error = "Invalid email";
        } else if ((email.substring(email.length()-7).equals("@ap.com"))) {
          error = "Email domain cannot be @ap.com";
        } else if (password == null || password.equals("")) {
          error = "Password cannot be empty";
        }
        if (error!="") {
          return error;
        }
        ap.addGuest(ap.addGuest(email, name, password, phoneNumber));
      }
      AssetPlusPersistence.save();
      return error;
    } catch (Exception e) {
      error = "An error has occured. Please try again: " + e.getMessage();
      return error;
    }
  }


  /**
   * Update the employee/guest information with the provided information
   * @author Li Yang Lei
   * @param email
   * @param newPassword
   * @param newName
   * @param newPhoneNumber
   * @return Error message if unsuccessful and empty string if successful
  */

  public static String updateEmployeeOrGuest(String email, String newPassword, String newName, String newPhoneNumber) {
    var error = "";
    try {
      if (newPassword==null || newPassword.length()==0) {
        error = "Password cannot be empty";
        return error;
      }
      // get all the employee/guest list, and go through them to find a matching email
      List<Employee> employeeList = ap.getEmployees();
      List<Guest> guestList = ap.getGuests();
      boolean isFound = false;
      for (int i=0; i<employeeList.size(); i++) {
        if (employeeList.get(i).getEmail().equals(email)){
          employeeList.get(i).setPassword(newPassword);
          employeeList.get(i).setPhoneNumber(newPhoneNumber);
          employeeList.get(i).setName(newName);
          isFound = true;
          break;
        }
      }
      if (!isFound) {
        for (int i=0; i<guestList.size(); i++) {
          if (guestList.get(i).getEmail().equals(email)){
            guestList.get(i).setPassword(newPassword);
            guestList.get(i).setPhoneNumber(newPhoneNumber);
            guestList.get(i).setName(newName);
            isFound = true;
            break;
          }
        }
      }
      // if nothing has been found, display that situation in the error
      if (!isFound) {
        error = "The email address does not match any user in our system. No update has been made.";
      }
      AssetPlusPersistence.save();
      return error;
    } catch (Exception e) {
      error = "An error has occured. Please try again: " + e.getMessage();
      return error;
    }
  }
}