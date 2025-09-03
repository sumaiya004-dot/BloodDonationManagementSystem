import java.io.Serializable;

public class Donor implements Serializable {
    private int id;
    private String name, bloodGroup, gender;
    private int age;

    public Donor(int id, String name, String bloodGroup) { // [kept same as old]
        this(id, name, bloodGroup, 0, "");
    }

    public Donor(int id, String name, String bloodGroup, int age, String gender) {
        this.id = id;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.age = age;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBloodGroup(String bg) {
        this.bloodGroup = bg;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return name;
    } // GUI combo/list display same as before
}
