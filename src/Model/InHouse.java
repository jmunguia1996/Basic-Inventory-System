package Model;

/**
 * a class to designate a part as an In House part
 *
 */
public class InHouse extends Part {
    private int machineID;

    /**
     * constructor
      * @param partID The part ID
     * @param name Part name
     * @param price Part price
     * @param stock Designates number of part in stock
     * @param min Minimum allowable stock of parts
     * @param max Maximum allowable stock of parts
     * @param machineID Source machine ID
     */
    public InHouse(int partID, String name, double price, int stock, int min, int max, int machineID){
        super(partID, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * Sets a new machine ID
     * @param newMachineID The new ID
     */
    public void setMachineID(int newMachineID){
        this.machineID = newMachineID;
    }

    /**
     * Machine ID getter method
     * @return Returns the machine ID
     */
    public int getMachineID(){
        return machineID;
    }
}
