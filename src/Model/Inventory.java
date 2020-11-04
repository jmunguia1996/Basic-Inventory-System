package Model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class controls inventory objects
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds parts to an inventory's part list
     * @param part The part to be added to the list
     */
    public static void addPart(Part part){
        allParts.add(part);
    }

    /**
     * Adds products to an inventory's product list
     * @param product The product to be added
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }

    /**
     * Searches an inventory's part list for a given search parameter (Overloaded)
     * @param partID The part ID to be searched
     * @return Returns a matching part found
     */
    public static Part lookupPart(int partID){
        Part foundPart = null;

        for(Part part: allParts) {
            if (part.getID() == partID) {
                foundPart = part;
                break;
            }
        }

        return foundPart;
    }

    /**
     * Searches an inventory's product list for a given search parameter (Overloaded)
     * @param productID The product ID to be searched
     * @return Returns a matching product found
     */
    public static Product lookupProduct(int productID){
        Product foundProduct = null;

        for(Product product: allProducts){
            if(product.getID() == productID){
                foundProduct = product;
            }
        }


        return foundProduct;
    }

    /**
     * Overloaded, see lookupPart above.
     * @param partName The part name to be searched.
     * @return Returns a matching part found.
     * If I were to update this program, I would make a more precise search filter using RegEx.
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> foundPart = FXCollections.observableArrayList();

        for(Part part: allParts){
            if (part.getName().toUpperCase().contains(partName)) {
                foundPart.add(part);
            }
        }

        return foundPart;
    }

    /**
     * Overloaded, see lookupProduct above
     * @param productName The product name to be searched
     * @return Returns a matching product found
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> foundProduct = FXCollections.observableArrayList();

        for(Product product: allProducts){
            if(product.getName().toUpperCase().contains(productName)){
                foundProduct.add(product);
            }
        }


        return foundProduct;
    }

    /**
     * Updates a part in an inventory's part list
     * @param index The index of the part to be updated
     * @param selectedPart The updated part to be passed in
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * Updates a product in an inventory's product list
     * @param index The index of the product to be updated
     * @param newProduct The updated product to be passed in
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * Removes a part from an inventory's part list
     * @param selectedPart The part to be deleted
     * @return Returns whether or not the part was successfully deleted
     */
    public static boolean deletePart(Part selectedPart){
        boolean deleted = false;
        for(Part part: allParts){
            if(part.equals(selectedPart)){
                allParts.remove(selectedPart);
                deleted = true;
            }
        }
        return deleted;
    }

    /**
     * Removes a product from an inventory's product list
     * @param selectedProduct The product to be deleted
     * @return Returns whether or not the product was successfully deleted
     */
    public static boolean deleteProduct(Product selectedProduct){
        boolean deleted = false;
        for(Product product: allProducts){
            if(product.equals(selectedProduct)){
                allProducts.remove(product);
                deleted = true;
            }
        }
        return deleted;
    }

    /**
     * Getter method for an inventory's part list
     * @return Returns the part list
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Getter method for an inventory's product list
     * @return Returns the product list
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
