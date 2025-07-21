package org.example.orchidsstore.Entity;

public class CartItem {
    private Long orchidId;
    private String orchidName;
    private String orchidUrl;
    private double price;
    private int quantity;

    public CartItem() {}
    public CartItem(Long orchidId, String orchidName, String orchidUrl, double price, int quantity) {
        this.orchidId = orchidId;
        this.orchidName = orchidName;
        this.orchidUrl = orchidUrl;
        this.price = price;
        this.quantity = quantity;
    }
    public Long getOrchidId() { return orchidId; }
    public void setOrchidId(Long orchidId) { this.orchidId = orchidId; }
    public String getOrchidName() { return orchidName; }
    public void setOrchidName(String orchidName) { this.orchidName = orchidName; }
    public String getOrchidUrl() { return orchidUrl; }
    public void setOrchidUrl(String orchidUrl) { this.orchidUrl = orchidUrl; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
} 