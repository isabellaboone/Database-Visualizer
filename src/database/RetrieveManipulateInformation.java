package database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Retrieve and manipulate information that is currently in the database.
 * @author Isabella Boone, Chase Banyai, Joel Gingrich, Joshua Jackson
 *
 */
public class RetrieveManipulateInformation {
  static Connection m_dbConn = null;
  
  static RetrieveManipulateInformation rmi;
  
  final int playerUsernames = 0, allItems = 1, charNames = 2, 
            locIdNums = 3, locAreaTypes = 4, charStats = 5;
  String[] procedures = {
      "CREATE PROCEDURE get_all_player_usernames()"
        + " SELECT Username FROM Player;",

      "CREATE PROCEDURE get_all_items() "
        + "SELECT ItemId from Item;",

      "CREATE PROCEDURE get_character_names(IN n varchar(20))"
        + "SELECT Name FROM Characters WHERE pUserName = n;",
          
      "CREATE PROCEDURE get_all_location_idNumbers() "
        + "SELECT IdNumber from Location;",
        
      "CREATE PROCEDURE get_all_location_AreaTypes() "
        + "SELECT AreaType from Location;",
        
      "CREATE PROCEDURE get_character_stats(IN n varchar(20))"
        + " SELECT * From Characters WHERE Name = n;"
          
  }, procedureNames = { 
      "get_all_player_usernames", "get_all_items", 
      "get_character_names", "get_all_location_idNumbers", 
      "get_all_location_AreaTypes", "get_character_stats"
  };
  
  private RetrieveManipulateInformation(Connection con) {
    this.m_dbConn = con;
    createProcedures(); 
  }
  
  public static RetrieveManipulateInformation createRetrieveManipulateInformation(Connection conn) {
    if(rmi != null) {
      return rmi;
    }
    
    return new RetrieveManipulateInformation(conn);
  }
  
  public void setConncetion(Connection con) {
    m_dbConn = con;
  }
  
  public static Connection getConncetion() {
    return m_dbConn;
  }

  /**
   * Create all procedures
   */
  private void createProcedures() {
    /* If we do not drop the procedures before we create them, we get a bunch of
     * errors from trying to create procedures that already exist.  
     */
    dropProcedures();

    PreparedStatement stmt;
    // Call statement to create procedures
    for (int i = 0; i < procedures.length; i++) {
      try {
        stmt = m_dbConn.prepareStatement(procedures[i]); 
        stmt.execute();
//        System.out.println("SQL Statement CREATED -- " + procedures[i]);
      } catch (SQLException e) {
//        e.printStackTrace();
        System.out.println("Stored procedure already exists -- " + procedures[i]);
      }
    }
  }

  /**
   * Drop all procedures 
   * maybe
   */
  public void dropProcedures() {
    for(int i = 0; i < procedureNames.length; i++) {
      String drop = "DROP PROCEDURE IF EXISTS " + procedureNames[i] + ";";
      try {
        PreparedStatement stmt = m_dbConn.prepareStatement(drop);
        stmt.execute(); 
//        System.out.println("Table dropped -- " + procedureNames[i]);
      } catch (SQLException e) {
//        e.printStackTrace();
        System.out.println("Failed to drop " + procedures[i]);
      }
    }
  }
  
  
  // Location Methods
  /**
   * Get the number of total locations in the database.  
   * @return int of how many total locations exist in database.
   */
  public int getNumLocations() {
    try {
      Statement stmt = m_dbConn.createStatement();
      ResultSet r = stmt.executeQuery("SELECT COUNT(*) FROM Location;");
      // Count how many locations are in the database
      
      r.next();
      return r.getInt("count(*)"); 
      
    } catch (SQLException e) {
      e.printStackTrace();
      return -1; // If this number is actually accidentally used, it will throw an error. 
    }
  }
  
