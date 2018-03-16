
package CommissionCalculator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author Henry
 */
public class CommissionCalculator {

 private static Scanner input = new Scanner(System.in);      // input as a Scanner object (used for keyboard monitoring)
    private static boolean allEqual = true; //used to check if all values in the array list are equal

    //Entry point
    public static void main(String[] args) {
        {
            System.out.println("Index\tPossible Compensation");
        int possibleCompensation[] = {100000, 105000, 110000, 115000, 120000, 125000, 130000, 135000, 140000, 145000, 150000};
        for (int counter = 0; counter < possibleCompensation.length; counter++) {
            System.out.println(counter + "\t" + possibleCompensation[counter]);
        }
        }
        ArrayList<SalesPerson> employees = new ArrayList(); //Arraylist of SalesPerson
        int highestIndex = 0; //Index of salesperson with highest compensation

        double annualSales;     //Used to retrieve user input
        String name = "";       //For salesperson's name.
        //Output Application Name
        System.out.println("Salesperson Annual Compensation Calculator\n");

        int counter = 0; //counter
        while (true) {
            //Need at least 2.
            //When we reached 2, we can now ask user if user wants to add more.
            if (counter >= 2) {
                System.out.print("Enter another? Y for yes. Anything else for No: ");
                if (!input.nextLine().toLowerCase().equals("y")) {
                    break;
                }
            }

            //Ask for user input. Validate as well :)
            do {
                System.out.print("Enter the name of salesperson: ");
                name = input.nextLine();
            } while (name.length() < 1);

            annualSales = Utils.getDoubleInput("Please enter " + name + "'s annual sales: ");

            
            employees.add(new SalesPerson(name, annualSales));

            //Check for highest earner
            boolean isHighest = true;
            for (int i = 0; i < employees.size(); i++) {
                if (employees.get(counter).totalCompensation < employees.get(i).totalCompensation) {
                    isHighest = false;
                }

                if (employees.get(counter).totalCompensation != employees.get(i).totalCompensation) {
                    allEqual = false;
                }
            }
            if (isHighest) {
                highestIndex = counter;
            }

            counter++; //increment            
        }// while (true);




        //Show comparison table:lll
        System.out.println("\n\nComparison Table:\n");
        System.out.format("%20s\t%20s\t%20s\t%20s\n", "Name", "Annual Sales", "Total Compensation", "Highest?");
        for (int i = 0; i < employees.size(); i++) {

            //Show results to user.
            System.out.format("%20s\t%20s\t%20s\t%20s\n", employees.get(i).name,
                    Utils.numFormat(employees.get(i).annualSales, "$"),
                    Utils.numFormat(employees.get(i).totalCompensation, "$"),
                    (i == highestIndex && allEqual == false) ? "Yes" : "");

        }


        //Show individual information
        System.out.println("\nPress ENTER key to show individual salesperson information.");
        input.nextLine();

        for (int i = 0; i < employees.size(); i++) {

            //Local Varibles
            SalesPerson highest = employees.get(highestIndex);
            SalesPerson current = employees.get(i);

            //Show results to user.
            System.out.println("\n\n" + current.name + "'s information:\n");
            System.out.println("Fixed Salary: " + Utils.numFormat(current.fixedSalary, "$"));
            System.out.println("Annual Sales: " + Utils.numFormat(current.annualSales, "$"));
            System.out.println("Current Commisasion: " + Utils.numFormat(current.curCommission, "$"));
            System.out.println("Current Commission Rate: " + (current.curCommissionRate * 100) + " %");
            System.out.println("Total Compensation: " + Utils.numFormat(current.totalCompensation, "$"));

            if (allEqual) {
                System.out.println("Note: \tAll salespersons earned equal amounts.");
            } else if (i == highestIndex) {
                System.out.println("Note: \t" + current.name + " is the highest earner.");
            } else {
                System.out.println("Note: "
                        + "\tTo match " + highest.name + "'s compensation, " + current.name + " needs "
                        + Utils.numFormat((highest.curCommission - current.curCommission), "$") + " more in commission.\n"
                        + "\t" + current.name + " needs "
                        + Utils.numFormat((highest.annualSales - current.annualSales), "$") + " more in annual sales.");
            }
        }

        //Ask user to repeat.
        System.out.println("\n\nRestart? Y to reset. Anything else to quit.");
        if (input.nextLine().toLowerCase().equals("y")) {
            main(args);
        }

    }
}


class SalesPerson {

    /**
     * Fixed Salary
     */
    public final double fixedSalary = 40000;
    /**
     * Regular Rate
     */
    private final double regularRate = 0.15;
    /**
     * Accelerated Rate
     */
    private final double accelerationFactor = 1.175;
    /**
     * Current Commission Rate
     */
    public double curCommissionRate = regularRate;
    /**
     * Sales Target
     */
    public final double salesTarget = 105000;
    /**
     * Current Sales Target Rate
     */
    public final double curSalesTargetRate = 0.8;
    /**
     * Current Sales Target
     */
    public double curSalesTarget = salesTarget * curSalesTargetRate;
    /**
     * Current Sales Target
     */
    public double computedSalesTarget = salesTarget * curSalesTargetRate;
    /**
     * Accrued Commission
     */
    public double curCommission = 0;
    /**
     * Total Compensation
     */
    public double totalCompensation = 0;
    /**
     * Annual Sales
     */
    public double annualSales = 0;
    /**
     * Salesperson's name
     */
    public String name = "";

    /**
     * Parameterized Constructor
     *
     * @param annualSales The Salesperson's annual sales.
     */
    public SalesPerson(String pName, double pAnnualSales) {
        name = pName; //set name
        annualSales = pAnnualSales; //set object member
        computeTotalCompensation();
    }

    /**
     * Default Constructor
     */
    SalesPerson() {
    }

    
    private void computeTotalCompensation() {
        if (annualSales >= curSalesTarget) //Target Reached
        {
            if (annualSales > salesTarget) { //Above sales target
                curCommissionRate *= accelerationFactor;
            }

        } else //Target NOT Reached
        {
            curCommissionRate = 0;
        }

        //Compute commission, then add fixed salary
        curCommission = (annualSales * curCommissionRate); //calculate current commission
        totalCompensation = fixedSalary + curCommission;
    }
}

class Utils {

    
    public static String numFormat(double dec, String symbol) {
        return new DecimalFormat(symbol + "##,##0.00").format(dec);
    }

    
    public static String numFormat(double dec) {
        return numFormat(dec, "");
    }

    
    public static double getDoubleInput(String message) {
        double goodInput = 0; //What we want to validate and return.
        try { //Try so we can catch exceptions.
            System.out.print(message); //Print message.

            //This will throw the exception if input is non double:
            goodInput = Double.parseDouble(new Scanner(System.in).nextLine());
        } catch (NumberFormatException ex) {
            //Exceptin caught!
            //Alert user, then recursively call this function again!
            System.out.println("Sorry. Please enter numbers only.");
            goodInput = getDoubleInput(message);
        }

        //Assumes goodInput is a double.
        return goodInput;

    }
}


     