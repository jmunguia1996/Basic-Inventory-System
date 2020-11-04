package Model;

/**
 * A class to designate a part as an Outsourced part
 */
public class Outsourced extends Part{
    private String companyName;

    /**
     * constructor
     * @param partID The part ID
     * @param name Part name
     * @param price Part price
     * @param stock Designates number of part in stock
     * @param min Minimum allowable stock of parts
     * @param max Maximum allowable stock of parts
     * @param companyName Source company name
     */
    public Outsourced(int partID, String name, double price, int stock, int min, int max, String companyName){
        super(partID, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets a new company name
     * @param newCompanyName The new company name
     */
    public void setCompanyName(String newCompanyName){
        this.companyName = newCompanyName;
    }

    /**
     * Company name getter method
     * @return Returns the company name
     */
    public String getCompanyName(){
        return companyName;
    }
}
