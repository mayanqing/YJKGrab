package com.romens.yjkgrab.utils;

import android.util.Log;

import com.avos.avoscloud.AVObject;
import com.romens.yjkgrab.model.Customer;
import com.romens.yjkgrab.model.Order;
import com.romens.yjkgrab.model.Product;
import com.romens.yjkgrab.model.Shop;

public class AnalysisHelper {
    public static class OrderHelper {
        public static Order analysis(AVObject avObject) {
            Order order = new Order();
            order.setCreatedAt(avObject.getCreatedAt());
            order.setCustomerId(avObject.getAVObject("customer").getObjectId());
            order.setShopId(avObject.getAVObject("shop").getObjectId());
            order.setProductId(avObject.getAVObject("product").getObjectId());
            order.setObjectId(avObject.getObjectId());
            order.setStatus(avObject.getString("status"));
            order.setProductNum(avObject.getString("productNum"));
            order.setRemarks(avObject.getString("remarks"));
            order.setPickupDate(avObject.getDate("pickupDate"));
            order.setServiceDate(avObject.getDate("serviceDate"));
            order.setGrabDate(avObject.getDate("grabDate"));
            order.setExpectDate(avObject.getDate("expectDate"));
            order.setDistance(Float.valueOf(avObject.getString("distance")));
            order.setOrderId(avObject.getString("orderId"));
            order.setInstallationId(avObject.getString("installationId"));
            return order;
        }
    }

    public static class ShopHelper {
        public static Shop analysis(AVObject avObject) {
            Shop shop = new Shop();
            shop.setAddress(avObject.getString("address"));
            shop.setName(avObject.getString("name"));
            shop.setObjectId(avObject.getObjectId());
            shop.setPhone(avObject.getString("phone"));
            shop.setPicture(avObject.getString("picture"));
            shop.setAvGeoPoint(avObject.getAVGeoPoint("location"));
            return shop;
        }
    }

    public static class ProductHelper {
        public static Product analysis(AVObject avObject) {
            Product product = new Product();
            product.setObjectId(avObject.getObjectId());
            product.setName(avObject.getString("name"));
            product.setCompany(avObject.getString("company"));
            product.setPacking(avObject.getString("packing"));
            product.setSpecifications(avObject.getString("specifications"));
            return product;
        }
    }

    public static class CustomHelper {
        public static Customer analysis(AVObject avObject) {
            Customer customer = new Customer();
            customer.setObjectId(avObject.getObjectId());
            customer.setName(avObject.getString("name"));
            customer.setPhone(avObject.getString("phone"));
            customer.setAddress(avObject.getString("adress"));
            customer.setAvGeoPoint(avObject.getAVGeoPoint("location"));
            return customer;
        }
    }
}
