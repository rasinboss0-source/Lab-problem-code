import java.util.Scanner;

interface Shape {
    double calculateArea();
}

class Circle implements Shape {
    double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    public double calculateArea() {
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    double length, width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public double calculateArea() {
        return length * width;
    }
}

class Triangle implements Shape {
    double base, height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    public double calculateArea() {
        return 0.5 * base * height;
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("----- Shape Area Calculator -----");

        System.out.print("Enter Circle Radius: ");
        Circle c = new Circle(sc.nextDouble());

        System.out.print("Enter Rectangle Length and Width: ");
        Rectangle r = new Rectangle(sc.nextDouble(), sc.nextDouble());

        System.out.print("Enter Triangle Base and Height: ");
        Triangle t = new Triangle(sc.nextDouble(), sc.nextDouble());

        System.out.println("Area of Circle = " + c.calculateArea());
        System.out.println("Area of Rectangle = " + r.calculateArea());
        System.out.println("Area of Triangle = " + t.calculateArea());

        sc.close();
    }
 } 
 
