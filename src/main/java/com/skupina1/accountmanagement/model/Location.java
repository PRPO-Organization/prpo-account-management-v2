package com.skupina1.accountmanagement.model;

public class Location{
    private long longitude;
    private long latitude;

    public Location(){}

    public Location(long lat, long lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public long getLatitude() {
        return this.latitude;
    }

    public long getLongitude() {
        return longitude;
    }
}
