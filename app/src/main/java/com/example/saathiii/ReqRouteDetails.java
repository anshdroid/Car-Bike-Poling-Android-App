package com.example.app.saathiii;

public class ReqRouteDetails {

    private String rname;
    private String rdestination;
    private String rmobile;
    private String rtime;
    private String rdate;
    private String rseat;
    public String rimageURL;
    private String rsource;


    public ReqRouteDetails() {
        // This is default constructor.
    }

    public ReqRouteDetails(String name, String url, String mobile, String destination, String source, String time, String date, String seat) {

        this.rname = name;
        this.rimageURL= url;
        this.rdestination=destination;
        this.rmobile=mobile;
        this.rdate=date;
        this.rtime=time;
        this.rseat=seat;
        this.rsource=source;
    }

    public String getRName() {

        return rname;
    }
    public void setRName(String rname) {

        this.rname= rname;
    }

    public String getRimageURL() {

        return rimageURL;
    }
    public void setRimageURL(String rimageURL) {

        this.rimageURL= rimageURL;
    }

    public String getRMobile() {

        return rmobile;
    }
    public void setRMobile(String rmobile) {

        this.rmobile = rmobile;
    }

    public String getRDestination() {

        return rdestination;
    }
    public void setRDestination(String rdestination) {

        this.rdestination = rdestination;
    }

    public String getRTime() {

        return rtime;
    }
    public void setRTime(String rtime) {

        this.rtime = rtime;
    }

    public String getRDate() {

        return rdate;
    }
    public void setRDate(String rdate) {

        this.rdate = rdate;
    }

    public String getRSeat() {

        return rseat;
    }
    public void setRSeat(String rseat) {

        this.rseat = rseat;
    }

    public String getRSource() {

        return rsource;
    }
    public void setRSource(String rsource) {

        this.rsource= rsource;
    }


}
