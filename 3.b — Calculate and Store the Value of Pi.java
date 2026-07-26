//stores the value of pi 
import java.util.Scanner;

interface Pi {
    double calculate(int n);
}

class Calculator implements Pi {

    private double pi = 0;

    public double calculate(int n) {
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0)
                pi += 4.0 / (2 * i + 1);
            else
                pi -= 4.0 / (2 * i + 1);
        }
        return pi;
    }

    public void result() {
        System.out.println("Public Method - Displaying Result:");
        System.out.println("Approximated value of Pi: " + pi);
    }

    protected void info(int n) {
        System.out.println("\nProtected Method - Displaying Precision Info:");
        System.out.println("Precision used: " + n + " terms");
        System.out.println("Series used: Leibniz Series (4/1 - 4/3 + 4/5 - 4/7 + 4/9 ...)");
    }

    private void data() {
        System.out.println("\nPrivate Data - Accessed only within class:");
        System.out.println("Raw computed value (private): " + pi);
    }

    public void show(int n) {
        result();
        info(n);
        data();
    }
}

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Pi Calculator using Access Specifiers ===");
        System.out.print("Enter the number of terms for Pi approximation: ");
        int n = sc.nextInt();

        Calculator c = new Calculator();

        System.out.println("\nCalculating Pi using Leibniz Series...");
        c.calculate(n);
        c.show(n);

        sc.close();
    }
}
 
