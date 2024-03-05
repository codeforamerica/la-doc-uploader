package org.ladocuploader.app.data.enums;



public enum Parish {
  
  ACADIA("Acadia", "825 Kaliste Saloom Street", "","Lafayette", "70508"),
  ALLEN("Allen", "116 SW Railroad., ", "Suite B", "Ville Platte", "70586"),
  ASCENSION("Ascension", "1442 Tiger Drive", "","Thibodaux", "70301"),
  ASSUMPTION("Assumption", "1442 Tiger Drive", "", "Thibodaux", "70301"),
  AVOYELLES("Avoyelles", "900 Murray Street", "", "Alexandria", "71301"),
  BEAUREGARD("Beauregard", "1417 Gadwall Street", "", "Lake Charles", "70615"),
  BIENVILLE("Bienville", "1525 Fairfield Ave., ", "Suite 141", "Shreveport", "71101"),
  BOSSIER("Bossier", "1525 Fairfield Ave., ", "Suite 141", "Shreveport", "71101"),
  CADDO("Caddo", "1525 Fairfield Ave., ", "Suite 141", "Shreveport", "71101"),
  CALCASIEU("Calcasieu", "1417 Gadwall Street", "", "Lake Charles", "70615"),
  CALDWELL("Caldwell", "951 Century Blvd.", "", "Monroe", "71202"),
  CAMERON("Cameron", "1417 Gadwall Street", "", "Lake Charles", "70615"),
  CATAHOULA("Catahoula", "900 Murray Street", "", "Alexandria",  "71301"),
  CLAIBORNE("Claiborne", "1525 Fairfield Ave., ", "Suite 141", "Shreveport", "71101"),
  CONCORDIA("Concordia", "900 Murray Street", "", "Alexandria", "71301"),
  DESOTO("DeSoto", "1774 Texas Street", "", "Natchitoches", "71457"),
  EAST_BATON_ROUGE("East Baton Rouge", "5825 Florida Blvd.", "", "Baton Rouge","70806"),
  EAST_CARROLL("East Carroll", "1614 Felicia Drive", "", "Tallulah",  "71282"),
  EAST_FELICIANA("East Feliciana", "5825 Florida Blvd.", "", "Baton Rouge", "70806"),
  EVANGELINE("Evangeline", "116 SW Railroad., ", "Suite B", "Ville Platte", "70586"),
  FRANKLIN("Franklin", "1614 Felicia Drive", "", "Tallulah",  "71282"),
  GRANT("Grant", "900 Murray Street", "", "Alexandria",  "71301"),
  IBERIA("Iberia", "825 Kaliste Saloom Street", "", "Lafayette", "70508"),
  IBERVILLE("Iberville", "5825 Florida Blvd.", "", "Baton Rouge", "70806"),
  JACKSON("Jackson", "1525 Fairfield Ave., ","Suite 141", "Shreveport", "71101"),
  JEFFERSON("Jefferson", "1630 Iberville St., ", "Suite 2800", "New Orleans", "70112"),

