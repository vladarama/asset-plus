package ca.mcgill.ecse.assetplus.controller;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import java.util.List;

public class AssetPlusFeatureSet2Controller {

  private static AssetPlus assetPlus = AssetPlusApplication.getAssetPlus();

  // controller feature set shouldn't be instantiated
  private AssetPlusFeatureSet2Controller() {};

  /**
   * Adds an asset type based on provided parameters.
   * 
   * @author Vlad Arama
   * @param name The name of the asset type.
   * @param expectedLifeSpanInDays The expected life span of the asset type (in days).
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static String addAssetType(String name, int expectedLifeSpanInDays) {
    if (expectedLifeSpanInDays <= 0) {
      return ("The expected life span must be greater than 0 days");
    }

    if (name == null || name.equals("")) {
      return ("The name must not be empty");
    }

    // check if asset type already exists
    if (AssetType.getWithName(name) != null) {
      return ("The asset type already exists");
    }

    try {
      assetPlus.addAssetType(assetPlus.addAssetType(name, expectedLifeSpanInDays));
      AssetPlusPersistence.save();
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return "";
  }

  /**
   * Updates an asset type based on provided parameters.
   * 
   * @author Vlad Arama
   * @param oldName The old name of the asset type.
   * @param newName The new name of the asset type.
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static String updateAssetType(String oldName, String newName,
      int newExpectedLifeSpanInDays) {
    List<AssetType> assetTypeList = assetPlus.getAssetTypes();

    if (newExpectedLifeSpanInDays <= 0) {
      return ("The expected life span must be greater than 0 days");
    }

    if (oldName == null || newName == null) {
      return ("The name must not be empty");
    }

    if (oldName.equals("") || newName.equals("")) {
      return ("The name must not be empty");
    }

    // check if asset type already exists
    if (AssetType.getWithName(newName) != null && !newName.equals(oldName)) {
      return ("The asset type already exists");
    }

    // update the asset type
    try {
      for (AssetType assetType : assetTypeList) {
        if (assetType.getName().equals(oldName)) {
          assetType.setName(newName);
          assetType.setExpectedLifeSpan(newExpectedLifeSpanInDays);
          AssetPlusPersistence.save();
          return "";
        }
      }
    } catch (RuntimeException e) {
      return e.getMessage();
    }
    return ("The asset type does not exist");
  }


  /**
   * Deletes an asset type based on provided parameters.
   * 
   * @author Vlad Arama
   * @param name The name of the asset type to delete.
   * @return A message indicating the status of the operation or an empty string if successful.
   */
  public static void deleteAssetType(String name) {
    List<AssetType> assetTypeList = assetPlus.getAssetTypes();

    if (name == null || name.equals("")) {
      return;
    }

    // delete the asset type
    try {
      for (AssetType assetType : assetTypeList) {
        if (assetType.getName().equals(name)) {
          assetType.delete();
          AssetPlusPersistence.save();
          break;
        }
      }
    } catch (RuntimeException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
