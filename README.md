# 🩸 Blood Donation Management System

A Java Swing desktop application to efficiently manage donors, blood banks, and donations with a **user-friendly, dark-themed interface**, robust validation, and persistent local storage.

---

## 🔹 Features
- **Donors & Blood Banks:** Add, Update, Delete with duplicate prevention  
- **Donations:** Filter donors by blood group, select bank, date validation (`dd/mm/yyyy`)  
- **UX Enhancements:** Tabbed interface with active tab highlighted in red, styled tables and buttons  
- **Validation & Feedback:** Inline warnings, required field checks, duplicate checks, auto-slash in date field  

---

## 🧰 Tech Stack
- **Language:** Java (Swing, AWT)  
- **Persistence:** Local files via `FileHandler`  
- **JDK:** 17+  

---

## 🏗️ Architecture
- `BloodDonationSystem.java`: Main UI & Controller  
- `DataManager.java`: CRUD operations, ID generation  
- `FileHandler.java`: Load/Save data  
- **Models:** Donor, BloodBank, Donation  

---

## 🖼️ Screenshots
- **Home Page:**  
  ![Home Page](images/home_tab.png)

- **Donors Tab:**  
  ![Donor Page](images/donor_tab.png)

- **Blood Banks Tab:**  
  ![BloodBank Page](images/bloodBank_tab.png)

- **Donations Tab (filter & date validation):**  
  ![Donation Page](images/donation_tab.png)

---

## 🧠 Design Highlights
- **OOP Principles:** Encapsulation, Separation of Concerns, Reusability  
- **UX:** Dark theme, zebra-striped tables, styled buttons with hover/click effects, placeholder helpers  
- **Validation:** Required fields, duplicates, date format enforcement  
- **Defensive Programming:** Prevents invalid or duplicate entries  

---

## ✅ Quick Test
1. Add, Update, Delete Donors → duplicate prevention works
2. Add, Update, Delete Blood Banks → duplicate prevention works
3. Add, Update, Delete Donations → blood group filter, date validation, and bank selection work
4. UI elements respond correctly: tabs, tables, buttons, warnings

---

## 📦 Run Instructions

### Option 1: Using VSCode
1. Open the project in VSCode.
2. Navigate to `BloodDonationSystem.java`.
3. Click **Run** or **Run Without Debugging** (green play button).
   
### Option 2: Using Terminal
```bash
# Compile all Java files
javac -d out src/*.java

# Run the application
java -cp out BloodDonationSystem
