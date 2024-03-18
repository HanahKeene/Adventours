package com.example.adventours.ui.models;

public class MyItineraryCalendarModel {

    private String UserID;
    private String HotelName;
    private String CheckIn;
    private String CheckOut;
    private int day;


    public MyItineraryCalendarModel(){

    }

    public MyItineraryCalendarModel(String userID, String hotelName, String checkIn, String checkOut, int day) {
        UserID = userID;
        HotelName = hotelName;
        CheckIn = checkIn;
        CheckOut = checkOut;
        this.day = day;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getCheckIn() {
        return CheckIn;
    }

    public void setCheckIn(String checkIn) {
        CheckIn = checkIn;
    }

    public String getCheckOut() {
        return CheckOut;
    }

    public void setCheckOut(String checkOut) {
        CheckOut = checkOut;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
