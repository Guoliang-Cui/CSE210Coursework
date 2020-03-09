package entity;

/**
 * Researcher Class
 * instance of Researcher
 */
public class Researcher {
    private String name;
    private String university;
    private String department;
    private String interest;

    public String getInterest() {
        return interest;
    }

    public Researcher(String name, String university, String department, String interest){
        this.name = name;
        this.university = university;
        this.department = department;
        this.interest = interest;

    }

    public String getDepartment() {
        return department;
    }

    public String getUniversity() {

        return university;
    }

    public String getName() {

        return name;
    }




}
