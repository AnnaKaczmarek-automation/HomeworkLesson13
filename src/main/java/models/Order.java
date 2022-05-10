package models;

public class Order {

    private String date;
    private double price;
    private String payment;
    private String status;

    public Order(String date,double price,String payment,String status){
        this.date = date;
        this.price = price;
        this.payment = payment;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    public String getPayment() {
        return payment;
    }

    public String getStatus() {
        return status;
    }


}
