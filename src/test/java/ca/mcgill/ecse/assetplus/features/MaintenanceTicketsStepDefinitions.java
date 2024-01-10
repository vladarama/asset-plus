package ca.mcgill.ecse.assetplus.features;

import ca.mcgill.ecse.assetplus.application.AssetPlusApplication;
import ca.mcgill.ecse.assetplus.controller.AssetPlusFeatureSet6Controller;
import ca.mcgill.ecse.assetplus.controller.AssetPlusTicketingController;
import ca.mcgill.ecse.assetplus.controller.TOMaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.AssetPlus;
import ca.mcgill.ecse.assetplus.model.AssetType;
import ca.mcgill.ecse.assetplus.model.HotelStaff;
import ca.mcgill.ecse.assetplus.model.MaintenanceNote;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket;
import ca.mcgill.ecse.assetplus.model.Manager;
import ca.mcgill.ecse.assetplus.model.SpecificAsset;
import ca.mcgill.ecse.assetplus.model.TicketImage;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.Status;
import static java.lang.Integer.parseInt;
import ca.mcgill.ecse.assetplus.model.User;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.TimeEstimate;
import ca.mcgill.ecse.assetplus.model.MaintenanceTicket.PriorityLevel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.it.Ma;

public class MaintenanceTicketsStepDefinitions {

  private static AssetPlus ap = AssetPlusApplication.getAssetPlus();
  private String errorMessage; // USE THIS TO STORE ERROR MESSAGES FROM CONTROLLER
  private List<TOMaintenanceTicket> tickets;


  /**
   * Initializes some employees into the application
   * 
   * @author William Wang
   * @param employeesDataTable
   */
  @Given("the following employees exist in the system")
  public void the_following_employees_exist_in_the_system(
      io.cucumber.datatable.DataTable employeesDataTable) {
    List<Map<String, String>> employeesToAdd = employeesDataTable.asMaps();
    for (Map<String, String> employee : employeesToAdd) {
      String email = employee.get("email");
      String name = employee.get("name");
      String password = employee.get("password");
      String phoneNumber = employee.get("phoneNumber");
      ap.addEmployee(email, name, password, phoneNumber);
    }
  }

  /**
   * @author Vlad Arama
   * @param managerDataTable
   */
  @Given("the following manager exists in the system")
  public void the_following_manager_exists_in_the_system(
      io.cucumber.datatable.DataTable managerDataTable) {
    String email = "";
    String password = "";
    List<Map<String, String>> rows = managerDataTable.asMaps();
    for (var row : rows) {
      email = row.get("email");
      password = row.get("password");
    }

    if (ap.hasManager()) {
      Manager existingManager = ap.getManager();
      existingManager.setEmail(email);
      existingManager.setPassword(password);
    } else {
      new Manager(email, "", password, "", ap);
    }
  }

  /**
   * Initializes asset types into the application
   * @author Krasimir Kirov
   * @param assetTypesDataTable
   */
  @Given("the following asset types exist in the system")
  public void the_following_asset_types_exist_in_the_system(
      io.cucumber.datatable.DataTable assetTypesDataTable) {
    List<Map<String, String>> rows = assetTypesDataTable.asMaps();
    for (Map<String, String> row : rows) {
      String name = row.get("name");
      int expectedLifeSpan = Integer.parseInt(row.get("expectedLifeSpan"));
      ap.addAssetType(name, expectedLifeSpan);
    }
  }