  JEFFERSON_DAVIS("Jefferson Davis", "1417 Gadwall Street", "", "Lake Charles", "70615"),
  LAFAYETTE("Lafayette", "825 Kaliste Saloom Street", "", "Lafayette",  "70508"),
  LAFOURCHE("Lafourche", "1442 Tiger Drive", "", "Thibodaux", "70301"),
  LASALLE("LaSalle", "900 Murray Street", "", "Alexandria", "71301"),
  LINCOLN("Lincoln", "951 Century Blvd.", "", "Monroe",  "71202"),
  LIVINGSTON("Livingston", "606 S. 1st Street", "", "Amite",  "70422"),
  MADISON("Madison", "1614 Felicia Drive", "",  "Tallulah",  "71282"),
  MOREHOUSE("Morehouse", "951 Century Blvd.", "", "Monroe","71202"),
  NATCHITOCHES("Natchitoches", "1774 Texas Street", "", "Natchitoches", "71457"),
  ORLEANS("Orleans", "1630 Iberville St., ", " Suite 2800", "New Orleans", "70112"),
  OUACHITA("Ouachita", "951 Century Blvd.", "", "Monroe", "71202"),
  PLAQUEMINES("Plaquemines", "1630 Iberville St., ", "Suite 2800", "New Orleans", "70112"),
  POINTE_COUPEE("Pointe Coupee", "5825 Florida Blvd.", "", "Baton Rouge",  "70806"),
  RAPIDES("Rapides", "900 Murray Street", "", "Alexandria", "71301"),
  RED_RIVER("Red River", "1774 Texas Street", "", "Natchitoches",  "71457"),
  RICHLAND("Richland", "1614 Felicia Drive", "", "Tallulah",  "71282"),
  SABINE("Sabine", "1774 Texas Street", "", "Natchitoches", "71457"),
  ST_BERNARD("St. Bernard", "1630 Iberville St, ", "Suite 2800", "New Orleans", "70112"),
  ST_CHARLES("St. Charles", "1442 Tiger Drive", "", "Thibodaux", "70301"),
  ST_HELENA("St. Helena", "606 S. 1st Street", "", "Amite", "70422"),
  ST_JAMES("St. James", "1442 Tiger Drive", "", "Thibodaux", "70301"),
  ST_JOHN("St. John", "1442 Tiger Drive", "", "Thibodaux", "70301"),
  ST_LANDRY("St. Landry", "116 SW Railroad., ", "Suite B", "Ville Platte", "70586"),
  ST_MARTIN("St. Martin", "825 Kaliste Saloom Street", "", "Lafayette", "0508"),
  ST_MARY("St. Mary", "825 Kaliste Saloom Street", "", "Lafayette", "70508"),
  ST_TAMMANY("St. Tammany", "606 S. 1st Street", "", "Amite", "70422"),
  TANGIPAHOA("Tangipahoa", "606 S. 1st Street", "", "Amite", "0422"),
  TENSAS("Tensas", "1614 Felicia Drive", "", "Tallulah", "71282"),
  TERREBONNE("Terrebonne", "1442 Tiger Drive", "", "Thibodaux", "70301"),
  UNION("Union", "951 Century Blvd.", "",  "Monroe", "71202"),
  VERMILION("Vermilion", "825 Kaliste Saloom Street", "", "Lafayette", "70508"),
  VERNON("Vernon", "900 Murray Street", "", "Alexandria", "71301"),
  WASHINGTON("Washington", "606 S. 1st Street", "", "Amite", "70422"),
  WEBSTER("Webster", "1525 Fairfield Ave., ", "Suite 141", "Shreveport", "71101"),
  WEST_BATON_ROUGE("West Baton Rouge", "5825 Florida Blvd.", "", "Baton Rouge", "70806"),
  WEST_CARROLL("West Carroll", "1614 Felicia Drive", "", "Tallulah", "71282"),
  WEST_FELICIANA("West Feliciana", "5825 Florida Blvd.", "", "Baton Rouge", "70806"),
  WINN("Winn", "900 Murray Street", "", "Alexandria", "71301");
  private final String displayName;

  private final String mailingAddress1;

  private final String mailingAddress2;

  private final String mailingAddressCity;

  private final String mailingAddressState;

  private final String mailingAddressZipcode;

  private static final String LOUISIANA = "LA";

  Parish(String displayName, String mailingAddress1, String mailingAddress2, String mailingAddressCity,
         String mailingAddressZipcode) {
    this.displayName = displayName;
    this.mailingAddressCity = mailingAddressCity;
    this.mailingAddressState = Parish.LOUISIANA;
    this.mailingAddressZipcode = mailingAddressZipcode;
    this.mailingAddress1 = mailingAddress1;
    this.mailingAddress2 = mailingAddress2;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getMailingAddressLine1() {
    return mailingAddress1 + mailingAddress2;
  }

  public String getMailingAddressLine2(){
    return mailingAddressCity + ", " + mailingAddressState + " " + mailingAddressZipcode;
  }

}
