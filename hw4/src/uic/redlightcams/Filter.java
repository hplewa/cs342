package uic.redlightcams;

import java.io.File;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;

import org.json.JSONArray;
import uic.redlightcams.DataPoint;
import uic.redlightcams.FilterException;
import uic.redlightcams.config.FilterParams;
import uic.redlightcams.enums.Column;
import uic.redlightcams.enums.OutputOptions;
import uic.redlightcams.enums.SortDirection;



/**
 * Class that handles the hard work of reading DataPoint records out of
 * a JSON file, and then filtering them accoring to some given configuraiton
 * parameters.
 */
public class Filter {

  protected List<DataPoint> data;

  /**
   * Constructor for object for filtering DataPoint results.
   *
   * @param jsonRecords Stream  A stream of JSONArray items, each describing a
   *                            single redlight camera datapoint.
   *
   * @throws FilterException If there was an error processing, or otherwise
   *                         processing the data set.
   */
  public Filter(Stream<JSONArray> jsonRecords) throws FilterException {
    // Your code should do something here to parse the inputFile into a series
    // uic.redlightcams.DataPoint objects.
    
    data = jsonRecords.map(t -> { 
      return new DataPoint(t); 
    })
    .collect(Collectors.toList());
  }

  /**
   * Generates textual description of the data set.
   *
   * <p>The returned textual description should reflect the inital data set,
   * with the filtering, sorting and aggregating parameters described by
   * the provided filtering paramters applied.
   *
   * <p>In the general case, this will be a rows of columns:
   *   - Intersection
   *   - Camera ID
   *   - Address
   *   - Date
   *   - Num Violations
   *   - Latitude
   *   - Longitude
   *
   * <p>If, though, were aggregating / merging all dates for each camera, then
   * the generated rows should have the columns:
   *   - Intersection
   *   - Camera ID
   *   - Address
   *   - Sum of Num Violations, across all dates
   *   - Latitude
   *   - Longitude
   *
   * @param params FilterParams Settings describing how to modify or restrict
   *                            the data set.
   * @param outputType OutputOptions The type of output to generate (JSON or
   *                                 or CSV).
   *
   * @return String A textual description of the sorted, filtered and aggregated
   *                data set.
   *
   * @throws FilterException If there was an error processing, or otherwise
   *                         processing the data set.
   */
  public String generateReport(FilterParams params, OutputOptions outputType)
      throws FilterException {

    if (params.getShouldMerge() == true) {
      data = mergeData();
    }

    if (params.getFilterValue() != null) {
      data = filterByColumn(params);
    }

    if (params.getSortColumn() != null) {
      data = sortByColumn(params);
    }

    if (params.getMilesFromUic() != null) {
      data = inRangeOfUic(params);
    }

    return outputAsString(outputType);
  }

  /**
   * Returns whether a DataPoint's column value matches the filter parameter's volumn value.
   *
   * @param d DataPoint to compare a column of.
   * 
   * @param p FilterParams The filters a user wishes reduce the data by.
   * 
   * @return boolean Does the DataPoint's clumn value match the parameter's?
   */  
  private boolean dataPointColumnEquals(DataPoint d, FilterParams p) {
    if (p.getFilterColumn() == Column.CAMERA_ID) {
      return d.getCameraId().toString().equalsIgnoreCase(p.getFilterValue());

    } else if (p.getFilterColumn() == Column.ADDRESS) {
      return d.getAddress().equalsIgnoreCase(p.getFilterValue());

    } else if (p.getFilterColumn() == Column.DATE) {
      return d.getDate().equalsIgnoreCase(p.getFilterValue());

    } else if (p.getFilterColumn() == Column.INTERSECTION) {
      return d.getIntersection().equalsIgnoreCase(p.getFilterValue());

    } else {
      return true;
    }
  }

  /**
   * Filters out DataPoints from the set read in from a file which 
   * do not match a user specified filter parameter.
   * 
   * @param p FilterParams, user provided to filter out DataPoints.
   *
   * @return List(DataPoint) A new list containing DataPoints matching the filter.
   */
  private List<DataPoint> filterByColumn(FilterParams p) {
    return data.stream()
                .filter(d -> {
                  return dataPointColumnEquals(d, p);
                })
                .collect(Collectors.toList());
  }