  /**
   * @author Michael Rafferty
   * @param dataTable
   */
  @Given("the following assets exist in the system")
  public void the_following_assets_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    AssetPlus ap = AssetPlusApplication.getAssetPlus();
    List<Map<String, String>> rows = dataTable.asMaps();
    for (var row : rows) {
      int aAssetNumber = Integer.parseInt(row.get("assetNumber"));
      AssetType aAssetType = AssetType.getWithName(row.get("type"));
      Date aPurchaseDate = Date.valueOf(row.get("purchaseDate"));
      int aFloorNumber = Integer.parseInt(row.get("floorNumber"));
      int aRoomNumber = Integer.parseInt(row.get("roomNumber"));
      //new SpecificAsset(aAssetNumber, aFloorNumber, aRoomNumber, aPurchaseDate, ap, aAssetType);
      ap.addSpecificAsset(aAssetNumber, aFloorNumber, aRoomNumber, aPurchaseDate, aAssetType);
    }
  }

  /**
   * 5
   * @author Tim Pham
   * @param dataTable test parameter from cucumber
   */
  @Given("the following tickets exist in the system")
  public void the_following_tickets_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    List<Map<String, String>> tickets = dataTable.asMaps();
    MaintenanceTicket ticket;
    for (var t : tickets) {
      //Status is open by default
      ticket = ap.addMaintenanceTicket(
              Integer.parseInt(t.get("id")),
              Date.valueOf(t.get("raisedOnDate")),
              t.get("description"),
              User.getWithEmail(t.get("ticketRaiser"))
      );
      if (t.get("assetNumber") != null) {
        ticket.setAsset(SpecificAsset.getWithAssetNumber(Integer.parseInt(t.get("assetNumber"))));
      }
    }
  }

  /**
   * Creates new notes
   * 
   * @author Li Yang Lei
   * @param noteDataTable
   */
  @Given("the following notes exist in the system")
  public void the_following_notes_exist_in_the_system(io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> notes = dataTable.asMaps();
    for (var note : notes) {
      String email = note.get("noteTaker");
      int ticketId = Integer.parseInt(note.get("ticketId"));
      Date addedOnDate = Date.valueOf(note.get("addedOnDate"));
      String description = note.get("description");
      MaintenanceTicket ticket = MaintenanceTicket.getWithId(ticketId);
      HotelStaff staff = (HotelStaff) HotelStaff.getWithEmail(email);
      MaintenanceNote newNote = new MaintenanceNote(addedOnDate, description, ticket, staff);
      ticket.addTicketNote(newNote);
    }
  }


  /**
   * Initializes some ticket images in the application
   * 
   * @author William Wang
   * @param imagesDataTable
   */
  @Given("the following ticket images exist in the system")
  public void the_following_ticket_images_exist_in_the_system(
      io.cucumber.datatable.DataTable imagesDataTable) {
    List<Map<String, String>> rows = imagesDataTable.asMaps();
    for (var row : rows) {
      String imageUrl = row.get("imageUrl");
      int ticketId = Integer.parseInt(row.get("ticketId"));
      new TicketImage(imageUrl, MaintenanceTicket.getWithId(ticketId));
    }
  }

  /**
   * @author Vlad Arama
   * @param ticketId
   * @param state
   * @param requiresApproval
   */
  @Given("ticket {string} is marked as {string} with requires approval {string}")
  public void ticket_is_marked_as_with_requires_approval(String ticketId, String state,
      String requiresApproval) {
    boolean approval;
    if (requiresApproval.equals("true")) {
      approval = true;
    } else {
      approval = false;
    }
    if (state.equals("InProgress")) {
      errorMessage = AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(Integer.parseInt(ticketId),
          "jeff@ap.com", TimeEstimate.ThreeToSevenDays, PriorityLevel.Low, approval);
    }
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketId));
    if (ticket.getStatusFullName().equals("Assigned")) {
      errorMessage = AssetPlusTicketingController.startWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
    }
  }

  /**
   * Marks ticket with ticketId as the given state
   * @author Krasimir Kirov
   * @param ticketId
   * @param state
   */
  @Given("ticket {string} is marked as {string}")
  public void ticket_is_marked_as(String ticketId, String state) {
    switch (state) {
      case "Assigned":
          AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(Integer.parseInt(ticketId),
          "jeff@ap.com", TimeEstimate.ThreeToSevenDays, PriorityLevel.Low, true);
          break;
      case "InProgress":
          AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(Integer.parseInt(ticketId),
          "jeff@ap.com", TimeEstimate.ThreeToSevenDays, PriorityLevel.Low, true);
          AssetPlusTicketingController.startWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
          break;
      case "Resolved":
          AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(Integer.parseInt(ticketId),
          "jeff@ap.com", TimeEstimate.ThreeToSevenDays, PriorityLevel.Low, true);
          AssetPlusTicketingController.startWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
          AssetPlusTicketingController.completeWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
          break;
      case "Closed":
          AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(Integer.parseInt(ticketId),
          "jeff@ap.com", TimeEstimate.ThreeToSevenDays, PriorityLevel.Low, true);
          AssetPlusTicketingController.startWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
          AssetPlusTicketingController.completeWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
          AssetPlusTicketingController.approveWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
          break;
  }
}

  /* | Open   |
      |  2 | jeff@ap.com    |   2023-07-10 | The lamp is sometimes flickering |           1 | Open   |*
   * @author Michael Rafferty
   */
  @When("the manager attempts to view all maintenance tickets in the system")
  public void the_manager_attempts_to_view_all_maintenance_tickets_in_the_system() {
    tickets = AssetPlusFeatureSet6Controller.getTickets();
  }

  /**
   * 5
   * 
   * @param id id
   * @param employeeEmail employeeEmail
   * @param timeEstimate timeEstimate
   * @param priorityLevel priorityLevel
   * @param requiresApproval requiresApproval
   */
  @When("the manager attempts to assign the ticket {string} to {string} with estimated time {string}, priority {string}, and requires approval {string}")
  public void the_manager_attempts_to_assign_the_ticket_to_with_estimated_time_priority_and_requires_approval(
      String id, String employeeEmail, String timeEstimate, String priorityLevel, String requiresApproval) {
      errorMessage = AssetPlusTicketingController.assignHotelStaffToMaintenanceTicket(
              Integer.parseInt(id),
              employeeEmail,
              MaintenanceTicket.TimeEstimate.valueOf(timeEstimate),
              MaintenanceTicket.PriorityLevel.valueOf(priorityLevel),
              Boolean.valueOf(requiresApproval)
      );
  }

  /**
   * Update status of ticket to In Progress
   * 
   * @author Li Yang Lei
   * @param ticketID
   */
  @When("the hotel staff attempts to start the ticket {string}")
  public void the_hotel_staff_attempts_to_start_the_ticket(String ticketID) {
    errorMessage = AssetPlusTicketingController.startWorkOnMaintenanceTicket(Integer.parseInt(ticketID));
  }

  /**
   * Calls the controller to approve work on a maintenance ticket.
   * 
   * @author William Wang
   * @param ticketId
   */
  @When("the manager attempts to approve the ticket {string}")
  public void the_manager_attempts_to_approve_the_ticket(String ticketId) {
    errorMessage =
        AssetPlusTicketingController.approveWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
  }

  /**
   * @author Vlad Arama
   * @param ticketId
   */
  @When("the hotel staff attempts to complete the ticket {string}")
  public void the_hotel_staff_attempts_to_complete_the_ticket(String ticketId) {
    errorMessage = AssetPlusTicketingController.completeWorkOnMaintenanceTicket(Integer.parseInt(ticketId));
  }

  /**
   * Manager attempts to disaprove the ticket on a date and with a reason
   * @author Krasimir Kirov
   * @param ticketId 
   * @param date
   * @param reason
   */
  @When("the manager attempts to disapprove the ticket {string} on date {string} and with reason {string}")
  public void the_manager_attempts_to_disapprove_the_ticket_on_date_and_with_reason(String ticketId,
      String date, String reason) {
    errorMessage = AssetPlusTicketingController.disapproveWorkOnMaintenanceTicket(Integer.parseInt(ticketId), Date.valueOf(date), reason);
  }

  /**
   * @author Michael Rafferty
   * @param ticketID represents the ticketID
   * @param ticketStatus represented the status of the ticket
   */
  @Then("the ticket {string} shall be marked as {string}")
  public void the_ticket_shall_be_marked_as(String ticketID, String ticketStatus) {
    MaintenanceTicket maintenanceTicket = MaintenanceTicket.getWithId(Integer.parseInt(ticketID)); 
    assertNotNull(maintenanceTicket);
    Status theStatus = maintenanceTicket.getStatus();
    assertEquals(ticketStatus, theStatus.name());
  }

  /**
   * 5
   * @author Tim Pham
   * @param errorMsg Expected error message
   */
  @Then("the system shall raise the error {string}")
  public void the_system_shall_raise_the_error(String errorMsg) {
    assertEquals("Incorrect error message", errorMsg, errorMessage);
  }

  /**
   * Verifies that there is no maintenance ticket associated with the given ticket ID
   * 
   * @author Li Yang Lei
   * @param ticket
   */
  @Then("the ticket {string} shall not exist in the system")
  public void the_ticket_shall_not_exist_in_the_system(String ticket) {
    int ticketId = Integer.parseInt(ticket);
    assertNull(MaintenanceTicket.getWithId(ticketId));
  }

  /**
   * Validates that the ticket's estimated time, priority, and approval requirement are correct
   * 
   * @author William Wang
   * @param ticketId
   * @param expectedTimeEstimate
   * @param expectedPriority
   * @param expectedRequiresApproval
   */
  @Then("the ticket {string} shall have estimated time {string}, priority {string}, and requires approval {string}")
  public void the_ticket_shall_have_estimated_time_priority_and_requires_approval(String ticketId,
      String expectedTimeEstimate, String expectedPriority, String expectedRequiresApproval) {
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketId));
    assertEquals(TimeEstimate.valueOf(expectedTimeEstimate), ticket.getTimeToResolve());
    assertEquals(PriorityLevel.valueOf(expectedPriority), ticket.getPriority());
    assertEquals(Boolean.valueOf(expectedRequiresApproval), ticket.hasFixApprover());
  }

  /**
   * @author Vlad Arama
   * @param ticketId
   * @param employeeEmail
   */
  @Then("the ticket {string} shall be assigned to {string}")
  public void the_ticket_shall_be_assigned_to(String ticketId, String employeeEmail) {
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketId));
    User ticketFixer = HotelStaff.getWithEmail(employeeEmail);
    HotelStaff hotelStaff;
    if (ticketFixer instanceof HotelStaff) {
      hotelStaff = (HotelStaff) ticketFixer;
      assertEquals(hotelStaff, ticket.getTicketFixer());
    } else {
      assertEquals(ticketFixer, ticket.getTicketFixer());
    }

  }
  
