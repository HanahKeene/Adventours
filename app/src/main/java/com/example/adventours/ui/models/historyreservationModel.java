package com.example.adventours.ui.models;

public class historyreservationModel {

    String RoomName;
    String HotelName;
    String reservationId;
    String CheckIn;
    String CheckOut;
    String status;

    historyreservationModel(){

    }

    public historyreservationModel(String roomName, String hotelName, String reservationId, String checkIn, String checkOut, String status) {
        this.RoomName = roomName;
        this.HotelName = hotelName;
        this.reservationId = reservationId;
        this.CheckIn = checkIn;
        this.CheckOut = checkOut;
        this.status = status;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getHotelName() {
        return HotelName;
    }

    public void setHotelName(String hotelName) {
        HotelName = hotelName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}