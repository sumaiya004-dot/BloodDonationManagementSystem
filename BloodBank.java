import java.io.Serializable;

public class BloodBank implements Serializable {
    private int id;
    private String name, location;

    public BloodBank(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String loc) {
        this.location = loc;
    }

    @Override
    public String toString() {
        return name;
    } // Table/combobox shows name (same as old)
}
