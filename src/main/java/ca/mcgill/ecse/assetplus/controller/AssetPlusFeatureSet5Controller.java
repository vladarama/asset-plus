package ca.mcgill.ecse.assetplus.controller;

import java.util.List;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.TicketImage;

public class AssetPlusFeatureSet5Controller {

  // controller feature set shouldn't be instantiated
  private AssetPlusFeatureSet5Controller() {};

  /**
   * Adds a ticket image to a specific ticket
   * 
   * @author William Wang
   * @param imageURL the url of the image to add
   * @param ticketID the ticket to which the image should be attached
   * @return an error message from either invalid input or RuntimeException, empty if successful
   */
  public static String addImageToMaintenanceTicket(String imageURL, int ticketID) {

    // input validation
    var error = "";
    if (imageURL == null || imageURL.trim().isEmpty()) {
      error = "Image URL cannot be empty. ";
    }
    if (!(imageURL.startsWith("http://") || imageURL.startsWith("https://"))) {
      error += "Image URL must start with http:// or https://. ";
    }
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
    if (ticket == null) {
      error += "Ticket does not exist. ";
    } else {
      List<TicketImage> ticketImages = ticket.getTicketImages();
      for (TicketImage e : ticketImages) {
        if (e.getImageURL().equals(imageURL)) {
          error += "Image already exists for the ticket. ";
          break;
        }
      }
    }
    if (!error.isEmpty()) {
      return error.trim(); // return error for invalid input
    }

    // call model
    try {
      ticket.addTicketImage(imageURL);
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
      String caughtError = e.getMessage();
      if (caughtError.startsWith("Unable to create ticketImage due to ticket.")) {
        caughtError = "There must be at least one ticket for a ticket image to exist. ";
      }
      return caughtError;
    }
    return "";
  }

  /**
   * Deletes an image from a maintenance ticket if the specified ticket exists and its ticket images
   * contains the image to be deleted.
   * 
   * @author Willian Wang
   * @param imageURL the url of the image to be deleted
   * @param ticketID the ticket ID of the ticket to which the image to be deleted is attached to
   */
  public static void deleteImageFromMaintenanceTicket(String imageURL, int ticketID) {
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketID);
    if (ticket != null) {
      List<TicketImage> ticketImages = ticket.getTicketImages();
      for (TicketImage ticketImage : ticketImages) {
        if (ticketImage.getImageURL().equals(imageURL)) {
          ticketImage.delete();
          break;
        }
      }
      try {
        AssetPlusPersistence.save();
      } catch (RuntimeException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }
}
