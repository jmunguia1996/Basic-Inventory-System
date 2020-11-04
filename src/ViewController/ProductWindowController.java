package ViewController;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * A class to control product windows
 */
public class ProductWindowController {
    private Stage windowStage;
    private boolean isNewProduct;
    private Inventory inventory;
    private Product selectedProduct;
    private Product editProduct;

    @FXML
    private Label titleLabel;
    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField inStockField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField maxField;
    @FXML
    private TextField minField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Part> addPartTable;
    @FXML
    private TableColumn<Part, Integer> addPartIDColumn;
    @FXML
    private TableColumn<Part, String> addPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> addPartQTYColumn;
    @FXML
    private TableColumn<Part, Double> addPartPriceColumn;
    @FXML
    private TableView<Part> delPartTable;
    @FXML
    private TableColumn<Part, Integer> delPartIDColumn;
    @FXML
    private TableColumn<Part, String> delPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> delPartQTYColumn;
    @FXML
    private TableColumn<Part, Double> delPartPriceColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;

    public ProductWindowController()
    {

    }

    /**
     * JavaFX initialization parameters
     */
    @FXML
    private void initialize()
    {
        addPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        addPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartQTYColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        delPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        delPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        delPartQTYColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        delPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        this.idField.setText(String.valueOf(inventory.getAllProducts().size() + 1));

        deleteButton.setDisable(true);

        addPartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
                addButton.setDisable(newSel == null));
        delPartTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) ->
                deleteButton.setDisable(newSel == null));
    }

    /**
     * Builds the window
     * @param stage JavaFX stage object
     * @param existingInventory The inventory from the main window
     * @param selectedProduct The currently selected product of the main window
     */
    public void setWindowScreen(Stage stage, Inventory existingInventory, Product selectedProduct)
    {
        windowStage = stage;
        inventory = existingInventory;
        this.selectedProduct= selectedProduct;
        addPartTable.setItems(inventory.getAllParts());

        if (selectedProduct == null)
        {
            isNewProduct = true;
            titleLabel.setText("Add Product");
        }
        else
        {
            isNewProduct = false;
            titleLabel.setText("Modify Product");
            setProductInfo(selectedProduct);
        }
    }

    /**
     * Populates the window with info on an existing product to be updated or created
     * @param product The product to be updated or created
     */
    public void setProductInfo(Product product)
    {
        idField.setText(Integer.toString(product.getID()));
        nameField.setText(product.getName());
        inStockField.setText(Integer.toString(product.getStock()));
        minField.setText(Integer.toString(product.getMin()));
        maxField.setText(Integer.toString(product.getMax()));
        priceField.setText(String.valueOf(product.getPrice()));
        delPartTable.setItems(product.getAllAssociatedParts());
    }

    /**
     * Search function of the miniaturized part window
     */
    @FXML
    private void executeSearch()
    {
        String searchText = searchField.getText();
        if(searchText == null || searchText.isEmpty())
        {
            addPartTable.setItems(inventory.getAllParts());
        }
        else{
            try{
                ObservableList<Part> partsFound = FXCollections.observableArrayList(inventory.lookupPart(Integer.parseInt(searchText)));
                if(partsFound == null){
                    throw new Exception();
                }
                else{
                    addPartTable.setItems(partsFound);
                }
            }
            catch (NumberFormatException e) {
                ObservableList<Part> partsFound = FXCollections.observableArrayList(inventory.lookupPart(searchText.toUpperCase()));
                if(partsFound == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(windowStage);
                    alert.setTitle("Part Not Found");
                    alert.setContentText("No part with a matching name or ID was found.");
                    alert.showAndWait();
                }
                else{
                    addPartTable.setItems(partsFound);
                }
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(windowStage);
                alert.setTitle("Part Not Found");
                alert.setContentText("No part with a matching name or ID was found.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Adds a part to the product's associated parts
     */
    @FXML
    private void executeAdd()
    {
        Part partToAdd = addPartTable.getSelectionModel().getSelectedItem();
        try
        {
            double price = 0;
            String priceString = priceField.getText();
            if(priceString != null && !priceString.isEmpty())
            {
                price = Double.parseDouble(priceString);
            }
            delPartTable.getItems().add(partToAdd);

            double partCost = getCostOfParts();

            if (partCost > price)
            {
                price += partToAdd.getPrice();
                priceString = String.valueOf(price);
                priceField.setText(priceString);
            }
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a part from a product's associated parts
     */
    @FXML
    private void executeRemoveAssociatedPart()
    {
        Part partToDelete = delPartTable.getSelectionModel().getSelectedItem();

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove Part?");
        confirm.setHeaderText("Are you sure you want to remove this part?");
        confirm.initOwner(windowStage);

        Optional<ButtonType> confirmationResult = confirm.showAndWait();

        if(confirmationResult.isPresent()){
            if (confirmationResult.get() == ButtonType.OK)
            {
                if(editProduct == null){
                    delPartTable.getItems().remove(partToDelete);
                }
                else{
                    editProduct.deleteAssociatedPart(partToDelete);
                    delPartTable.getItems().remove(partToDelete);
                }
                Alert notice = new Alert(Alert.AlertType.INFORMATION);
                notice.setTitle("Removed");
                notice.setContentText("The selected part has been removed from this product.");
                notice.showAndWait();
            }
        }

    }

    /**
     * Saves new or updated products
     */
    @FXML
    private void executeSave()
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(windowStage);
        alert.setTitle("Product Not Saved!");

        String productID = idField.getText();
        String name = nameField.getText();
        String inStockString = inStockField.getText();
        String priceString = priceField.getText();
        String maxString = maxField.getText();
        String minString = minField.getText();
        ObservableList<Part> parts = delPartTable.getItems();
        try
        {
            if(name.isEmpty() || inStockString.isEmpty() ||
                    priceString.isEmpty() || maxString.isEmpty() ||
                    minString.isEmpty() )
            {
                throw new IllegalArgumentException("All text fields must have a value.");
            }

            int modIndex;
            double price = Double.parseDouble(priceString);
            int inStock = Integer.parseInt(inStockString);
            int min = Integer.parseInt(minString);
            int max = Integer.parseInt(maxString);

            double partCost = getCostOfParts();

            if (min > max){
                throw new IllegalArgumentException("Value \"min\" cannot be greater than value \"max\".");
            }

            if(inStock > max || inStock < min){
                throw new IllegalArgumentException("Value \"inStock\" must be within the range of \"min\" and \"max\".");
            }

            if (partCost > price)
            {
                throw new IllegalArgumentException("Price cannot be less than the total\n" + "price of all the parts it contains.");
            }
            editProduct = new Product(Integer.parseInt(productID), name, price, inStock, min, max);

            if(isNewProduct)
            {
                ObservableList<Product> searchProduct = FXCollections.observableArrayList(inventory.lookupProduct(editProduct.getName()));
                if (searchProduct.isEmpty())
                {
                    inventory.addProduct(editProduct);
                    for(Part part: parts){
                        editProduct.addAssociatedPart(part);
                    }
                }
            }
            else
            {
                modIndex = inventory.getAllProducts().indexOf(selectedProduct);
                inventory.updateProduct(modIndex, editProduct);
                for(Part part: parts){
                    editProduct.addAssociatedPart(part);
                }
            }
        }
        catch (NumberFormatException e)
        {
            alert.setContentText("There was an invalid number in one of the fields. \n" + "Please try again.");
            alert.showAndWait();
            return;
        }
        catch (IllegalArgumentException e)
        {
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        windowStage.close();
    }

    /**
     * Closes the window without making any changes
     */
    @FXML
    private void executeCancel()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        if (isNewProduct)
        {
            alert.setContentText("Exit without saving new product?");
        }
        else
        {
            alert.setContentText("Exit without updating product?");
        }
        alert.setHeaderText("Product Not Saved!");
        alert.initOwner(windowStage);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if (result.get() == ButtonType.OK)
            {
                windowStage.close();
            }
        }
    }

    /**
     * Calculates the sum of the cost of all associated parts
     * @return Returns the sum
     */
    private double getCostOfParts()
    {
        double partCost = 0;
        ObservableList<Part> partsContained = delPartTable.getItems();

        if (partsContained == null || partsContained.isEmpty())
        {
            return partCost;
        }
        for(Part part : partsContained)
        {
            partCost += part.getPrice();
        }
        return partCost;
    }
}