  /**
   * Get every IdNumber of every Location in the database.
   * @return int[] of all Location IdNumbers 
   */
  public int[] getAllLocationIds() {
    String call = "CALL " + procedureNames[locIdNums] + "();";
    int[] locationIds = new int[getNumLocations()]; 
    try {
      CallableStatement stmt = m_dbConn.prepareCall(call);
      stmt.execute();
      
      ResultSet rs = stmt.getResultSet(); 
      
      int i = 0;
      while(rs.next()) {
        locationIds[i] = rs.getInt("IdNumber");
        i++; 
      }
      
    } catch (SQLException e1) {
//      e1.printStackTrace();
      System.out.println("getAllLocationIds failed");
    }
    
    return locationIds; 
  }
  
  /**
   * Retrieve all location AreaTypes, utilizing stored procedure
   * @return String array of all location AreaTypes
   */
  public String[] getAllAreaTypes() {
    String call = "CALL " + procedureNames[locAreaTypes] + "();";
    String[] types = new String[getNumLocations()]; 
    try {
      CallableStatement stmt = m_dbConn.prepareCall(call);
      stmt.execute();
      
      ResultSet rs = stmt.getResultSet(); 
      
      int i = 0;
      while(rs.next()) {
        types[i] = rs.getString("AreaType");
        i++; 
      }
      
    } catch (SQLException e1) {
      e1.printStackTrace();
      System.out.println("Syntax error (probably, dunno)");
    }
    
    return types; 
  }
  
  
  // Item Manipulation Methods
  /**
   * Get the ItemId of every item in the database , utilizing stored procedure
   * @return int array of every item id 
   */
  public int[] getAllItems() {
    String call = "CALL " + procedureNames[1] + "();";
    int[] itemIds = new int[getNumItems()]; 
    try {
      CallableStatement stmt = m_dbConn.prepareCall(call);
      stmt.execute();
  
      ResultSet rs = stmt.getResultSet(); 
      
      int i = 0;
      while(rs.next()) {  
        itemIds[i] = rs.getInt("ItemId");
        i++; 
      }
      
    } catch (SQLException e1) {
      e1.printStackTrace();
      System.out.println("Syntax error (probably, dunno)");
    }
    
    return itemIds; 
  }
  
  /**
   * Retrieve number of all items in the database
   * @return int of all items in database
   */
  public int getNumItems() {
    try {
      Statement stmt = m_dbConn.createStatement();
      ResultSet r = stmt.executeQuery("SELECT COUNT(*) FROM Item");
      // Count how many items we have
      
      r.next();
      return r.getInt("count(*)"); 
      
    } catch (SQLException e) {
      e.printStackTrace();
      return -1; // If this number is actually accidentally used, it will throw an error. 
    }
  }
  
  /**
   * Determine what the type of an item is based on its itemId.
   * @param itemId of an existing item
   * @return "Armor", "Container", or "Weapon"
   */
  public String getItemType(String itemId) {
    try {
      Statement stmt = m_dbConn.createStatement();
      ResultSet[] rs = { stmt.executeQuery("SELECT COUNT(*) FROM Armor WHERE ArmorId = " + itemId + ";"),
         stmt.executeQuery("SELECT COUNT(*) FROM Container WHERE ContId = " + itemId + ";"),
         stmt.executeQuery("SELECT COUNT(*) FROM Weapon WHERE WepId = " + itemId + ";") };
      
      for(int i = 0; i< rs.length; i++) {
        rs[i].next(); 
        if(rs[i].getInt("count(*)") > 0) {
          // If our count is > 0, we found what type our item is.  
          switch(i) {
          case(0):
            return "Armor"; 
          case(1):
            return "Container"; 
          case(2):
            return "Weapon";
          }
        }
      }
      return null; // shouldnt get here but ya know
      
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("Error in syntax or ");
      return null; // If there is an error we return null to exit, and if it is used it will cause an error
    }
  }
  
// TODO: Implement these methods
//  pubilc String[] getArmorInfo(String itemId) {
//    
//  }
//  
//  pubilc String[] getWeaponInfo(String itemId) {
//    
//  }
//  
//  pubilc String[] getContainerInfo(String itemId) {
//    
//  }
  
  // Character Layout Information
  
