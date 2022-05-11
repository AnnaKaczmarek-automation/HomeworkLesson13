package models;
public class Product {

    private String name;
    private double price;
    private int quantity;
    private double totalPrice;

    public Product(String name, double price, int quantity, double totalPrice) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void updateTotalPrice(double totalPrice) {
        this.totalPrice += totalPrice;
    }
}
