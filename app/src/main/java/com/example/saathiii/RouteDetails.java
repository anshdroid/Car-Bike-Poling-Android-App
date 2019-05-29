package com.example.app.saathiii;

import java.util.Map;

public class RouteDetails {

    private String source;
    private String destination;
    private String mobile;
    private String vehical;
    private String fare;
    private String time;
    private String date;
    private String seat;
    private String name;
    private  String imageURL;
    private Map<String, String> timestamp;

    public RouteDetails() {
        // This is default constructor.
    }
    public RouteDetails(String name, String url, String mobile, String destination, String time, String date, String seat, String source, String vehical, String fare) {

        this.name = name;
        this.imageURL= url;
        this.destination=destination;
        this.source=source;
        this.vehical=vehical;
        this.fare=fare;
        this.mobile=mobile;
        this.date=date;
        this.time=time;
        this.seat=seat;
    }

    public String getimageURL() {

        return imageURL;
    }
    public void setimageURL(String imageURL) {

        this.imageURL= imageURL;
    }

    public String getName() {

        return name;
    }
    public void setName(String name) {

        this.name = name;
    }

    public String getSource() {

        return source;
    }
    public void setSource(String source) {

        this.source = source;
    }

    public String getMobile() {

        return mobile;
    }
    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getDestination() {

        return destination;
    }
    public void setDestination(String destination) {

        this.destination = destination;
    }

    public String getFare() {

        return fare;
    }
    public void setFare(String fare) {

        this.fare = fare;
    }

    public String getTime() {

        return time;
    }
    public void setTime(String time) {

        this.time = time;
    }

    public String getDate() {

        return date;
    }
    public void setDate(String date) {

        this.date = date;
    }

    public String getVehical() {

        return vehical;
    }
    public void setVehical(String vehical) {

        this.vehical = vehical;
    }

    public String getSeat() {

        return seat;
    }
    public void setSeat(String seat) {

        this.seat = seat;
    }
    public void setTimestamp(Map<String, String> timeStamp) {

        this.timestamp= timeStamp;}



}