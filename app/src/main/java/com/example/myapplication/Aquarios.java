package com.example.myapplication;

public class Aquarios {

    private String email;
    private Float ph;
    private Float temp;


    public Aquarios(String email, Float ph, Float temp) {
        this.email = email;
        this.ph = ph;
        this.temp = temp;
    }

    public Aquarios(){
        email = "";
        ph = 0.0f;
        temp = 0.0f;
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

}
