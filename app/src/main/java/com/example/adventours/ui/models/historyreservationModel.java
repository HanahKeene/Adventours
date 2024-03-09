package com.example.adventours.ui.models;

public class historyreservationModel {

    String RoomName;
    String HotelName;
    String RestaurantName;
    String reservationId;
    String Guests;
    String CheckIn;
    String CheckOut;
    String status;

    historyreservationModel(){

    }

    public historyreservationModel(String roomName, String hotelName, String restaurantName, String reservationId, String guests, String checkIn, String checkOut, String status) {
        this.RoomName = roomName;
        this.HotelName = hotelName;
        this.RestaurantName = restaurantName;
        this.reservationId = reservationId;
        this.Guests = guests;
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

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuests() {
        return Guests;
    }

    public void setGuests(String guests) {
        Guests = guests;
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
