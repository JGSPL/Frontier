package com.procialize.mrgeApp20.GetterSetter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {

    @SerializedName("date_time")
    @Expose
    private String dateTime;
    @SerializedName("cityname")
    @Expose
    private String cityname;
    @SerializedName("countryname")
    @Expose
    private String countryname;
    @SerializedName("current_temp")
    @Expose
    private float currentTemp;
    @SerializedName("current_temp_text")
    @Expose
    private String currentTempText;

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    @SerializedName("max")
    @Expose
    private String max;
    @SerializedName("min")
    @Expose
    private String min;
    @SerializedName("weather_url")
    @Expose
    private String weatherUrl;
    @SerializedName("humidity")
    @Expose
    private String humidity;
    @SerializedName("visibility")
    @Expose
    private String visibility;
    @SerializedName("sunrise")
    @Expose
    private String sunrise;
    @SerializedName("sunset")
    @Expose
    private String sunset;
    @SerializedName("today_weather")
    @Expose
    private TodayWeather todayWeather;

    public void setForecast(List<Forecast> forecast) {
        this.forecast = forecast;
    }

    public List<Forecast> getForecast() {
        return forecast;
    }

    @SerializedName("forecast")
    @Expose
    private List<Forecast> forecast = null;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public float getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(Integer currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getCurrentTempText() {
        return currentTempText;
    }

    public void setCurrentTempText(String currentTempText) {
        this.currentTempText = currentTempText;
    }



    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public TodayWeather getTodayWeather() {
        return todayWeather;
    }

    public void setTodayWeather(TodayWeather todayWeather) {
        this.todayWeather = todayWeather;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}