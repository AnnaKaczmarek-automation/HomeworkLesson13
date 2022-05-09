package models;

import org.openqa.selenium.WebDriver;
import pages.BasePage;
import pages.BasketPage;

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

//    BasketPage basketPage = new BasketPage();

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

    //metoda addNewProduct
    public void addNewProduct(Product productToAdd) {
        if (!isProductInList(productToAdd.getName())) {
            productsList.add(productToAdd);
            return;
        }

        for (Product product : productsList) {
            if (product.getName().equals(productToAdd.getName())) {
                product.addQuantity(productToAdd.getQuantity());
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
        //ktora zsumiuje total costs dla wszystkich elementów w
        double totalCost=0;
        double cost=0;
        for (Product product : getProductsList()) {
            totalCost += product.getTotalPrice();
        }
        totalCost = cost + 7.00;
        return totalCost;
    }


    public void removeFirstProduct() {
        productsList.remove(0);
    }
}
