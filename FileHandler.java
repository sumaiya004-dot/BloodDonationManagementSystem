import java.io.*;

public class FileHandler {
    private static final String FILE_NAME = "blood_data.ser"; // same as original

    public static void saveData(DataManager data) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataManager loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (DataManager) ois.readObject();
        } catch (Exception e) {
            return new DataManager();
        }
    }
}
