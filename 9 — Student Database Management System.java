import java.sql.*;
import java.util.Scanner;

public class StudentJDBC {

    // ---- Replace these 4 values with your Supabase project details ----
    // Found in Supabase Dashboard -> Project Settings -> Database -> Connection string
    static final String HOST = "db.tkthrnrlzlzsbsljzzco.supabase.co"; // already filled in for you
    static final String PORT = "5432";
    static final String DB   = "postgres";
    static final String USER = "postgres";
    static final String PASSWORD = "6QJqSD6grDCj8QAh"; // <-- only this one needs editing

    static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Scanner sc = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\n1. Insert Record");
                System.out.println("2. Update Record");
                System.out.println("3. View Student by Roll No");
                System.out.println("4. View All Records");
                System.out.println("5. Exit");
                System.out.print("Enter choice: ");
                choice = sc.nextInt();

                switch (choice) {
                    case 1: insertRecord(con, sc); break;
                    case 2: updateRecord(con, sc); break;
                    case 3: viewOne(con, sc); break;
                    case 4: viewAll(con); break;
                    case 5: System.out.println("Exiting..."); break;
                    default: System.out.println("Invalid choice.");
                }
            } while (choice != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertRecord(Connection con, Scanner sc) throws SQLException {
        System.out.print("Roll No: ");
        int rollno = sc.nextInt();
        sc.nextLine();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Department: ");
        String dept = sc.nextLine();
        System.out.print("Marks: ");
        int marks = sc.nextInt();

        String sql = "INSERT INTO student (rollno, name, department, marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rollno);
            ps.setString(2, name);
            ps.setString(3, dept);
            ps.setInt(4, marks);
            ps.executeUpdate();
            System.out.println("\nRecords Inserted\nSuccessfully.");
        }
    }

    static void updateRecord(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Roll No to update: ");
        int rollno = sc.nextInt();
        System.out.print("New Marks: ");
        int marks = sc.nextInt();

        String sql = "UPDATE student SET marks = ? WHERE rollno = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, marks);
            ps.setInt(2, rollno);
            ps.executeUpdate();
            System.out.println("\nRecord Updated Successfully.");
        }
    }

    static void viewOne(Connection con, Scanner sc) throws SQLException {
        System.out.print("Enter Roll No: ");
        int rollno = sc.nextInt();

        String sql = "SELECT * FROM student WHERE rollno = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rollno);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("\nStudent Details");
                    System.out.println("\nRoll No : " + rs.getInt("rollno"));
                    System.out.println("Name    : " + rs.getString("name"));
                    System.out.println("Department : " + rs.getString("department"));
                    System.out.println("Marks   : " + rs.getInt("marks"));
                } else {
                    System.out.println("No record found.");
                }
            }
        }
    }

    static void viewAll(Connection con) throws SQLException {
        String sql = "SELECT * FROM student ORDER BY rollno";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.println("\nStudent Records");
            System.out.println("-----------------------------------------");
            System.out.printf("%-6s %-10s %-12s %-6s%n", "Roll", "Name", "Department", "Marks");
            System.out.println("-----------------------------------------");

            while (rs.next()) {
                System.out.printf("%-6d %-10s %-12s %-6d%n",
                        rs.getInt("rollno"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("marks"));
            }
        }
    }
}

 