  /**
   * Determine how many characters a user has
   * @param username whose characters we are counting 
   * @return int of characters from user
   */
  public int getNumChars(String username) {
    try {
      Statement stmt = m_dbConn.createStatement();
      ResultSet r = stmt.executeQuery("SELECT COUNT(*) FROM Characters WHERE pUserName = '" + username + "'; ");
      // Count how many characters a user has 
      
      r.next();
      return r.getInt("count(*)"); 
      
    } catch (SQLException e) {
      e.printStackTrace();
      return -1; // If this number is actually accidentally used, it will throw an error. 
    }
  }
  
  /**
   * Retrieve number of total players in the database.
   * @return number of players
   */
  public int getNumPlayers() {
    try {
      Statement stmt = m_dbConn.createStatement();
      ResultSet r = stmt.executeQuery("SELECT COUNT(*) FROM Player");
      // Count how many players we have
      
      r.next();
      return r.getInt("count(*)"); 
      
    } catch (SQLException e) {
      e.printStackTrace();
      return -1; // If this number is actually accidentally used, it will throw an error. 
    }
  }
  
  /**
   * Retrieve all characters from a specific user given a username.
   * Utilizes store procedure. 
   * 
   * @param username of desired player
   * @return String[] of all character names that username has. 
   */
  public String[] getAllCharacterNamesFromUser(String username) {
    String call = "CALL " + procedureNames[2] + "(?);";
    String[] names = new String[getNumChars(username)]; 
    try {
      CallableStatement stmt = m_dbConn.prepareCall(call);
      stmt.setString(1, username);
      stmt.execute();
      
      ResultSet rs = stmt.getResultSet(); 
      
      int i = 0;
      while((rs.next()) && (i < names.length)) {
        names[i] = rs.getString("Name");
        i++; 
      }
      
    } catch (SQLException e1) {
      e1.printStackTrace();
      System.out.println("Syntax error (probably, dunno)");
    }
    
    return names; 
  }

  /**
   * Retrieve all player usernames, utilizing stored procedure
   * @return String array of all player usernames
   */
  public String[] getAllPlayerUsernames() {
    String call = "CALL " + procedureNames[playerUsernames] + "();";
    String[] usernames = new String[getNumPlayers()]; 
    try {
      CallableStatement stmt = m_dbConn.prepareCall(call);
      stmt.execute();
      
      ResultSet rs = stmt.getResultSet(); 
      
      int i = 0;
      while(rs.next()) {
        usernames[i] = rs.getString("Username");
        i++; 
      }
      
    } catch (SQLException e1) {
      e1.printStackTrace();
      System.out.println("Syntax error (probably, dunno)");
    }
    
    return usernames; 
  }

  /**
   * im gonna use a stored procedure because this function will prob be called a lot
   * so hopefully using a stored procedure is faster than just select statements and
   * maybe thats the point of a store procedure. dunno.
   * 
   * @param characterName
   * @return
   */
  public String[] getCharacterStats(String characterName) {
    String call = "CALL " + procedureNames[playerUsernames] + "();";
    String[] stats = new String[5];
    try {
      CallableStatement stmt = m_dbConn.prepareCall(call);
      stmt.execute();
      
      ResultSet rs = stmt.getResultSet(); 
      
      rs.next();
      
      System.out.println(rs.getInt("MaxHP"));
      stats[0] = "" + rs.getInt("MaxHP");      
      stats[1] = "" + rs.getInt("CurrentHP");   
      stats[2] = "" + rs.getInt("Strength");    
      stats[3] = "" + rs.getInt("Stamina"); 
      stats[4] = "" + rs.getInt("LocationId");
      
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
    
    return stats; 
  }
  
  /**
   * 
   * @param string
   * @return
   */
  public String getLocType(String idNumber) {
    String select = "SELECT AreaType FROM Location WHERE IdNumber = '" + idNumber + "';";
    String areatype = null; 
    try {
      PreparedStatement stmt = m_dbConn.prepareStatement(select);
      stmt.execute(); 
      
      ResultSet rs = stmt.getResultSet(); 
      
      rs.next(); 
      areatype = rs.getString("AreaType");
      
    } catch(SQLException e) {
      
    }
    return areatype;
  }
}