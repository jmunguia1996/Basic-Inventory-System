package ViewController;

import java.util.Optional;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * Class to control Part windows
 */
public class PartWindowController
{
    private Part selectedPart;
    public ToggleGroup source;
    private Stage windowStage;
    private boolean isNewPart;
    private Inventory inventory;

    @FXML
    private Label titleLabel;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
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
    private Label sourceLabel;
    @FXML
    private TextField sourceField;

    public PartWindowController()
    {

    }

    /**
     * JavaFX Initialization parameters
     */
    @FXML
    private void initialize()
    {
        ToggleGroup radioGroup = new ToggleGroup();

        inHouseRadio.setToggleGroup(radioGroup);
        outsourcedRadio.setToggleGroup(radioGroup);

        radioGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> sourceChanged(newToggle));

        this.idField.setText(String.valueOf(inventory.getAllParts().size() + 1));
    }

    /**
     * Builds the window
     * @param stage JavaFX Stage object
     * @param existingInventory The inventory from the main window
     * @param selectedPart The currently selected part of the main window
     */
    public void setWindowScreen(Stage stage, Inventory existingInventory, Part selectedPart)
    {
        this.selectedPart = selectedPart;
        windowStage = stage;
        inventory = existingInventory;

        if (selectedPart == null)
        {
            isNewPart = true;
            titleLabel.setText("Add Part");
        }
        else
        {
            isNewPart = false;
            titleLabel.setText("Modify Part");
            setPartInfo(selectedPart);
        }
    }

    /**
     * Populates the window with info on an existing part to be updated or created
     * @param part The part to be updated or created
     */
    public void setPartInfo(Part part)
    {
        if (part instanceof InHouse)
        {
            inHouseRadio.setSelected(true);
            sourceField.setText(Integer.toString(((InHouse)part).getMachineID()));
            sourceLabel.setText("Machine ID");
        }
        else if (part instanceof Outsourced)
        {
            outsourcedRadio.setSelected(true);
            sourceField.setText(((Outsourced)part).getCompanyName());
            sourceLabel.setText("Company Name");
        }

        idField.setText(Integer.toString(part.getID()));
        nameField.setText(part.getName());
        inStockField.setText(Integer.toString(part.getStock()));
        minField.setText(Integer.toString(part.getMin()));
        maxField.setText(Integer.toString(part.getMax()));
        priceField.setText(String.valueOf(part.getPrice()));
    }

    /**
     * Uses the radio button selection to change the source label title
     * @param toggle The selection
     */
    private void sourceChanged(Toggle toggle)
    {
        String labelText = "";

        if (toggle.equals(inHouseRadio))
        {
            labelText = "Machine ID";
        }
        else if (toggle.equals(outsourcedRadio))
        {
            labelText = "Company Name";
        }
        sourceLabel.setText(labelText);

        if (sourceField.getText().isEmpty())
        {
            sourceField.setPromptText(labelText);
        }
    }

    /**
     * Saves new parts or updates existing parts
     */
    @FXML
    private void executeSave()
    {
        Part editPart;
        boolean isInHouse = inHouseRadio.isSelected();

        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(windowStage);
        alert.setTitle("Part Not Saved!");

        int modIndex;
        String partID = idField.getText();
        String partName = nameField.getText();
        String partInStockString = inStockField.getText();
        String partPriceString = priceField.getText();
        String partMaxString = maxField.getText();
        String partMinString = minField.getText();
        String partSourceString = sourceField.getText();



        try
        {
            if(partName.isEmpty() || partInStockString.isEmpty() ||
                    partPriceString.isEmpty() || partMaxString.isEmpty() ||
                    partMinString.isEmpty() || partSourceString.isEmpty())
            {
                throw new IllegalArgumentException("All text fields must have a value.");
            }


            double price = Double.parseDouble(partPriceString);
            int inStock = Integer.parseInt(partInStockString);
            int min = Integer.parseInt(partMinString);
            int max = Integer.parseInt(partMaxString);


            String companyName;
            int machineID;

            if (min > max){
                throw new IllegalArgumentException("Value \"min\" cannot be greater than value \"max\".");
            }

            if(inStock > max || inStock < min){
                throw new IllegalArgumentException("Value \"inStock\" must be within the range of \"min\" and \"max\".");
            }

            if (isInHouse)
            {
                machineID = Integer.parseInt(partSourceString);
                editPart = new InHouse(Integer.parseInt(partID), partName, price, inStock, min, max, machineID);
            }
            else
            {
                companyName = partSourceString;
                editPart = new Outsourced(Integer.parseInt(partID), partName, price, inStock, min, max, companyName);
            }

            if(isNewPart)
            {
                ObservableList<Part> searchPart = FXCollections.observableArrayList(inventory.lookupPart(editPart.getName()));
                if (searchPart.isEmpty() || existingPartFound() == ButtonType.YES)
                {
                    inventory.addPart(editPart);
                }
            }
            else
            {
                modIndex = inventory.getAllParts().indexOf(selectedPart);
                inventory.updatePart(modIndex, editPart);
            }
        }
        catch (NumberFormatException e)
        {
            alert.setContentText("There was an invalid input in one of the fields. \n" + "Please try again.");
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
     * The function to close the part window without making changes
     */
    @FXML
    private void executeCancel()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit?");
        if (isNewPart)
        {
            alert.setContentText("Exit without saving new part?");
        }
        else
        {
            alert.setContentText("Exit without updating part?");
        }
        alert.setHeaderText("Part Not Saved!");
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
     * Checks for duplicate parts
     * @return Returns true or false for having found a duplicate part
     */
    private ButtonType existingPartFound()
    {
        ButtonType result = ButtonType.NO;
        Alert alert = new Alert(AlertType.WARNING);
        alert.initOwner(windowStage);
        alert.setTitle("Duplicate Part Found");
        alert.setHeaderText("Add duplicate part?");
        alert.setContentText("A part with that name already exists in the inventory. \n" + "Do you want to add this part?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.isPresent()){
            result = answer.get();
        }

        return result;
    }
}
