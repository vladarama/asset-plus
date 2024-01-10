package ca.mcgill.ecse.assetplus.controller;

import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import java.sql.Date;
import java.util.List;

public class AssetPlusFeatureSet3Controller {

  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();

  // controller feature set shouldn't be instantiated
  private AssetPlusFeatureSet3Controller() {};

  /**
   * Adds a specific asset based on provided parameters.
   *
   * @author Krasimir Kirov
   * @param assetNumber    The unique number for the asset.
   * @param floorNumber    The floor number where the asset is located.
   * @param roomNumber     The room number where the asset is located.
   * @param purchaseDate   The date the asset was purchased.
   * @param assetTypeName  The name of the asset type.
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static String addSpecificAsset(int assetNumber, int floorNumber, int roomNumber,
      Date purchaseDate, String assetTypeName) {

    AssetType assetType = AssetType.getWithName(assetTypeName);

    if (assetType == null) {
      return "The asset type does not exist";
    }

    if (floorNumber < 0) {
      return "The floor number shall not be less than 0";
    }

    if (roomNumber < -1) {
      return "The room number shall not be less than -1";
    }

    if (assetNumber < 1) {
      return "The asset number shall not be less than 1";
    }

    // try adding the specific asset
    try {
      ap.addSpecificAsset(ap.addSpecificAsset(assetNumber, floorNumber, roomNumber, purchaseDate, assetType));
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
      String caughtError = e.getMessage();
      if (caughtError.startsWith("Cannot create due to duplicate assetNumber.")) {
        caughtError = "Cannot create due to duplicate asset number.";
      }
      return caughtError;
    }
    return "";
  }

  /**
   * Updates a specific asset based on provided parameters.
   *
   * @author Krasimir Kirov
   * @param assetNumber        The unique number of the asset to update.
   * @param newFloorNumber     The new floor number for the asset.
   * @param newRoomNumber      The new room number for the asset.
   * @param newPurchaseDate    The new purchase date for the asset.
   * @param newAssetTypeName   The new asset type name for the asset.
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static String updateSpecificAsset(int assetNumber, int newFloorNumber, int newRoomNumber,
      Date newPurchaseDate, String newAssetTypeName) {

    List<SpecificAsset> specificAssetsList = ap.getSpecificAssets();

    AssetType newAssetType = AssetType.getWithName(newAssetTypeName);

    if (newAssetType == null) {
      return "The asset type does not exist";
    }

    if (assetNumber < 1) {
      return "The asset number shall not be less than 1";
    }

    if (newFloorNumber < 0) {
      return "The floor number shall not be less than 0";
    }

    if (newRoomNumber < -1) {
      return "The room number shall not be less than -1";
    }

    // try updating the specific asset
    try {
      for (SpecificAsset specificAsset : specificAssetsList) {
        if (specificAsset.getAssetNumber() == assetNumber) {
          specificAsset.setFloorNumber(newFloorNumber);
          specificAsset.setRoomNumber(newRoomNumber);
          specificAsset.setPurchaseDate(newPurchaseDate);
          specificAsset.setAssetType(newAssetType);
          AssetPlusPersistence.save();
          break;
        }
      }
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return "";
  }

  
  /**
   * Deletes a specific asset based on the provided asset number.
   *
   * @author Krasimir Kirov
   * @param assetNumber  The unique number of the asset to delete.
   */
  public static void deleteSpecificAsset(int assetNumber) {
    List<SpecificAsset> specificAssetsList = ap.getSpecificAssets();
    
    if (assetNumber < 1) {
      return;
    }

    // delete the specific asset
    for (SpecificAsset specificAsset : specificAssetsList) {
      if (specificAsset.getAssetNumber() == assetNumber) {
        specificAsset.delete();
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
