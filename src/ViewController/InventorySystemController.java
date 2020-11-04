package ViewController;

import Main.Main;
import Model.Inventory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Model.Part;
import Model.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

import java.util.Optional;

/**
 * A class to control the main window of the application.
 */
public class InventorySystemController{
    private Main mainWindow;
    private Inventory inventory;

    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partQTYColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIDColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productQTYColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    @FXML
    private Button modifyPartButton;
    @FXML
    private Button deletePartButton;
    @FXML
    private Button modifyProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private TextField searchPartField;
    @FXML
    private TextField searchProductField;

    public InventorySystemController(){
    }

    /**
     * JavaFX initialization parameters.
     * A runtime error occurred when attempting to display a part or product's ID field. This was corrected by
     * capitalizing "ID" here.
     */
    @FXML
    private void initialize(){


        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partQTYColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productQTYColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));



        changeButtonState(partTable, true);
        changeButtonState(productTable, true);

        partTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) ->
                changeButtonState(partTable, newSelection == null));
        productTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) ->
                changeButtonState(productTable, newSelection == null));
    }

    /**
     * Builds the window.
     * @param app The main method for the program.
     */
    public void buildMainScreen(Main app)
    {
        mainWindow = app;
        inventory = app.getInventory();
        partTable.setItems(inventory.getAllParts());
        productTable.setItems(inventory.getAllProducts());
    }

    /**
     * Adjusts buttons on the window according to functional availability.
     * @param table The table whose buttons are to be adjusted.
     * @param state The state of the buttons' availability.
     */
    private void changeButtonState(TableView<?> table, boolean state)
    {
        if (table.equals(partTable))
        {
            modifyPartButton.setDisable(state);
            deletePartButton.setDisable(state);
        }
        else if (table.equals(productTable))
        {
            modifyProductButton.setDisable(state);
            deleteProductButton.setDisable(state);
        }
    }

    /**
     * Closes the program.
     */
    @FXML
    private void executeExit()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        alert.setHeaderText("Are you sure you want to quit?");
        alert.initOwner(mainWindow.getPrimaryStage());

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent()){
            if (result.get() == ButtonType.OK)
            {
                Platform.exit();
            }
        }
    }

    /**
     * Opens a window to add new parts.
     */
    @FXML
    private void executeAddPart()
    {
        mainWindow.showPartWindow(null);
    }

    /**
     * Opens a window to modify a part.
     */
    @FXML
    private void executeModifyPart()
    {
        Part existingPart = partTable.getSelectionModel().getSelectedItem();
        mainWindow.showPartWindow(existingPart);
    }

    /**
     * Search function for the part table.
     */
    @FXML
    private void executeSearchPart()
    {
        String searchText = searchPartField.getText();
        if(searchText == null || searchText.isEmpty())
        {
            partTable.setItems(inventory.getAllParts());
        }
        else{
            try{
                ObservableList<Part> partsFound = FXCollections.observableArrayList(inventory.lookupPart(Integer.parseInt(searchText)));
                if(partsFound.isEmpty()){
                    throw new Exception();
                }
                else{
                    partTable.setItems(partsFound);
                }
            }
            catch (NumberFormatException e) {
                ObservableList<Part> partsFound = FXCollections.observableArrayList(inventory.lookupPart(searchText.toUpperCase()));
                if(partsFound.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainWindow.getPrimaryStage());
                    alert.setTitle("Part Not Found");
                    alert.setContentText("No part with a matching name or ID was found.");
                    alert.showAndWait();
                }
                else{
                    partTable.setItems(partsFound);
                }
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainWindow.getPrimaryStage());
                alert.setTitle("Part Not Found");
                alert.setContentText("No part with a matching name or ID was found.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Listens for the enter key in the part search field to search.
     */
    @FXML
    private void onPartEnter(){
        searchPartField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                executeSearchPart();
            }
        });
    }

    /**
     * Deletes a part.
     */
    @FXML
    private void executeDeletePart()
    {
        Part part = partTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete?");
        alert.setHeaderText("Are you sure you want to delete " + part.getName() + "?");
        alert.initOwner(mainWindow.getPrimaryStage());
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent()){
            if (result.get() == ButtonType.OK)
            {
                inventory.deletePart(part);
            }
        }
    }

    /**
     * Opens a window to add a new product.
     */
    @FXML
    private void executeAddProduct()
    {
        mainWindow.showProductWindow(null);
    }

    /**
     * Opens a window to modify a product.
     */
    @FXML
    private void executeModifyProduct()
    {
        Product existingProduct = productTable.getSelectionModel().getSelectedItem();
        mainWindow.showProductWindow(existingProduct);
    }

    /**
     * Search function for the product table.
     */
    @FXML
    private void executeSearchProduct()
    {
        String searchText = searchProductField.getText();
        if(searchText == null || searchText.isEmpty())
        {
            productTable.setItems(inventory.getAllProducts());
        }
        else{
            try{
                ObservableList<Product> productsFound = FXCollections.observableArrayList(inventory.lookupProduct(Integer.parseInt(searchText)));
                if(productsFound.isEmpty()){
                    throw new Exception();
                }
                else{
                    productTable.setItems(productsFound);
                }
            }
            catch (NumberFormatException e) {
                ObservableList<Product> productsFound = FXCollections.observableArrayList(inventory.lookupProduct(searchText.toUpperCase()));
                if(productsFound.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.initOwner(mainWindow.getPrimaryStage());
                    alert.setTitle("Product Not Found");
                    alert.setContentText("No product with a matching name or ID was found.");
                    alert.showAndWait();
                }
                else{
                    productTable.setItems(productsFound);
                }
            }
            catch(Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initOwner(mainWindow.getPrimaryStage());
                alert.setTitle("Product Not Found");
                alert.setContentText("No product with a matching name or ID was found.");
                alert.showAndWait();
            }
        }
    }

    /**
     * Listens for the enter key in the search product field to search.
     */
    @FXML
    private void onProductEnter(){
        searchProductField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.ENTER)){
                executeSearchProduct();
            }
        });
    }

    /**
     * Deletes a product.
     */
    @FXML
    private void executeDeleteProduct() {
        Product product = productTable.getSelectionModel().getSelectedItem();

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete?");
        confirmation.setHeaderText("Are you sure you want to delete " + product.getName() + "?");
        confirmation.initOwner(mainWindow.getPrimaryStage());
        Optional<ButtonType> confirmResult = confirmation.showAndWait();

        if(confirmResult.isPresent()){
            try {
                if(!product.getAllAssociatedParts().isEmpty()){
                    throw new Exception();
                }
                else if (confirmResult.get() == ButtonType.OK) {
                    inventory.deleteProduct(product);
                }
            }
            catch (Exception e) {
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Product Not Empty!");
                warning.setContentText("This product still contains one or more parts.");
                warning.showAndWait();
            }
        }
    }
}
