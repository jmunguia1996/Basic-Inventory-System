package Main;

import Model.Inventory;
import Model.Part;
import Model.Product;
import ViewController.InventorySystemController;
import ViewController.PartWindowController;
import ViewController.ProductWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Johann Schafer
 * This class controls the launch of the inventory system.
 */
public class Main extends Application {

    private Inventory inventory;
    private Stage primaryStage;

    /**
     * This method initializes an Inventory object.
     */
    public Main(){
        inventory = new Inventory();
    }

    /**
     * This method is responsible for generating the main window.
     * @param primaryStage Hands the stage object to a javafx start method.
     */
    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;

        initRootLayout();
    }

    /**
     * Generic main method to launch program.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Method for passing private inventory to other classes.
     * @return Returns the private inventory object.
     */
    public Inventory getInventory()
    {
        return inventory;
    }

    /**
     * Method for passing the main window to other classes.
     * @return Returns the private stage object.
     */
    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    /**
     * JavaFX tool for displaying the main window
     */
    public void initRootLayout()
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ViewController/InventorySystem.fxml"));
            BorderPane root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);

            InventorySystemController controller = loader.getController();
            controller.buildMainScreen(this);
            primaryStage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Calls for the part window. This method calls and titles the part window.
     * @param part Uses a part param to determine if a part in the inventory is being modified or a new part is being created.
     */
    public void showPartWindow(Part part)
    {
        String title;
        if (part == null)
        {
            title = "Add a New Part";
        }
        else
        {
            title = "Modify an Existing Part";
        }
        try
        {
            FXMLLoader partLoader = new FXMLLoader();
            partLoader.setLocation(Main.class.getResource("/ViewController/PartWindow.fxml"));
            AnchorPane partWindowRoot = partLoader.load();

            Stage partWindowStage = new Stage();
            partWindowStage.setTitle(title);
            partWindowStage.initModality(Modality.WINDOW_MODAL);
            partWindowStage.initOwner(primaryStage);

            Scene scene = new Scene(partWindowRoot);
            partWindowStage.setScene(scene);

            PartWindowController controller = partLoader.getController();
            controller.setWindowScreen(partWindowStage, inventory, part);

            partWindowStage.showAndWait();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Calls for the part window. This method calls and titles the part window.
     * @param product Uses a product param to determine if a product in the inventory is being modified or a new product is being created.
     */
    public void showProductWindow(Product product)
    {
        String title;
        if (product == null)
        {
            title = "Add a New Product";
        }
        else
        {
            title = "Modify an Existing Product";
        }
        try
        {
            FXMLLoader productLoader = new FXMLLoader();
            productLoader.setLocation(Main.class.getResource("/ViewController/ProductWindow.fxml"));
            AnchorPane productWindowRoot = productLoader.load();

            Stage productWindowStage = new Stage();
            productWindowStage.setTitle(title);
            productWindowStage.initModality(Modality.WINDOW_MODAL);
            productWindowStage.initOwner(primaryStage);
            Scene scene = new Scene(productWindowRoot);
            productWindowStage.setScene(scene);

            ProductWindowController controller = productLoader.getController();
            controller.setWindowScreen(productWindowStage, inventory, product);
            productWindowStage.showAndWait();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
