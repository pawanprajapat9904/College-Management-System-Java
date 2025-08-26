import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// ==================== Student Class ====================
class Student {
    int rollNo;
    String name;
    int age;
    String course;
    private String password; // Made private for better encapsulation
    String fatherPhone;
    String aadhar;

    public Student(int rollNo, String name, int age, String course, String password, String fatherPhone, String aadhar) {
        this.rollNo = rollNo;
        this.name = name;
        this.age = age;
        this.course = course;
        this.password = password;
        this.fatherPhone = fatherPhone;
        this.aadhar = aadhar;
    }

    // Method to check password
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Method to change password
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void display() {
        System.out.println("  Roll No: " + rollNo);
        System.out.println("  Name: " + name);
        System.out.println("  Age: " + age);
        System.out.println("  Course: " + course);
        System.out.println("  Father's Phone: " + fatherPhone);
        System.out.println("  Aadhar: " + aadhar);
    }
}

// ==================== Teacher Class ====================
class Teacher {
    String username;
    private String password; // Made private
    String phone;
    String aadhar;
    // Using HashMap for efficient student lookup by roll number
    Map<Integer, Student> students = new HashMap<>();

    public Teacher(String username, String password, String phone, String aadhar) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.aadhar = aadhar;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}

// ==================== Admin Class ====================
class Admin {
    String collegeName;
    String username;
    private String password; // Made private
    // Using HashMap for efficient teacher lookup by username
    Map<String, Teacher> teachers = new HashMap<>();

    public Admin(String collegeName, String username, String password) {
        this.collegeName = collegeName;
        this.username = username;
        this.password = password;
    }

    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
}

// ==================== Main Class ====================
public class Main {
    static Scanner sc = new Scanner(System.in);
    // Using HashMap for efficient admin lookup by username
    static Map<String, Admin> admins = new HashMap<>();

