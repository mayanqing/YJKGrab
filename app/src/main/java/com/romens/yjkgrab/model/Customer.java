package com.romens.yjkgrab.model;

import com.avos.avoscloud.AVGeoPoint;

/**
 * Created by myq on 15-12-10.
 */
public class Customer extends BaseModel {
    private String objectId;
    private String name;
    private String phone;
    private String address;
    private AVGeoPoint avGeoPoint;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public AVGeoPoint getAvGeoPoint() {
        return avGeoPoint;
    }

    public void setAvGeoPoint(AVGeoPoint avGeoPoint) {
        this.avGeoPoint = avGeoPoint;
    }
}
