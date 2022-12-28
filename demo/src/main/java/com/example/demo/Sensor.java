package com.example.demo;

import java.util.Objects;

public class Sensor {
    private double x;

    private double y;

    private boolean isHead;

    private Integer headId = -1;

    private Integer id;


    private Double energyTemp = 0.5;

    private double receiveEnergy = Tool.E_ELEC * Tool.PACKET_SIZE;


    private boolean isActive = true;

    public Sensor(double x, double y, Integer id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }


    public void sendToHead(double distance) {
        energyTemp -= Tool.E_ELEC * Tool.PACKET_SIZE + Tool.E_AMP_INTRA * Tool.PACKET_SIZE * Math.pow(distance, 2);
    }

    public void sendToStation(double distance) {
        energyTemp -= Tool.E_ELEC * Tool.PACKET_SIZE + Tool.E_AMP * Tool.PACKET_SIZE * Math.pow(distance, 2) + receiveEnergy;
    }


    public Double getEnergyTemp() {
        return energyTemp;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isValidHead(double distance) {
        return isHead() && energyTemp > (receiveEnergy +
                //send energy
                Tool.E_ELEC * Tool.PACKET_SIZE
                + Tool.E_AMP * Tool.PACKET_SIZE * distance * distance);
    }

    public boolean isValidSensor(double distance) {
        return energyTemp > Tool.E_ELEC * Tool.PACKET_SIZE
                + Tool.E_AMP_INTRA * Tool.PACKET_SIZE * distance * distance;
    }


    @Override
    public boolean equals(Object obj) {
        Sensor temp = (Sensor) obj;
        return Objects.equals(this.x, temp.x) && Objects.equals(this.y, temp.y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public Integer getHeadId() {
        return headId;
    }

    public void setHeadId(Integer headId) {
        this.headId = headId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
