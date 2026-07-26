import java.util.*;
import java.util.stream.*;

class Employee {
    int id;
    String name;
    String department;
    double salary;

    Employee(int id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String toString() {
        return id + "\t" + name + "\t" + department + "\t" + salary;
    }
}

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<Employee> employees = new ArrayList<>();

        System.out.print("Enter number of employees: ");
        int n = sc.nextInt();

        for (int i = 0; i < n; i++) {
            System.out.println("\nEnter details of Employee " + (i + 1));

            System.out.print("ID: ");
            int id = sc.nextInt();

            System.out.print("Name: ");
            String name = sc.next();

            System.out.print("Department: ");
            String department = sc.next();

            System.out.print("Salary: ");
            double salary = sc.nextDouble();

            employees.add(new Employee(id, name, department, salary));
        }

        System.out.println("\n---- All Employees ----");
        employees.forEach(System.out::println);

        System.out.println("\n---- Salary Above 50000 (High to Low) ----");
        employees.stream()
                .filter(e -> e.salary > 50000)
                .sorted((a, b) -> Double.compare(b.salary, a.salary))
                .forEach(e -> System.out.println(e.name + " -> " + e.salary));

        System.out.println("\n---- Employee Names ----");
        List<String> names = employees.stream()
                .map(e -> e.name)
                .collect(Collectors.toList());
        System.out.println(names);

        System.out.println("\n---- Employees Grouped by Department ----");
        Map<String, List<String>> group = employees.stream()
                .collect(Collectors.groupingBy(
                        e -> e.department,
                        Collectors.mapping(e -> e.name, Collectors.toList())
                ));

        group.forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println("\n---- Average Salary per Department ----");
        Map<String, Double> avg = employees.stream()
                .collect(Collectors.groupingBy(
                        e -> e.department,
                        Collectors.averagingDouble(e -> e.salary)
                ));

        avg.forEach((k, v) -> System.out.println(k + " : " + v));

        double total = employees.stream()
                .map(e -> e.salary)
                .reduce(0.0, Double::sum);

        System.out.println("\nTotal Salary Paid : " + total);

        long count = employees.stream()
                .filter(e -> e.department.equalsIgnoreCase("CSE"))
                .count();

        System.out.println("Number of CSE Employees : " + count);

        Employee highest = employees.stream()
                .max(Comparator.comparingDouble(e -> e.salary))
                .get();

        System.out.println("Highest Paid : " + highest.name + " (" + highest.salary + ")");

        sc.close();
    }
}
 