  /**
   * Sorts the DataPoints of the larger data list according to a FilterParams.
   * 
   * @param p FilterParams contains the column and direction to sort the data list by.
   *
   * @return List(DataPoint) The DataPoints of larger data list in sorted order.
   */
  private List<DataPoint> sortByColumn(FilterParams p) {
    List<DataPoint> dataHolder;
    if (p.getSortColumn() == Column.CAMERA_ID) {
      dataHolder = data.stream()
                 .sorted((d1, d2) -> {
                   return Integer.compare(d1.getCameraId(), d2.getCameraId());
                 })
                 .collect(Collectors.toList());
    } else {
      dataHolder = data.stream()
            .sorted((d1, d2) -> {
              String s1 = d1.getColumnValue(p.getSortColumn());
              String s2 = d2.getColumnValue(p.getSortColumn());
              return s1.compareTo(s2);
            })
            .collect(Collectors.toList());
    }

    if (p.getSortDirection() == SortDirection.DESC) {
      Collections.reverse(dataHolder);
    }

    return dataHolder;

  }

  /**
   * Returns a list of DataPoints whose coordinates are within a certain range of UIC.  
   * 
   * @param p FilterParams contains the distance from UIC in miles the user wishes to see
   *          DataPoints for.
   *
   * @return List(DataPoint) A list of DataPoints whose coordinates
   *                         are within range of UIC.
   */
  private List<DataPoint> inRangeOfUic(FilterParams p) {
    return data.stream()
                .filter(d -> {
                  return inRange(d, p.getMilesFromUic());
                })
                .collect(Collectors.toList());
  }

  /**
   * Returns true if the supplied DataPoint is within range of UIC.
   * 
   * @param d DataPoint to compare coordinates to UIC's
   * 
   * @param distFromUic Integer the acceptable range for a DataPoint to be from UIC
   *
   * @return boolean is the DataPoint within range of UIC
   */
  private boolean inRange(DataPoint d, Integer distFromUic) {
    double lat1 = 41.8756;
    double long1 = -87.6244;

    double lat2 = d.getLatitude();
    double long2 = d.getLongitude();

    double pi = 3.14159265;
    double earthRad = 3963.1;  // statue miles:
    
    double lat1Rad = lat1 * pi / 180.0;
    double long1Rad = long1 * pi / 180.0;
    double lat2Rad = lat2 * pi / 180.0;
    double long2Rad = long2 * pi / 180.0;
    
    double dist = earthRad 
                 * Math.acos(
                    (Math.cos(lat1Rad) * Math.cos(long1Rad) 
                    * Math.cos(lat2Rad) * Math.cos(long2Rad)) 
                    + (Math.cos(lat1Rad) * Math.sin(long1Rad) 
                    * Math.cos(lat2Rad) * Math.sin(long2Rad)) 
                    + (Math.sin(lat1Rad) * Math.sin(lat2Rad))
                  );
    
    return dist <= distFromUic;
  }

  /**
   * Returns a new list of merged DataPoints, with number of violations aggregated
   * for each unique cameraId, and dates blanked out.
   *
   * @return List(DataPoint) a new List made from the larger List with merged DataPoints.
   */
  private List<DataPoint> mergeData() {
    HashMap<Integer, DataPoint> map = new HashMap<>();

    List<DataPoint> mergedData = data.stream()
                                      .filter(d -> {
                                        d.removeDate();
                                        if (map.containsKey(d.getCameraId()) == false) {
                                          map.put(d.getCameraId(), d);
                                          return true;
                                        } else {
                                          map.get(d.getCameraId()).addNumViolations(d);;
                                          return false;
                                        }
                                      })
                                      .collect(Collectors.toList());
    mergedData.clear();
    mergedData.addAll(map.values());
    return mergedData;
  }

  /**
   * Returns a report of the data after all filtering operations the user has 
   * specified have been completed. Outputs a String either in CSV or JSON formats
   * depending on what the user has specified.
   * 
   * @param o OutputOptions how should the report be formatted.
   *
   * @return String A report of DataPoints after user specified filtration.
   */
  private String outputAsString(OutputOptions o) {
    if (o == OutputOptions.CSV) {
      return data.stream()
                  .map(d -> {
                    return d.getAllFields().stream()
                                            .collect(Collectors.joining(", "));
                  })
                  .collect(Collectors.joining("\n"));
    } else if (o == OutputOptions.JSON) {
      JSONArray parentArray = new JSONArray();
      List<String> children = data.stream()
                                  .map(d -> {
                                    JSONArray childArray = new JSONArray();
                                    String item = d.getAllFields().stream()
                                                            .map(s -> {
                                                              childArray.put(s);
                                                              return s;
                                                            })
                                                            .collect(Collectors.joining(", "));
                                    parentArray.put(childArray);
                                    return item;
                                  })
                                  .collect(Collectors.toList());
      return parentArray.toString(2);

    } else {
      return "";
    }
  }
}
