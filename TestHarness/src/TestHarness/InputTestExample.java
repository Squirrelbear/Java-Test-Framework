package TestHarness;

import java.util.Scanner;

public class InputTestExample {
    public static void main(String[] args) {
        System.out.println("Welcome!");
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your age: ");
        int input = scan.nextInt();
        System.out.println("Output age is: " + (input+1));
    }
}
