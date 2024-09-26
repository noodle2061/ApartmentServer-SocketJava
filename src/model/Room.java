/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class Room {
    private int roomId;
    private String name;
    private double area; 
    private int capacity, floorId;
    private boolean inUseStatus;

    public Room() {
    }

    public Room(int roomId, String name, double area, int capacity, boolean inUseStatus, int floorId) {
        this.roomId = roomId;
        this.name = name;
        this.area = area;
        this.capacity = capacity;
        this.floorId = floorId;
        this.inUseStatus = inUseStatus;
    }


    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public boolean isInUseStatus() {
        return inUseStatus;
    }

    public void setInUseStatus(boolean inUseStatus) {
        this.inUseStatus = inUseStatus;
    }
    
    
    @Override
    public String toString() {
        return roomId + " " + name + " " + area + " " + capacity + " " + inUseStatus + " " + floorId;
    }

}
