package ca.mcgill.ecse.assetplus.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet3Controller;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;

// line 10 "../../../../../../AssetPlusPersistence.ump"
// line 16 "../../../../../../AssetPlus.ump"
public class TOAssetType {
  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();
  // User Attributes
  private String name;
  private Integer expectedLifespan;


  public TOAssetType(String aName, Integer aExpectedLifespan) {
    name = aName;
    expectedLifespan = aExpectedLifespan;
  }

  public static ArrayList<TOAssetType> getAllAssetTypes() {
    ArrayList<TOAssetType> assetList = new ArrayList<TOAssetType>();
    for (var asset : ap.getAssetTypes()) {
      assetList.add(new TOAssetType(asset.getName(), asset.getExpectedLifeSpan()));
    }
    return assetList;

  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setExpectedLifeSpan(Integer aExpectedLifespan) {
    boolean wasSet = false;
    expectedLifespan = aExpectedLifespan;
    wasSet = true;
    return wasSet;
  }


  public String getName() {
    return name;
  }

  public Integer getExpectedLifespan() {
    return expectedLifespan;
  }
}
