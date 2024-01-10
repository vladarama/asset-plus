package ca.mcgill.ecse.assetplus.controller;

import java.sql.Date;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.HotelStaff;
import ca.mcgill.ecse.assetplus.model.MaintenanceNote;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.PriorityLevel;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.Status;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.TimeEstimate;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;

public class AssetPlusTicketingController {
  // MAKE SURE YOU READ ProcessMaintenanceTickets.feature before starting to work
  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();

  /**
   * Assigns a hotel staff member to a maintenance ticket based on provided parameters.
   *  
   * @author Krasimir Kirov
   * @param ticketId The unique identifier of the maintenance ticket to be assigned.
   * @param employeeEmail The email address of the hotel staff member to be assigned to the maintenance ticket.
   * @param timeEstimate The estimated time required to complete the maintenance work.
   * @param priority The priority level of the maintenance ticket.
   * @param requiresApproval A Boolean indicating whether the assignment requires approval.
   * @return An empty string if the operation was successful, or an error message string describing why the operation failed.
   */
  public static String assignHotelStaffToMaintenanceTicket(int ticketId, String employeeEmail,
      TimeEstimate timeEstimate, PriorityLevel priority, Boolean requiresApproval) {
    
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketId);
    if (ticket == null) {
      return "Maintenance ticket does not exist.";
    }

    if (timeEstimate == null) {
      return "Please input a time estimate.";
    }

    if (priority == null) {
      return "Please input a priority level.";
    }

    //employee validation
    User ticketFixer = HotelStaff.getWithEmail(employeeEmail);
    HotelStaff hotelStaff;
    if (ticketFixer instanceof HotelStaff) {
      hotelStaff = (HotelStaff) ticketFixer;
    } else {
      return "Staff to assign does not exist.";
    }

    // assign the ticket
    try {
      if (requiresApproval) {
        ticket.assign(hotelStaff, priority, timeEstimate, ap.getManager());
      } else {
        ticket.assign(hotelStaff, priority, timeEstimate, null);
      }
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return "";
  } 
  
  /**
   * Starts work on a maintenance ticket.
   * 
   * @author Vlad Arama
   * @param ticketId
   * @return an empty string if the operation was successful or an error
   */
  public static String startWorkOnMaintenanceTicket(int ticketId) {
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketId);
    if (ticket == null) {
      return "Maintenance ticket does not exist.";
    }
    // start work on the ticket
    try {
      Boolean result = ticket.start();
      if (!result) {
        return "Could not start work on maintenance ticket.";
      }
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return "";
  }
  
  /**
   * Completes work on a maintenance ticket
   * 
   * @author Michael Rafferty
   * @param ticketId
   * @return an empty string if the operation was successful or an error
   */
  public static String completeWorkOnMaintenanceTicket(int ticketId) {
   MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketId);
    if (ticket == null) {
      return "Maintenance ticket does not exist.";
    }
    // Complete the ticket
    try {
      Boolean result = ticket.complete();
      if (!result) {
        return "Could not complete work on maintenance ticket.";
      }
      AssetPlusPersistence.save();
    } catch (Exception e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Disapprove work and include a maintenance note explaining the reason.
   *
   * @author Tim Pham
   * @param ticketId
   * @return error message
   */
  public static String approveWorkOnMaintenanceTicket(int ticketId) {
    // Remove this exception when you implement this method
    MaintenanceTicket t = MaintenanceTicket.getWithId(ticketId);
    if (t == null) {
      return "Maintenance ticket does not exist.";
    }

    try {
      t.approve();
      AssetPlusPersistence.save();
    } catch (RuntimeException e){
      return e.getMessage();
    }
    return "";
  }

  /**
   * Disapprove work and include a maintenance note explaining the reason.
   * 
   * @author Li Yang Lei
   * @param ticketId
   * @param date
   * @param reason
   * @return empty string if operation was successful and error if it was unsuccessful
   */
  public static String disapproveWorkOnMaintenanceTicket(int ticketId, Date date, String reason) {
    String error = "";
    try {
      // get the ticket and its current status
      MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketId);
      if (ticket == null) {
        error = "Maintenance ticket does not exist.";
        return error;
      }
      if (reason.isEmpty()) {
        error = "A note must be provided when disapproving.";
        return error;
      }
      ticket.disapprove(date, reason, ap.getManager());
      AssetPlusPersistence.save();
      return error;
    } catch (RuntimeException e) {
      error = e.getMessage();
      return error;
    }
  }
}
