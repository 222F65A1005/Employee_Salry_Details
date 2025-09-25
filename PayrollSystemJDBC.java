package Employee;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Scanner;

public class PayrollSystemJDBC {
    private static final String URL = "jdbc:postgresql://localhost:5432/payroll_db";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "Poorna"; 

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void addEmployee(Employee emp) {
        String sql = "INSERT INTO employees(name, basic_salary, type, hra, da, hours_worked, rate_per_hour) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, emp.name);
            pstmt.setDouble(2, emp.basicSalary);
            pstmt.setString(3, emp.getType());

            if (emp instanceof FullTimeEmployee) {
                FullTimeEmployee fte = (FullTimeEmployee) emp;
                pstmt.setDouble(4, fte.getHra());
                pstmt.setDouble(5, fte.getDa());
                pstmt.setNull(6, Types.INTEGER);
                pstmt.setNull(7, Types.NUMERIC);
            } else {
                PartTimeEmployee pte = (PartTimeEmployee) emp;
                pstmt.setNull(4, Types.NUMERIC);
                pstmt.setNull(5, Types.NUMERIC);
                pstmt.setInt(6, pte.getHoursWorked());
                pstmt.setDouble(7, pte.getRatePerHour());
            }

            pstmt.executeUpdate();
            System.out.println("✅ Employee added successfully!");

        } catch (SQLException e) {
            System.out.println("Error inserting employee: " + e.getMessage());
        }
    }

    private static void displayEmployees() {
        String sql = "SELECT * FROM employees";

        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n===== Employee Records =====");
            while (rs.next()) {
                int id = rs.getInt("emp_id");
                String name = rs.getString("name");
                double basic = rs.getDouble("basic_salary");
                String type = rs.getString("type");

                if (type.equals("FullTime")) {
                    double hra = rs.getDouble("hra");
                    double da = rs.getDouble("da");
                    FullTimeEmployee fte = new FullTimeEmployee(name, basic, hra, da);
                    System.out.println(id + " | " + name + " | " + type + " | Net Salary: " + fte.calculateSalary());
                } else {
                    int hours = rs.getInt("hours_worked");
                    double rate = rs.getDouble("rate_per_hour");
                    PartTimeEmployee pte = new PartTimeEmployee(name, basic, hours, rate);
                    System.out.println(id + " | " + name + " | " + type + " | Net Salary: " + pte.calculateSalary());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Employee Payroll System (PostgreSQL) =====");
            System.out.println("1. Add Full Time Employee");
            System.out.println("2. Add Part Time Employee");
            System.out.println("3. Display All Employees");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String fname = sc.nextLine();
                    System.out.print("Enter Basic Salary: ");
                    double fbasic = sc.nextDouble();
                    System.out.print("Enter HRA: ");
                    double hra = sc.nextDouble();
                    System.out.print("Enter DA: ");
                    double da = sc.nextDouble();

                    addEmployee(new FullTimeEmployee(fname, fbasic, hra, da));
                    break;

                case 2:
                    sc.nextLine();
                    System.out.print("Enter Name: ");
                    String pname = sc.nextLine();
                    System.out.print("Enter Basic Salary: ");
                    double pbasic = sc.nextDouble();
                    System.out.print("Enter Hours Worked: ");
                    int hours = sc.nextInt();
                    System.out.print("Enter Rate per Hour: ");
                    double rate = sc.nextDouble();

                    addEmployee(new PartTimeEmployee(pname, pbasic, hours, rate));
                    break;

                case 3:
                    displayEmployees();
                    break;

                case 4:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 4);

        sc.close();
    }
}
