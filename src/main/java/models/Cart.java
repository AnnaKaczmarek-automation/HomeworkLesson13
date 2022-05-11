package models;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Product> productsList;

    public Cart(List<Product> productsList) {
        this.productsList = productsList;
    }

    public Cart() {
        this.productsList = new ArrayList<>();
    }

    public List<Product> getProductsList() {
        return productsList;
    }

    public Double sumProductValuesInCart(Cart elementList) {
        double sum = 0;
        for (Product product : elementList.getProductsList()) {
            sum += product.getPrice() * product.getQuantity();
        }
        return sum;
    }

    public void addNewProduct(Product productToAdd) {
        if (!isProductInList(productToAdd.getName())) {
            productsList.add(productToAdd);
            return;
        }

        for (Product product : productsList) {
            if (product.getName().equals(productToAdd.getName())) {
                product.addQuantity(productToAdd.getQuantity());
            }
            if (product.getName().equals(productToAdd.getName())) {
//                product.updateTotalPrice(productToAdd.getPrice());
                product.updateTotalPrice(productToAdd.getTotalPrice());
            }
        }
    }

    public boolean isProductInList(String productName) {
        for (Product product : productsList) {
            if (product.getName().equals(productName)) {
                return true;
            }
        }
        return false;
    }

    public Double getTotalOrderCost() {
        double totalCost = 0;
        double cost = 0;
        DecimalFormat dFormat = new DecimalFormat("#,###.##");

        for (Product product : getProductsList()) {
            cost += product.getTotalPrice();

        }
        double total = Double.parseDouble(String.valueOf(cost));
        totalCost = Double.parseDouble(dFormat.format(total));
        return totalCost;
    }

    public void removeFirstProduct() {
        productsList.remove(0);
    }
}
