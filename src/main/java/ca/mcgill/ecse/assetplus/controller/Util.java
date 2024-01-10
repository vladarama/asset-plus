package ca.mcgill.ecse.assetplus.controller;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.Employee;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.PriorityLevel;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.TimeEstimate;
import javafx.scene.layout.Priority;

public class Util {
  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();

  public static User getUser(String email) {
    ArrayList<User> allUsers = new java.util.ArrayList<>(Collections.emptyList());
    allUsers.addAll(ap.getGuests());
    allUsers.addAll(ap.getEmployees());
    allUsers.add(ap.getManager());

    for (User u: allUsers) {
      if (u.getEmail().equals(email)) {
        return u;
      }
    }
    return null;
  }

  public static SpecificAsset getSpecificAsset(int id) {
    List<SpecificAsset> allAsset = ap.getSpecificAssets();
    for (SpecificAsset a: allAsset) {
      if (a.getAssetNumber() == id) {
        return a;
      }
    }
    return null;
  }

  public static List<String> getPriorities() {
    List<PriorityLevel> priorities = Arrays.asList(MaintenanceTicket.PriorityLevel.values());
    List<String> stringPriorities = new ArrayList<String>();
    for (PriorityLevel p: priorities) {
      stringPriorities.add(p.toString());
    }
    return stringPriorities;
    // return new ArrayList<>(Arrays.asList("Urgent", "Normal", "Low"));
  }


  public static void ticketAssignment(int ticketId, String employeeEmail, String timeEstimate, String priority, Boolean requiresApproval) {
    PriorityLevel selectedPriority = PriorityLevel.Low; //RANDOM INITIALIZATION VALUE (TO NOT HAVE RED ERROR SQUIGGLY LINES BELOW). DOES NOT MATTER BECAUSE ACTUAL VALUE IS SET IN THE FOR LOOP.
    TimeEstimate selectedTimeEstimate = TimeEstimate.LessThanADay;
    List<PriorityLevel> priorities = Arrays.asList(MaintenanceTicket.PriorityLevel.values());
    List<TimeEstimate> timeEstimates = Arrays.asList(MaintenanceTicket.TimeEstimate.values());
    for (PriorityLevel p: priorities) {
      if (p.toString().equals(priority)) {
        selectedPriority = p;
        break;
      }
    }
    for (TimeEstimate te: timeEstimates) {
      if (te.toString().equals(timeEstimate)) {
        selectedTimeEstimate = te;
        break;
      }
    }
    AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(ticketId, employeeEmail, selectedTimeEstimate, selectedPriority, requiresApproval);
  }

  public static String startTicket(int ticketId) {
    return AssetPlusTicketingController.startWorkOnMaintenanceTicket(ticketId);
  }

  public static String completeTicket(int ticketId) {
    return AssetPlusTicketingController.completeWorkOnMaintenanceTicket(ticketId);
  }

  public static List<String> getTimeEstimate() {
    List<TimeEstimate> timeEstimates = Arrays.asList(MaintenanceTicket.TimeEstimate.values());
    List<String> stringTimeEstimates = new ArrayList<String>();
    for (TimeEstimate te:timeEstimates) {
      stringTimeEstimates.add(te.toString());
    }
    return stringTimeEstimates;
    //return new ArrayList<>(Arrays.asList("LessThanADay", "OneToThreeDays", "ThreeToSevenDays", "OneToThreeWeeks", "ThreeOrMoreWeeks"));
  }



  // public static ArrayList<User> getAllUsers() {
  //   ArrayList<User> allUsers = new java.util.ArrayList<User>(Collections.emptyList());
  //   allUsers.addAll(ap.getGuests());
  //   allUsers.addAll(ap.getEmployees());
  //   allUsers.add(ap.getManager());
  //   return allUsers;
  // }


  // public static Boolean removeUser(User employee) {
  //   Boolean status = ap.removeEmployee((Employee)employee);
  //   return status;
  // }

  // public static String getUserName(User u) {
  //   return u.getName();
  // }
  // public static String getUserEmail(User u) {
  //   return u.getEmail();
  // }
}
