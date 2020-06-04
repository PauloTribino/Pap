package com.example.myapplication;

public class Aquarios {

    private String email;
    private Float ph;
    private Float temp;
    private Integer lamps;


    public Aquarios(String email, Float ph, Float temp, Integer lamps) {
        this.email = email;
        this.ph = ph;
        this.temp = temp;
        this.lamps = lamps;
    }

    public Aquarios(){
        email = "";
        ph = 0.0f;
        temp = 0.0f;
        lamps = 0;
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

    public Integer getLamps() { return lamps; }

    public void setLamps(Integer lamps){ this.lamps = lamps; }



}
