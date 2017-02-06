package com.student.fahrtenbuchapp.dataSync;


import com.student.fahrtenbuchapp.models.Drive;

import org.json.JSONException;
import org.json.JSONObject;

public class DriveToJSONConverter {


    public JSONObject DriveToJson(Drive drive)
    {
        JSONObject driveObject = new JSONObject();
        JSONObject startCoordObject = new JSONObject();
        JSONObject endCoordObject = new JSONObject();
        JSONObject startAddressObject = new JSONObject();
        JSONObject endAddressObject = new JSONObject();
        try
        {

            startCoordObject.put("longitude", drive.getStartCoord().getLongitude());
            startCoordObject.put("latitude", drive.getStartCoord().getLatitude());
            endCoordObject.put("longitude", drive.getEndCoord().getLongitude());
            endCoordObject.put("latitude", drive.getEndCoord().getLatitude());
            startAddressObject.put("country", drive.getStartAddress().getCountry());
            startAddressObject.put("city", drive.getStartAddress().getCity());
            startAddressObject.put("zip", drive.getStartAddress().getZip());
            startAddressObject.put("street", drive.getStartAddress().getStreet());
            endAddressObject.put("country", drive.getEndAddress().getCountry());
            endAddressObject.put("city", drive.getEndAddress().getCity());
            endAddressObject.put("zip", drive.getEndAddress().getZip());
            endAddressObject.put("street", drive.getEndAddress().getStreet());


            driveObject.put("user", drive.getUser());
            driveObject.put("car", drive.getCar());
            driveObject.put("startDate", drive.getStartDate());
            driveObject.put("endDate", drive.getEndDate());
            driveObject.put("startPOI", drive.getStartPOI());
            driveObject.put("endPOI", drive.getEndPOI());
            driveObject.put("startMileage", drive.getStartMileage());
            driveObject.put("endMileage", drive.getEndMileage());
            driveObject.put("usedkwh", drive.getUsedkWh());

            driveObject.put("startCoord", startCoordObject);
            driveObject.put("endCoord", endCoordObject);
            driveObject.put("startAddress", startAddressObject);
            driveObject.put("endAddress", endAddressObject);


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return driveObject;
    }

}
