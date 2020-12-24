package model;

import javafx.beans.value.ObservableValue;
import utils.DataTransferObject;

import java.util.Calendar;

public class Customer extends Address {

    private Long id;
    private String customerName;
    private Long addressId;
    private Byte active;
    private String createDate;
    private String createdBy;
    private String lastUpdate;
    private String lastUpdateBy;

    public Customer(String address, String city, String country, String postalCode, String phone, Long id, String customerName, Byte active, String createDate) {
        super(address, city, country, postalCode, phone);
        this.id = id;
        this.customerName = customerName;
        this.active = active;
        this.createDate = createDate;
    }

    public Customer(Long id, String customerName, Long addressId, Byte active, String createDate, String createdBy, String lastUpdate, String lastUpdateBy) {
        this.id = id;
        this.customerName = customerName;
        this.addressId = addressId;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    public Customer() { super(); }

//    public Customer(Long customerId, String customerName, Byte active, String address, String postalCode, String phone, String city, String country, String createDate) {
//        this.id = customerId;
//        this.customerName = customerName;
//        this.active = active;
//        this.address = address;
//
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public Long getAddressId() {
        return addressId;
    }

    @Override
    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Byte getActive() {
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
    }

    @Override
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    @Override
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
}
