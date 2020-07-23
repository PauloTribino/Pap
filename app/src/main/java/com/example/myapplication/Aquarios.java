package com.example.myapplication;

public class Aquarios {

    private String email;
    private Float ph;
    private Float temp;
    private Integer brightness;
    private Integer colorLamp;
    private Integer lampLevel;
    private Integer lightauto;
    private Float maxtemp;
    private Integer starttpa;
    private Integer dias;
    private Integer datafinaltpa;


    public Aquarios(String email, Float ph, Float temp, Integer brightness, Integer colorLamp, Integer lampLevel, Integer lightauto, Float maxtemp, Integer starttpa, Integer dias, Integer datafinaltpa) {
        this.email = email;
        this.ph = ph;
        this.temp = temp;
        this.brightness = brightness;
        this.colorLamp = colorLamp;
        this.lampLevel = lampLevel;
        this.lightauto = lightauto;
        this.maxtemp = maxtemp;
        this.starttpa = starttpa;
        this.dias = dias;
        this.datafinaltpa = datafinaltpa;
    }

    public Aquarios(){
        email = "";
        ph = 0.0f;
        temp = 0.0f;
        brightness = 0;
        colorLamp = 0;
        lampLevel = 0;
        lightauto = 0;
        maxtemp = 0.0f;
        starttpa = 0;
        dias = 0;
        datafinaltpa = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Float getPh() {
        return ph;
    }

    public void setPh(Float ph) {
        this.ph = ph;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public Integer getBrightness() { return brightness; }

    public void setBrightness(Integer brightness){ this.brightness = brightness; }

    public Integer getLampColor() { return colorLamp; }

    public void setLampColor(Integer colorLamp){ this.colorLamp = colorLamp; }

    public Integer getLampLevel() { return lampLevel; }

    public void setLampLevel(Integer lampLevel){ this.lampLevel = lampLevel; }

    public Integer getLightauto() { return lightauto; }

    public void setLightauto(Integer lightauto){ this.lightauto = lightauto; }

    public Float getMaxtemp () { return maxtemp;}

    public Integer getStarttpa() {return starttpa;}

    public Integer getDias() { return dias;}

    public Integer getDatafinaltpa() {return datafinaltpa;}
}
