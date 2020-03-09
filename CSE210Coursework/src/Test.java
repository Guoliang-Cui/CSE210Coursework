import entity.Researcher;
import service.Functions;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static util.ImportExcelUtil.parseExcel;

/**
 * Test the program
 */
public class Test {
    /**
     * Run the main method.
     * Choose which task will be executed.
     */
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        boolean exit = false;
        String select;
        File file = new File("./Dataset_RG.xlsx");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            List<Map<String, Object>> ls = parseExcel(fis, file.getName());
            while (!exit) {
                System.out.println("Please input the operation that you want to do: " +"\n" +
                        "0 -------- exit. " +"\n" +
                        "1 -------- calculate the number of distinct researchers in the dataset. " +"\n" +
                        "2 -------- calculate the number of distinct interests in the dataset. " +"\n" +
                        "3 -------- given a researcher’s name, show detailed information about him/her. " +"\n" +
                        "4 -------- given an interest, calculate the number of researchers who have that interest. " +"\n" +
                        "5 -------- given two interests, show the number of times they co-occur. " +"\n" +
                        "6 -------- given a researcher, find similar researchers based on their interests.");
                select = kb.nextLine();
                switch (select) {
                    case "0":
                        exit = true;
                        break;
                    case "1":
                        System.out.println("the number of distinct researchers in the dataset is: " + Functions.numberOfResearchers(ls));
                        break;
                    case "2":
                        System.out.println("the number of distinct interests in the dataset is: " + Functions.numberOfInterests(ls));

                        break;
                    case "3":
                        System.out.println("Please input the Researcher's name: ");
                        String name = kb.nextLine();
                        if (Functions.showInformation(ls,name) != null){
                            List<Researcher> researcherList = Functions.showInformation(ls,name);
                            for(Researcher r : researcherList){
                                System.out.println("The name of the Researcher is: " + r.getName() + "\n" +
                                        "The university of the Researcher is: " + r.getUniversity() + "\n" +
                                        "The department of the Researcher is: " + r.getDepartment() + "\n" +
                                        "The interests of the Researcher is: " + r.getInterest());
                            }

                        }
                       break;
                    case "4":
                        System.out.println("Please input the interest: ");
                        String interest = kb.nextLine();
                        System.out.println("The number of Researchers that have interests "+ interest + " is " + Functions.researchersHave(ls,interest));
                        break;
                    case "5":
                        System.out.println("Please input the interest one: ");
                        String interest1 = kb.nextLine();
                        System.out.println("Please input the interest two: ");
                        String interest2 = kb.nextLine();
                        System.out.println("the number of times they occur: " + Functions.numberOfTimes(ls,interest1,interest2));
                        break;
                    case "6":
                        System.out.println("Please input the Researcher's name: ");
                        String researcherName = kb.nextLine();
                        Functions.similarResearchers(ls,researcherName);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    }

