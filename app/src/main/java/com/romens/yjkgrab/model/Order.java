package com.romens.yjkgrab.model;

import com.avos.avoscloud.AVGeoPoint;

import java.util.Date;

/**
 * Created by myq on 15-12-10.
 */
public class Order extends BaseModel {

    private float distance;
    private String objectId;
    private Date createdAt;
    private String status;
    private String productNum;
    private String customerId;
    private String productId;
    private String shopId;
    private Shop shop;
    private Customer customer;
    private Product product;
    private Date grabDate;
    private Date pickupDate;
    private Date serviceDate;
    private Date expectDate;
    private String remarks;
    private String orderId;

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }

    private String installationId;
    /**
     * 所有信息是否齐全
     * @return
     */
    public boolean isPerfect() {
        if (getCustomer() == null)
            return false;
        if (getShop() == null)
            return false;
        if (getProduct() == null)
            return false;
        return true;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Date getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Date expectDate) {
        this.expectDate = expectDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getGrabDate() {
        return grabDate;
    }

    public void setGrabDate(Date grabDate) {
        this.grabDate = grabDate;
    }

    public Date getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(Date pickupDate) {
        this.pickupDate = pickupDate;
    }

    public Date getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = serviceDate;
    }

    public Shop getShop() {
        return shop;
    }

    public Order setShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public Order setProduct(Product product) {
        this.product = product;
        return this;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

}
