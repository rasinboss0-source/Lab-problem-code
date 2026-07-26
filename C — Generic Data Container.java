import java.util.*;

public class Main {

    // Generic Box class that can hold any type
    static class Box<T> {
        private T value;

        Box(T value) {
            this.value = value;
        }

        T getValue() {
            return value;
        }

        String getType() {
            return value.getClass().getName();
        }
    }

    // Generic Pair class to hold key-value pairs of any types
    static class Pair<K, V> {
        K key;
        V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key + " = " + value;
        }
    }

    // Generic bounded method to find max of any Comparable type
    static <T extends Comparable<T>> T findMax(T[] arr) {
        T max = arr[0];
        for (T item : arr) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter integer value: ");
        int intVal = Integer.parseInt(sc.nextLine().trim());
        Box<Integer> intBox = new Box<>(intVal);

        System.out.print("Enter string value: ");
        String strVal = sc.nextLine().trim();
        Box<String> strBox = new Box<>(strVal);

        System.out.print("Enter name (key 1): ");
        String name = sc.nextLine().trim();
        System.out.print("Enter marks for " + name + " (value 1): ");
        int marks = Integer.parseInt(sc.nextLine().trim());
        Pair<String, Integer> pair1 = new Pair<>(name, marks);

        System.out.print("Enter roll number (key 2): ");
        int roll = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Enter department for roll " + roll + " (value 2): ");
        String dept = sc.nextLine().trim();
        Pair<Integer, String> pair2 = new Pair<>(roll, dept);

        System.out.print("How many numbers to compare? ");
        int nCount = Integer.parseInt(sc.nextLine().trim());
        Integer[] numbers = new Integer[nCount];
        for (int i = 0; i < nCount; i++) {
            System.out.print("Number " + (i + 1) + ": ");
            numbers[i] = Integer.parseInt(sc.nextLine().trim());
        }
        int maxNumber = findMax(numbers);

        System.out.print("How many names to compare? ");
        int sCount = Integer.parseInt(sc.nextLine().trim());
        String[] words = new String[sCount];
        for (int i = 0; i < sCount; i++) {
            System.out.print("Name " + (i + 1) + ": ");
            words[i] = sc.nextLine().trim();
        }
        String maxWord = findMax(words);

        System.out.print("How many marks to compare? ");
        int dCount = Integer.parseInt(sc.nextLine().trim());
        Double[] marksArr = new Double[dCount];
        for (int i = 0; i < dCount; i++) {
            System.out.print("Marks " + (i + 1) + ": ");
            marksArr[i] = Double.parseDouble(sc.nextLine().trim());
        }
        double maxMarks = findMax(marksArr);

        // ---- Final output block: matches the sample exactly ----
        System.out.println();
        System.out.println("Integer Box Value : " + intBox.getValue());
        System.out.println("Type of stored item : " + intBox.getType());
        System.out.println("String Box Value : " + strBox.getValue());
        System.out.println("Type of stored item : " + strBox.getType());
        System.out.println();
        System.out.println("---- Key-Value Pairs ----");
        System.out.println(pair1);
        System.out.println(pair2);
        System.out.println();
        System.out.println("Maximum Number : " + maxNumber);
        System.out.println("Maximum (Alphabetical) : " + maxWord);
        System.out.println("Maximum Marks : " + maxMarks);

        sc.close();
    }
}
