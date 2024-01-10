
package ca.mcgill.ecse.assetplus.controller;
import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.model.Employee;
import ca.mcgill.ecse.assetplus.model.Guest;
import ca.mcgill.ecse.assetplus.model.MaintenanceNote;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.PriorityLevel;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.Status;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import ca.mcgill.ecse.assetplus.model.TicketImage;
import java.sql.Date;
import java.util.List;

public class AssetPlusFeatureSet6Controller {
  
  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();

  /**
   * Using an email, deletes employee or guest account
   * 
   * @author Michael Rafferty
   * @param email
   * @return void
   */
  public static void deleteEmployeeOrGuest(String email) {
    List<Employee> employeeList = ap.getEmployees();
    List<Guest> guestList = ap.getGuests();
    boolean exists = false;
    User employeeOrGuest = null;
  for (int i=0; i<employeeList.size(); i++) {
      if (employeeList.get(i).getEmail().equals(email)){
        exists = true;
        employeeOrGuest = employeeList.get(i);
        break;
    }
  }
  for (int i=0; i<guestList.size(); i++) {
      if (guestList.get(i).getEmail().equals(email)){
        exists = true;
        employeeOrGuest = guestList.get(i);
        break;
    }
  }
  if (exists){
    try {
      employeeOrGuest.delete();
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
  }

  /**
   * Returns all tickets
   * 
   * @author Michael Rafferty
   * @return List<TOMaintenanceTicket> all tickets
   */
  public static List<TOMaintenanceTicket> getTickets() {
    var tickets = new ArrayList<TOMaintenanceTicket>();
    for (var maintenanceticket : ap.getMaintenanceTickets()){
      // Preparing parameters for the TOMaintenanceTicket object
      Status status = maintenanceticket.getStatus();
      String theStatus = status.name();
      /**
       * the following three attributes are set to null if no one has been assigned yet to fix the ticket
       */
      String fixerEmail = null;
      String timeToResolve = null;
      PriorityLevel priority = null; 
      String thePriority = null;
      if (maintenanceticket.hasTicketFixer()){
        fixerEmail = maintenanceticket.getTicketFixer().getEmail(); 
        timeToResolve = maintenanceticket.getTimeToResolve().name();;
        priority = maintenanceticket.getPriority(); 
        thePriority = priority.name();
      }
      // Does having a fix approver imply approval is required. I'm assuming so because .hasFixApprover() returns a boolean.
      boolean approvalRequired = maintenanceticket.hasFixApprover(); 
      /**
       *  if no asset is specified for the ticket, the following 5 attributes are set to null (String/Date) / -1 (Integer)
       */ 
      String assetName = null;
      int lifeSpan = -1;
      Date purchaseDate = null;
      int floorNumber = -1;
      int roomNumber = -1;
      if (maintenanceticket.hasAsset()){
        assetName = maintenanceticket.getAsset().getAssetType().getName(); 
        lifeSpan = maintenanceticket.getAsset().getAssetType().getExpectedLifeSpan(); 
        purchaseDate = maintenanceticket.getAsset().getPurchaseDate();
        floorNumber = maintenanceticket.getAsset().getFloorNumber(); 
        roomNumber = maintenanceticket.getAsset().getRoomNumber();
      }
      // URLS
      List<String> imageUrls = new ArrayList<String>();
      if (maintenanceticket.hasTicketImages()){
      for (TicketImage ticketImage: maintenanceticket.getTicketImages()){
        imageUrls.add(ticketImage.getImageURL());
        }
      }
      // Notes
      TOMaintenanceNote[] notes = new TOMaintenanceNote[maintenanceticket.numberOfTicketNotes()];
      if (maintenanceticket.hasTicketNotes()){
        int i = 0;
      for (MaintenanceNote maintenanceNote : maintenanceticket.getTicketNotes()){
        notes[i] = new TOMaintenanceNote(maintenanceNote.getDate(), maintenanceNote.getDescription(), maintenanceNote.getNoteTaker().getEmail());
        i++;
        }
      }

      // Making the list of tickets
      tickets.add(new TOMaintenanceTicket(
        maintenanceticket.getId(), 
        maintenanceticket.getRaisedOnDate(), 
        maintenanceticket.getDescription(), 
        maintenanceticket.getTicketRaiser().getEmail(), 
        theStatus,
        fixerEmail,
        timeToResolve,
        thePriority, 
        approvalRequired, 
        assetName,
        lifeSpan,
        purchaseDate,
        floorNumber,
        roomNumber,
        imageUrls,
        notes
        ));

    }
    return tickets;

  }
}
