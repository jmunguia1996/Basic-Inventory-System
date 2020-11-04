/*
 * Supplied class Part.java
 */
package Model;

/**
 * An abstract class for InHouse and Outsourced to inherit from
 * @author Place Your Name Here
 */
public abstract class Part {
    private int partID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Part(int partID, String name, double price, int stock, int min, int max) {
        this.partID = partID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getID() {
        return partID;
    }

    /**
     * @param newPartID the id to set
     */
    public void setID(int newPartID) {

        this.partID = newPartID;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
    
}