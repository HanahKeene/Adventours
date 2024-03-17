package com.example.adventours.ui.adapters;

public class WeeklyForecast {

    private String day;
    private String minTemperature;
    private String maxTemperature;
    private String conditionIconUrl;

    public WeeklyForecast(String day, String minTemperature, String maxTemperature, String conditionIconUrl) {
        this.day = day;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.conditionIconUrl = conditionIconUrl;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getConditionIconUrl() {
        return conditionIconUrl;
    }

    public void setConditionIconUrl(String conditionIconUrl) {
        this.conditionIconUrl = conditionIconUrl;
    }
}
