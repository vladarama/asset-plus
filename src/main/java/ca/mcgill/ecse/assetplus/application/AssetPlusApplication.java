package ca.mcgill.ecse.assetplus.application;

import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.persistence.AssetPlusPersistence;
import ca.mcgill.ecse.assetplus.javafx.AssetPlusFxmlView;
import javafx.application.Application;

public class AssetPlusApplication {

  private static AssetPlus assetPlus;

  public static void main(String[] args) {
    // start UI
    Application.launch(AssetPlusFxmlView.class, args);
  }

  public static AssetPlus getAssetPlus() {
    if (assetPlus == null) {
      // load model from persistence 
      assetPlus = AssetPlusPersistence.load();
    }
    return assetPlus;
  }

}
