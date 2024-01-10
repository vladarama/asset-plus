package ca.mcgill.ecse.assetplus.controller;

import java.sql.Date;
import java.util.List;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;

public class AssetPlusFeatureSet4Controller {
  private static final AssetPlus ap = AssetPlusApplication.getAssetPlus();

  /**
   * Adds a ticket based on the argument
   *
   * @author Tim Pham
   * @param id              Ticket's unique id
   * @param raisedOnDate    Ticket's date
   * @param description     Ticket's description
   * @param email           The raiser's email
   * @param assetNumber     ID of the specific asset with -1 means no asset
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static String addMaintenanceTicket(int id, Date raisedOnDate, String description, String email, int assetNumber) {
    User g = User.getWithEmail(email);
    if (g == null) {
      return "The ticket raiser does not exist";
    }

    if (ticketExist(id)) {
        return "Ticket id already exists";
    }

    SpecificAsset asset;

    if (assetNumber == -1) {
        asset = null;
    }
    else if (!assetExist(assetNumber)) {
        return "The asset does not exist";
    }
    else {
        asset = SpecificAsset.getWithAssetNumber(assetNumber);
    }

    if (description.isBlank()) {
        return "Ticket description cannot be empty";
    }

    try {
        MaintenanceTicket t = ap.addMaintenanceTicket(id, raisedOnDate, description, g);
        t.setAsset(asset);
        AssetPlusPersistence.save();
    } catch (RuntimeException e) {
        return e.getMessage();
    }
    return "";
  }

  private static boolean assetExist(int id) {
      return SpecificAsset.getWithAssetNumber(id) != null;
  }

  private static boolean ticketExist(int id) {
      return MaintenanceTicket.getWithId(id) != null;
  }

  /**
   * Update a ticket based on the argument
   *
   * @author Tim Pham
   * @param id                 Ticket's unique id
   * @param newRaisedOnDate    Ticket's new date
   * @param newDescription     Ticket's new description
   * @param newEmail           The raiser's email
   * @param newAssetNumber     ID of the new specific asset, -1 means removing the asset
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static String updateMaintenanceTicket(int id, Date newRaisedOnDate, String newDescription,
      String newEmail, int newAssetNumber) {
      MaintenanceTicket current = MaintenanceTicket.getWithId(id);

      if (newDescription.isBlank()) {
        return "Ticket description cannot be empty";
      }

      User u = User.getWithEmail(newEmail);
      if (u == null) {
        return "The ticket raiser does not exist";
      }

      SpecificAsset a;
      if (newAssetNumber == -1) {
        a = null;
      } else {
        a = SpecificAsset.getWithAssetNumber(newAssetNumber);
        if (a == null) {
          return "The asset does not exist";
        }
      }

      if (current == null) {
          return "";
      }

      try {
          current.setDescription(newDescription);
          current.setRaisedOnDate(newRaisedOnDate);
          current.setTicketRaiser(u);
          current.setAsset(a);
          AssetPlusPersistence.save();
      } catch (RuntimeException e) {
          return e.getMessage();
      }
      return "";
  }

  /**
   * Delete a ticket based on id
   *
   * @author Tim Pham
   * @param id                 Ticket's unique id
   */
  public static void deleteMaintenanceTicket(int id) {
    // Remove this exception when you implement this method
    MaintenanceTicket t = MaintenanceTicket.getWithId(id);
    if (t == null) {
      return;
    }
    t.delete();
    AssetPlusPersistence.save();
  }
}
