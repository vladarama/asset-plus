package ca.mcgill.ecse.assetplus.controller;

import java.util.ArrayList;
import java.util.Collections;
import com.thoughtworks.xstream.mapper.Mapper.Null;
import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.Employee;
import ca.mcgill.ecse.assetplus.model.Guest;
import ca.mcgill.ecse.assetplus.model.User;

// line 10 "../../../../../../AssetPlusPersistence.ump"
// line 16 "../../../../../../AssetPlus.ump"
public class TOUser
{
  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();
  //User Attributes
  private String email;
  private String name;
  private String password;
  private String phoneNumber;
  private String userType;

  public TOUser(String aEmail, String aName, String aPassword, String aPhoneNumber, String aEmployee)
  {
    name = aName;
    password = aPassword;
    phoneNumber = aPhoneNumber;
    email = aEmail;
    userType = aEmployee;
  }

  public static ArrayList<TOUser> getAllUsers() {
    ArrayList<TOUser> userList = new ArrayList<TOUser>();
    for (var user: ap.getEmployees()) {
      userList.add(new TOUser(user.getEmail(), user.getName(), user.getPassword(), user.getPhoneNumber(), "Employee"));
    }
    for (var user: ap.getGuests()) {
      userList.add(new TOUser(user.getEmail(), user.getName(), user.getPassword(), user.getPhoneNumber(), "Guest"));
    }
    if (ap.getManager()!=null) {
      var user = ap.getManager();
      userList.add(new TOUser(user.getEmail(), user.getName(), user.getPassword(), user.getPhoneNumber(), "Manager"));
    }
    return userList;

  }
  
  public boolean setUserType(String aUserType)
  {
    boolean wasSet = false;
    userType = aUserType;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setPhoneNumber(String aPhoneNumber)
  {
    boolean wasSet = false;
    phoneNumber = aPhoneNumber;
    wasSet = true;
    return wasSet;
  }

  public String getUserType()
  {
    return userType;
  }

  public String getEmail()
  {
    return email;
  }

  public String getName()
  {
    return name;
  }

  public String getPassword()
  {
    return password;
  }

  public String getPhoneNumber()
  {
    return phoneNumber;
  }




  public String toString()
  {
    return getName();
  }
}