    // Helper method for validated string input
    private static String getValidatedInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("❌ Input cannot be empty. Please try again.");
        }
    }

    // ==================== Admin Functions ====================
    public static void createAdmin() {
        String collegeName = getValidatedInput("Enter College Name: ");
        // Prevent duplicate college
        for (Admin a : admins.values()) {
            if (a.collegeName.equalsIgnoreCase(collegeName)) {
                System.out.println("❌ College with this name already exists!\n");
                return;
            }
        }

        String user = getValidatedInput("Create Admin Username: ");
        if (admins.containsKey(user)) {
            System.out.println("❌ This admin username is already taken!\n");
            return;
        }

        String pass = getValidatedInput("Create Admin Password: ");
        Admin newAdmin = new Admin(collegeName, user, pass);
        admins.put(user, newAdmin);
        System.out.println("✅ Admin for " + collegeName + " created successfully!\n");
    }

    public static void adminLogin() {
        String user = getValidatedInput("Enter Admin Username: ");
        String pass = getValidatedInput("Enter Admin Password: ");

        Admin admin = admins.get(user); // Efficient lookup
        if (admin != null && admin.checkPassword(pass)) {
            System.out.println("\n✅ Admin Login Successful! (" + admin.collegeName + ")\n");
            adminMenu(admin);
        } else {
            System.out.println("❌ Invalid Admin Credentials.\n");
        }
    }

    public static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("=== Admin Menu (" + admin.collegeName + ") ===");
            System.out.println("1. Add Teacher");
            System.out.println("2. View Teachers");
            System.out.println("3. Remove Teacher");
            System.out.println("4. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addTeacher(admin);
                case 2 -> viewTeachers(admin);
                case 3 -> removeTeacher(admin);
                case 4 -> { return; }
                default -> System.out.println("❌ Invalid Choice!\n");
            }
        }
    }

    public static void addTeacher(Admin admin) {
        String tUser = getValidatedInput("Enter Teacher Username: ");
        if (admin.teachers.containsKey(tUser)) {
            System.out.println("❌ Teacher with this username already exists!\n");
            return;
        }
        String tPass = getValidatedInput("Enter Teacher Password: ");
        String tPhone = getValidatedInput("Enter Teacher Phone: ");
        String tAadhar = getValidatedInput("Enter Teacher Aadhar: ");

        Teacher newTeacher = new Teacher(tUser, tPass, tPhone, tAadhar);
        admin.teachers.put(tUser, newTeacher);
        System.out.println("✅ Teacher Added Successfully!\n");
    }

    public static void viewTeachers(Admin admin) {
        if (admin.teachers.isEmpty()) {
            System.out.println("❌ No Teachers Available.\n");
            return;
        }
        System.out.println("--- Teacher List for " + admin.collegeName + " ---");
        for (Teacher t : admin.teachers.values()) {
            System.out.println("  Username: " + t.username + " (Phone: " + t.phone + ", Aadhar: " + t.aadhar + ")");
        }
        System.out.println();
    }

    public static void removeTeacher(Admin admin) {
        String tUser = getValidatedInput("Enter Teacher Username to remove: ");
        if (admin.teachers.remove(tUser) != null) { // remove() returns the removed object or null
            System.out.println("✅ Teacher Removed Successfully!\n");
        } else {
            System.out.println("❌ Teacher not found.\n");
        }
    }

    // ==================== Teacher Functions ====================
    public static void teacherLogin() {
        String adminUser = getValidatedInput("Enter Admin (College) Username: ");
        Admin admin = admins.get(adminUser);
        if (admin == null) {
            System.out.println("❌ College Admin not found.\n");
            return;
        }

        String tUser = getValidatedInput("Enter Teacher Username: ");
        String tPass = getValidatedInput("Enter Teacher Password: ");
        Teacher teacher = admin.teachers.get(tUser);

        if (teacher != null && teacher.checkPassword(tPass)) {
            System.out.println("\n✅ Teacher Login Successful!\n");
            teacherMenu(teacher);
        } else {
            System.out.println("❌ Invalid Teacher Credentials.\n");
        }
    }

    public static void teacherMenu(Teacher teacher) {
        while (true) {
            System.out.println("=== Teacher Menu (" + teacher.username + ") ===");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addStudent(teacher);
                case 2 -> viewAllStudents(teacher);
                case 3 -> updateStudent(teacher);
                case 4 -> deleteStudent(teacher);
                case 5 -> { return; }
                default -> System.out.println("❌ Invalid Choice!\n");
            }
        }
    }

    public static void addStudent(Teacher teacher) {
        System.out.print("Enter Roll No: ");
        int roll = Integer.parseInt(sc.nextLine());
        if (teacher.students.containsKey(roll)) {
            System.out.println("❌ Student with this Roll No already exists!\n");
            return;
        }

        String name = getValidatedInput("Enter Name: ");
        System.out.print("Enter Age: ");
        int age = Integer.parseInt(sc.nextLine());
        String course = getValidatedInput("Enter Course: ");
        String pass = getValidatedInput("Set Student Password: ");
        String fatherPhone = getValidatedInput("Enter Father's Phone: ");
        String aadhar = getValidatedInput("Enter Aadhar: ");

        Student newStudent = new Student(roll, name, age, course, pass, fatherPhone, aadhar);
        teacher.students.put(roll, newStudent);
        System.out.println("✅ Student Added Successfully!\n");
    }

    public static void viewAllStudents(Teacher teacher) {
        if (teacher.students.isEmpty()) {
            System.out.println("❌ No Students Available.\n");
            return;
        }
        System.out.println("--- Student List ---");
        for (Student s : teacher.students.values()) {
            s.display();
            System.out.println("--------------------");
        }
    }

    public static void updateStudent(Teacher teacher) {
        System.out.print("Enter Roll No to update: ");
        int roll = Integer.parseInt(sc.nextLine());
        Student student = teacher.students.get(roll);

        if (student != null) {
            student.name = getValidatedInput("Enter New Name (" + student.name + "): ");
            System.out.print("Enter New Age (" + student.age + "): ");
            student.age = Integer.parseInt(sc.nextLine());
            student.course = getValidatedInput("Enter New Course (" + student.course + "): ");
            student.fatherPhone = getValidatedInput("Enter New Father's Phone (" + student.fatherPhone + "): ");
            student.aadhar = getValidatedInput("Enter New Aadhar (" + student.aadhar + "): ");
            System.out.println("✅ Student Updated Successfully!\n");
        } else {
            System.out.println("❌ Student not found.\n");
        }
    }

    public static void deleteStudent(Teacher teacher) {
        System.out.print("Enter Roll No to delete: ");
        int roll = Integer.parseInt(sc.nextLine());
        if (teacher.students.remove(roll) != null) {
            System.out.println("✅ Student Deleted Successfully!\n");
        } else {
            System.out.println("❌ Student not found.\n");
        }
    }

    // ==================== Student Functions ====================
    public static void studentLogin() {
        String adminUser = getValidatedInput("Enter Admin (College) Username: ");
        Admin admin = admins.get(adminUser);
        if (admin == null) {
            System.out.println("❌ College Admin not found.\n");
            return;
        }

        String tUser = getValidatedInput("Enter Teacher Username: ");
        Teacher teacher = admin.teachers.get(tUser);
        if (teacher == null) {
            System.out.println("❌ Teacher not found.\n");
            return;
        }

        System.out.print("Enter Roll No: ");
        int roll = Integer.parseInt(sc.nextLine());
        String pass = getValidatedInput("Enter Password: ");
        Student student = teacher.students.get(roll);

        if (student != null && student.checkPassword(pass)) {
            System.out.println("\n✅ Student Login Successful! Welcome, " + student.name + "\n");
            studentMenu(student);
        } else {
            System.out.println("❌ Invalid Roll No or Password.\n");
        }
    }

    // NEW! Student Menu
    public static void studentMenu(Student student) {
        while (true) {
            System.out.println("=== Student Menu (" + student.name + ") ===");
            System.out.println("1. View My Profile");
            System.out.println("2. Change Password");
            System.out.println("3. Logout");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("\n--- Your Profile ---");
                    student.display();
                    System.out.println();
                    break;
                case 2:
                    String newPass = getValidatedInput("Enter new password: ");
                    student.setPassword(newPass);
                    System.out.println("✅ Password changed successfully!\n");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("❌ Invalid Choice!\n");
            }
        }
    }


    // ==================== Main Method ====================
    public static void main(String[] args) {
        // Pre-populate with some data for easier testing
        Admin testAdmin = new Admin("Jaipur Engineering College", "admin", "admin123");
        Teacher testTeacher = new Teacher("prof_sharma", "sharma123", "9876543210", "111122223333");
        Student testStudent = new Student(101, "Amit Kumar", 20, "Computer Science", "amit123", "9988776655", "444455556666");
        testTeacher.students.put(101, testStudent);
        testAdmin.teachers.put("prof_sharma", testTeacher);
        admins.put("admin", testAdmin);


        while (true) {
            try {
                System.out.println("\n===== School/College Management System =====");
                System.out.println("1. Create Admin (College)");
                System.out.println("2. Admin Login");
                System.out.println("3. Teacher Login");
                System.out.println("4. Student Login");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> createAdmin();
                    case 2 -> adminLogin();
                    case 3 -> teacherLogin();
                    case 4 -> studentLogin();
                    case 5 -> {
                        System.out.println("👋 Exiting... Thank you!");
                        System.exit(0);
                    }
                    default -> System.out.println("❌ Invalid Choice! Try again.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
            }
        }
    }
}