/**
 * Asserts the number of tickets in the system
 * @author Krasimir Kirov
 * @param number
 */
  @Then("the number of tickets in the system shall be {string}")
  public void the_number_of_tickets_in_the_system_shall_be(String number) {
    int numberOfMaintenanceTickets = Integer.parseInt(number);
    assertEquals(ap.numberOfMaintenanceTickets(), numberOfMaintenanceTickets);
  }

  /**
   * @author Michael Rafferty
   * @param dataTable Cucumber DataTable containing the id, ticketRaiser, raisedOnDate, description, assetName, expectedLifeSpan, purchaseDate, floorNumber and roomNumber of the tickets shown.
   */
  @Then("the following maintenance tickets shall be presented")
  public void the_following_maintenance_tickets_shall_be_presented(
      io.cucumber.datatable.DataTable dataTable) {
    List<Map<String, String>> rows = dataTable.asMaps();
    int i = 0;
    for (var row : rows) {
      TOMaintenanceTicket currTicket = tickets.get(i);
      int id = Integer.parseInt(row.get("id"));
      assertEquals(id, currTicket.getId());
      String ticketRaiserEmail = row.get("ticketRaiser");
      assertEquals(ticketRaiserEmail, currTicket.getRaisedByEmail());
      Date raisedOnDate = Date.valueOf(row.get("raisedOnDate"));
      assertEquals(raisedOnDate, currTicket.getRaisedOnDate());
      String description = row.get("description");
      assertEquals(description, currTicket.getDescription());
      String assetName = row.get("assetName");
      assertEquals(assetName, currTicket.getAssetName());
      String expectLifeSpanStr = row.get("expectLifeSpan");
      int expectLifeSpan = -1;
      if (expectLifeSpanStr != null) {
        expectLifeSpan = Integer.parseInt(expectLifeSpanStr);
      }
      assertEquals(expectLifeSpan, currTicket.getExpectLifeSpanInDays());
      String purchaseDateStr = row.get("purchaseDate");
      Date purchaseDate = null;
      if (purchaseDateStr != null) {
        purchaseDate = Date.valueOf(purchaseDateStr);
      }
      assertEquals(purchaseDate, currTicket.getPurchaseDate());
      String floorNumberStr = row.get("floorNumber");
      int floorNumber = -1;
      if (floorNumberStr != null) {
        floorNumber = Integer.parseInt(floorNumberStr);
      }
      assertEquals(floorNumber, currTicket.getFloorNumber());
      String roomNumberStr = row.get("roomNumber");
      int roomNumber = -1;
      if (roomNumberStr != null) {
        roomNumber = Integer.parseInt(roomNumberStr);
      }
      assertEquals(roomNumber, currTicket.getRoomNumber());
      i++;
    }
  }

  /**
   * 5
   * 
   * @param string
   * @param dataTable
   */
  @Then("the ticket with id {string} shall have the following notes")
  public void the_ticket_with_id_shall_have_the_following_notes(String string,
      io.cucumber.datatable.DataTable dataTable) {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // E, List<E>, List<List<E>>, List<Map<K,V>>, Map<K,V> or
    // Map<K, List<V>>. E,K,V must be a String, Integer, Float,
    // Double, Byte, Short, Long, BigInteger or BigDecimal.
    //
    // For other transformations you can register a DataTableType.
    var notes = dataTable.asMaps();
    MaintenanceTicket t = MaintenanceTicket.getWithId(Integer.parseInt(string));
    int matched = 0;
    for (var tNote: t.getTicketNotes()) {
      for (var note: notes) {
        if (tNote.getDescription().equals(note.get("description"))
          && tNote.getDate().equals(Date.valueOf(note.get("addedOnDate")))
          && tNote.getNoteTaker().getEmail().equals(note.get("noteTaker"))) {
          matched += 1;
        }
      }
    }
    assertEquals("Incorrect number of matched notes", notes.size(), matched);
  }

  /**
   * Verifies that the maintenance ticket has 0 notes
   * 
   * @author Li Yang Lei
   * @param ticket
   */
  @Then("the ticket with id {string} shall have no notes")
  public void the_ticket_with_id_shall_have_no_notes(String ticket) {
    int ticketId = Integer.parseInt(ticket);
    assertEquals(MaintenanceTicket.getWithId(ticketId).numberOfTicketNotes(), 0);
  }

  /**
   * Validates that a specific ticket contains the expected images.
   * 
   * @author William Wang
   * @param ticketId
   * @param imagesDataTable
   */
  @Then("the ticket with id {string} shall have the following images")
  public void the_ticket_with_id_shall_have_the_following_images(String ticketId,
      io.cucumber.datatable.DataTable imagesDataTable) {
    List<Map<String, String>> rows = imagesDataTable.asMaps();
    List<String> expectedImageUrls =
        rows.stream().map(row -> row.get("imageUrl")).collect(Collectors.toList());
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketId));
    assertNotNull(ticket); // necessary or not?
    List<String> actualImageUrls = ticket.getTicketImages().stream().map(TicketImage::getImageURL)
        .collect(Collectors.toList());
    assertIterableEquals(expectedImageUrls, actualImageUrls);
  }

  /**
   * @author Vlad Arama
   * @param ticketId
   */
  @Then("the ticket with id {string} shall have no images")
  public void the_ticket_with_id_shall_have_no_images(String ticketId) {
    MaintenanceTicket ticket = MaintenanceTicket.getWithId(Integer.parseInt(ticketId));
    assertEquals(0, ticket.getTicketImages().size());
  }
}
