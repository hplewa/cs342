package uic.redlightcams;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import uic.redlightcams.enums.Column;


/**
 * This class represents a single day's datapoint for information about
 * how many violations were recorded at a given location, on a give date.
 */
public class DataPoint {
  private String intersection;
  private Integer cameraId;
  private String address;
  private String date;
  private Integer numViolations;
  private double latitude;
  private double longitude;

  /**
   * Constructor for object containing a red light camera violation data.
   * If a piece of data is missing in the file being read, the constructor 
   * handles this error by apply a default value for the parameter.
   *
   * @param j JSONArray A single red light camera violation.
   *
   */
  DataPoint(JSONArray j) {
    try {
      intersection = j.getString(0);
    } catch (JSONException e) {
      intersection = "";
    }
    try {
      cameraId = Integer.parseInt(j.getString(1));
    } catch (JSONException e) {
      cameraId = -1;
    }
    try {
      address = j.getString(2);
    } catch (JSONException e) {
      address = "";
    }    
    try {
      date = j.getString(3);
    } catch (JSONException e) {
      date = "";
    }    
    try {
      numViolations = Integer.parseInt(j.getString(4));
    } catch (JSONException e) {
      numViolations = 0;
    }    
    try {
      latitude = Double.parseDouble(j.getString(5));
    } catch (JSONException e) {
      latitude = 0;
    }    
    try {
      longitude = Double.parseDouble(j.getString(6));
    } catch (JSONException e) {
      longitude = 0;
    }
  }

  /**
   * Takes a Column, and returns value for that Column in this datapoint.
   * Returns an empty String should the Column contain an invalid value.
   * 
   * @param c Column The column to be looked up.
   * 
   * @return String The value of a column as a String.
   */
  public String getColumnValue(Column c) {
    if (c == Column.INTERSECTION) {
      return intersection;
    } else if (c == Column.CAMERA_ID) {
      return cameraId.toString();
    } else if (c == Column.ADDRESS) { 
      return address;
    } else if (c == Column.DATE) {
      return date;
    } else {
      return "";
    }
  }

  /**
   * Returns the Intersection, CameraId, Address, Date, and 
   * Number of violations for a datapoint as an ArrayList of Strings.
   *
   * @return ArrayList<String></String> The Intersection, CameraId, Address, Date, and 
   *                           Number of violations as an ArrayList of Strings .
   */
  public ArrayList<String> getAllFields() {
    ArrayList<String> d = new ArrayList<>();
    d.add(intersection);
    d.add(cameraId.toString());
    d.add(address);
    d.add(date);
    d.add(numViolations.toString());
    d.add(Double.toString(latitude));
    d.add(Double.toString(longitude));
    return d;
  }

  /**
   * Returns the name of the intersection.
   *
   * @return String The intersection of this datapoint.
   */
  public String getIntersection() {
    return intersection;
  }

  /**
   * Returns the camera's ID number.
   *
   * @return Integer The camera's ID number.
   */
  public Integer getCameraId() {
    return cameraId;
  }

  /**
   * Returns the address of the datapoint.
   *
   * @return String The address of the datapoint.
   */
  public String getAddress() {
    return address;
  }    

  /**
   * Returns the date of the incident.
   *
   * @return String The date an incident occurred.
   */
  public String getDate() {
    return date;
  }    

  /**
   * Removes the date of the incident. Use when merging datapoints.
   */
  public void removeDate() {
    date = "";
  }

  /**
   * Returns the count of violations.
   *
   * @return Integer The number of violations count of this datapoint.
   */
  public Integer getNumViolations() {
    return numViolations;
  }    

  /**
   * Adds the number of violations in d to the caller object.
   * Used for merging datapoints.
   * 
   * @param d DataPoint object.
   */
  public void addNumViolations(DataPoint d) {
    numViolations = numViolations + d.getNumViolations();
  }

  /**
   * Returns the value of the latitude.
   *
   * @return double The latitude of this datapoint.
   */
  public double getLatitude() {
    return latitude;
  }    

  /**
   * Returns the value of the longitude.
   *
   * @return double The longitude of this datapoint.
   */
  public double getLongitude() {
    return longitude;
  }
  
  /**
   * Prints all fields of this DataPoint to stdout.
   */
  public void print() {
    System.out.println(intersection + ", " + cameraId + ", " + date + ", " + address + ", "
                        + numViolations + ", " + latitude + ", " + longitude);
  }
}
