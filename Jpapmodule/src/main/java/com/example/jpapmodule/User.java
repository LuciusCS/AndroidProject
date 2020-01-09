package com.example.jpapmodule;


import com.example.annotation.Logger;

@Logger
public class User {

    String name;

    String city;

    public User(String name,String city){
        this.name=name;
        this.city=city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
