package com.raj.profarmingapp;

public class Info {

    Boolean fertilizerPump,nitrogenValve,phosphorousValve,potassiumValve,waterPump;
    Integer humidity,moisture,temperature,nitrogen,pH,phosphorous,potassium;

    Info ()
    {

    }

    ///Irrigation Values
    public Info(Boolean fertilizerPump,Boolean waterPump)
    {
        this.fertilizerPump=fertilizerPump;
        this.waterPump=waterPump;
    }

    ///Irrigation Values
    public Info(Boolean nitrogenValve,Boolean phosphorousValve,Boolean potassiumValve)
    {
        this.nitrogenValve=nitrogenValve;
        this.phosphorousValve=phosphorousValve;
        this.potassiumValve=potassiumValve;
    }

    ///Sensor Values
    public Info(Integer humidity,Integer moisture,Integer temperature)
    {
        this.humidity=humidity;
        this.moisture=moisture;
        this.temperature=temperature;

    }

    ///Soil Content Values
    public Info(Integer nitrogen,Integer pH,Integer phosphorous,Integer potassium)
    {
        this.nitrogen=nitrogen;
        this.pH=pH;
        this.phosphorous=phosphorous;
        this.potassium=potassium;
    }

}
