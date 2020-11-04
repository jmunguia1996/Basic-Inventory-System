package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A class to control all product objects
 */
public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Constructor
     * @param productID The product ID
     * @param name The product name
     * @param price The product price
     * @param stock The number of the product in stock
     * @param min The minimum allowable stock of a product
     * @param max The maximum allowable stock of a product
     */
    public Product(int productID, String name, double price, int stock, int min, int max){
        this.productID = productID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * Sets an ID for a product
     * @param newProductID The new ID
     */
    public void setID(int newProductID){
        this.productID = newProductID;
    }

    /**
     * Sets a name for a product
     * @param name The new name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Sets a price for a product
     * @param price The new price
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     * Sets a stock for a product
     * @param stock The new stock
     */
    public void setStock(int stock){
        this.stock = stock;
    }

    /**
     * Sets a minimum stock for a product
     * @param min The new minimum stock
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     * Sets a maximum stock for a product
     * @param max The new maximum stock
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     * Getter method for ID
     * @return Product ID
     */
    public int getID(){
        return productID;
    }

    /**
     * Getter method for name
     * @return The product name
     */
    public String getName(){
        return name;
    }

    /**
     * Getter method for price
     * @return The product price
     */
    public double getPrice(){
        return price;
    }

    /**
     * Getter method for stock
     * @return Product in stock
     */
    public int getStock(){
        return stock;
    }

    /**
     * Getter method for min
     * @return The minimum allowable stock of a product
     */
    public int getMin(){
        return min;
    }

    /**
     * Getter method for max
     * @return The maximum allowable stock of a product
     */
    public int getMax(){
        return max;
    }

    /**
     * Gets the sum of prices of all parts of a product
     * @return Thee sum of prices of all parts of a product
     */
    public double getPartPriceSum(){
        double partSum = 0;
        for (Part part: associatedParts) {
            partSum+=part.getPrice();
        }
        return partSum;
    }

    /**
     * Adds parts to a product
     * @param part The part to add
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Deletes parts from a product
     * @param selectedAssociatedPart The part to be removed
     */
    public void deleteAssociatedPart(Part selectedAssociatedPart){
        associatedParts.remove(selectedAssociatedPart);
    }

    /**
     * Getter method for the part list of a product
     * @return The part list of a product
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
