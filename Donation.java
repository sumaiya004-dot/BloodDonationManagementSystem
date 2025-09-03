import java.io.Serializable;

public class Donation implements Serializable {
    private int id;
    private Donor donor;
    private BloodBank bloodBank;
    private String date;

    public Donation(int id, Donor donor, BloodBank bloodBank, String date) {
        this.id = id;
        this.donor = donor;
        this.bloodBank = bloodBank;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public Donor getDonor() {
        return donor;
    }

    public BloodBank getBloodBank() {
        return bloodBank;
    }

    public String getDate() {
        return date;
    }
}
