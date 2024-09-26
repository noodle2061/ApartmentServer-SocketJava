/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class Floor {
    private int floorId;
    private String name, description;
    private boolean inUseStatus;

    public Floor() {
    }

    public Floor(int floorId, String name, String description, boolean inUseStatus) {
        this.floorId = floorId;
        this.name = name;
        this.description = description;
        this.inUseStatus = inUseStatus;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInUseStatus() {
        return inUseStatus;
    }

    public void setInUseStatus(boolean inUseStatus) {
        this.inUseStatus = inUseStatus;
    }

    @Override
    public String toString() {
        return "Floor{" + "floorId=" + floorId + ", name=" + name + ", description=" + description + ", inUseStatus=" + inUseStatus + '}';
    }

}
