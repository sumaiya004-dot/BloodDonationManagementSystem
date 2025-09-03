import java.util.*;
import java.io.Serializable;

public class DataManager implements Serializable {
    private List<Donor> donors = new ArrayList<>();
    private List<BloodBank> bloodBanks = new ArrayList<>();
    private List<Donation> donations = new ArrayList<>();

    public List<Donor> getDonors() {
        return donors;
    }

    public List<BloodBank> getBloodBanks() {
        return bloodBanks;
    }

    public List<Donation> getDonations() {
        return donations;
    }

    public int getNextDonorId() {
        return donors.isEmpty() ? 1 : donors.get(donors.size() - 1).getId() + 1;
    }

    public int getNextBloodBankId() {
        return bloodBanks.isEmpty() ? 1 : bloodBanks.get(bloodBanks.size() - 1).getId() + 1;
    }

    public int getNextDonationId() {
        return donations.isEmpty() ? 1 : donations.get(donations.size() - 1).getId() + 1;
    }

    public void addDonor(Donor d) {
        donors.add(d);
    }

    public void updateDonor(int id, String name, String bg, int age, String gender) {
        for (Donor d : donors)
            if (d.getId() == id) {
                d.setName(name);
                d.setBloodGroup(bg);
                d.setAge(age);
                d.setGender(gender);
                break;
            }
    }

    public void deleteDonor(int id) {
        donors.removeIf(d -> d.getId() == id);
    }

    public void addBloodBank(BloodBank b) {
        bloodBanks.add(b);
    }

    public void updateBloodBank(int id, String name, String loc) {
        for (BloodBank b : bloodBanks)
            if (b.getId() == id) {
                b.setName(name);
                b.setLocation(loc);
                break;
            }
    }

    public void deleteBloodBank(int id) {
        bloodBanks.removeIf(b -> b.getId() == id);
    }

    public void addDonation(Donation d) {
        donations.add(d);
    }

    public void deleteDonationById(int id) {
        donations.removeIf(d -> d.getId() == id);
    }
}
