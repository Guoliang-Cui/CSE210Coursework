package service;

import entity.Researcher;
import util.ImportExcelUtil;
import java.util.*;
import static util.ImportExcelUtil.getSpecificColumn;

/**
 * call several useful functions for task 3-1 to 3-6
 */
public class Functions {
    /**
     * calculate the number of distinct researchers in the dataset
     * traverse the ls in specific column "USer"
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @return the int number of distinct researchers
     */
    public static int numberOfResearchers(List<Map<String, Object>> ls) {
        String specificColumn = "User";
        List<Object> allObject = getSpecificColumn(ls, specificColumn);
        return allObject.size();
    }

    /**
     * calculate the number of distinct interests in the dataset
     * traverse ls and get "Topics" and "Skills" column, union them
     * remove duplicate
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @return the int number of distinct interests
     */
    public static int numberOfInterests(List<Map<String, Object>> ls) {
        String topic = "Topics";
        List<Object> topicList = getSpecificColumn(ls, topic);
        List<String> allTopicList = ImportExcelUtil.splitByColumn(topicList);
        String skill = "Skills";
        List<Object> skillList = getSpecificColumn(ls, skill);
        List<String> allSkillList = ImportExcelUtil.splitByColumn(skillList);
        allTopicList.addAll(allSkillList);
        //remove " " and ","
        List<String> allTopicList2 = new ArrayList<>();
        for(int i = 0; i< allTopicList.size(); i ++){
            String s = allTopicList.get(i).trim().toLowerCase();
            allTopicList2.add(s);
        }
        //remove duplicate
        for (int i = 0; i < allTopicList2.size() - 1; i++) {
            for (int j = allTopicList2.size() - 1; j > i; j--) {
                if (allTopicList2.get(j).equals(allTopicList2.get(i))) {
                        allTopicList2.remove(j);
                }
            }
        }
        for (int i = 0; i < allTopicList2.size(); i ++){
            if(allTopicList2.get(i).equals("")){
                allTopicList2.remove(i);
            }

        }
        return allTopicList2.size();

    }
    /**
     * show detaied infoemation for a Researcher
     * by using name to match ls
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @param name input name and use name to match
     * @return List(Researcher) contains the Researcher who has the same name
     */
        public static List<Researcher> showInformation(List<Map<String, Object>> ls, String name){
        List<Researcher> researcherList = new ArrayList<>();
            for(int i = 0; i< ls.size(); i++){
                String compareName = (String) ls.get(i).get("User");
                if(compareName.equals(name)){
                    String university = (String) ls.get(i).get("University");
                    String department = (String) ls.get(i).get("Department");
                    String topic = (String) ls.get(i).get("Topics");
                    String skill = (String) ls.get(i).get("Skills");

                        String interest = topic +", " + skill;
                        Researcher researcher = new Researcher(name,university,department,interest);
                        researcherList.add(researcher);
                }
            }

            return researcherList;
        }


    /**
     * calculate the number of researchers who have the input interest.
     * get every Researcher's interest and match the given interest
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @param s input interest
     * @return the number of researchers
     */
        public static int researchersHave(List<Map<String, Object>> ls, String s){
            int count = 0;
            for(int i = 0; i< ls.size(); i ++){
                List<String> interestList = getInterestList(ls,i);
                //remove duplicate
                Set set = new HashSet();
                List<String> newList = new ArrayList<String>();
                for(String cd: interestList){
                    if(set.add(cd)){
                        newList.add(cd);
                    }
                }

                for(String anInterest:newList){
                    if(s.equals(anInterest)){
                        count++;
                    }
                }

            }

            return count;
        }

    /**
     * calculate the number of times given two interests co-occur
     * get every Researcher's interest and judge whether it
     * contains given two interest co-occur
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @param s1 input interest1
     * @param s2 input interest2
     * @return int the number of times given two interests co-occur
     */
        public static int numberOfTimes(List<Map<String, Object>> ls, String s1, String s2){
            int count = 0;
            for(int i = 0; i < ls.size(); i++){
                List<String> interestList = getInterestList(ls,i);
                if(interestList.contains(s1) && interestList.contains(s2)){
                    count++;
                }

            }

        return count;

    }

    /**
     * Recommended the Researcher based on the interests
     * by using cosine similarity algorithm
     * get interest by name and match all other interest
     * calculate cosine similarity and sort
     * output the max value.
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @param researcherName input the researcherName
     */
    public static void similarResearchers(List<Map<String, Object>> ls, String researcherName) {
        List<Double> cosList = new ArrayList<>();
        Scanner kb = new Scanner(System.in);
        String first;
        List<Researcher> researcherList = showInformation(ls, researcherName);
        for (Researcher r : researcherList) {
            System.out.println("{name: " + r.getName() + "," +
                    "university: " + r.getUniversity() + "," +
                    "department: " + r.getDepartment() + "," +
                    "interests: " + r.getInterest() + "}");
        }
        System.out.println("Please select the Researcher");
        int select = kb.nextInt();
        first = researcherList.get(select - 1).getInterest();
        for (int i = 0; i < ls.size(); i++) {
            String topic = (String) ls.get(i).get("Topics");
            String skill = (String) ls.get(i).get("Skills");

            String second = topic + ", " + skill;
            Similarity similarity = new Similarity(first, second);
            Double d = similarity.sim();

            cosList.add(d);

        }
        System.out.println(cosList);
        List<Double> sortCosList = sortList(cosList);


        for (int i = 0; i < cosList.size(); i++) {
            if (cosList.get(i).compareTo(sortCosList.get(cosList.size() - 2)) == 0) {

                List<Researcher> researcherList1 = showInformation(ls, (String) ls.get(i).get("User"));
                for (Researcher r : researcherList1) {
                    System.out.println("The Researcher that has the most similar interests is: " + r.getName()+ ". The cosine similarity is: " + sortCosList.get(sortCosList.size() - 2));
                }
            }
        }
    }

    /**
     * get a researcher's interest by union topic and skill
     * traverse ls and get "Topics" and "Skills" column, union them
     * @param ls the List(Map(String, Object)) by parsing excel file
     * @param i specific Row (specific researcher's)
     * @return List(String) The String list of interests
     */
    public static List<String> getInterestList(List<Map<String, Object>> ls, int i){
        String topic = (String) ls.get(i).get("Topics");
        String skill = (String) ls.get(i).get("Skills");
        String interest = topic +", " + skill;
        List<String> interestList = ImportExcelUtil.splitSByComma(interest);
        return interestList;
    }

    /**
     * sort the double list by using bubble sort
     * @param doubleList input a disordered double list
     * @return a ordered double list
     */
    public static List<Double> sortList(List<Double> doubleList) {
        Double[] doubles = new Double[doubleList.size()];
        doubleList.toArray(doubles);

        Double d;
        for(int i = 0; i< doubles.length - 1; i++){
            for(int j = 0; j < doubles.length - 1; j++){
                if(doubles[j].compareTo(doubles[j+1]) > 0){
                    d = doubles[j];
                    doubles[j] = doubles[j+1];
                    doubles[j+1] = d;
                }
            }
        }
        List<Double> doubleList1 = Arrays.asList(doubles);
        return doubleList1;
    }

}

