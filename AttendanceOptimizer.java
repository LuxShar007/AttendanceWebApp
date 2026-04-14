import java.util.Scanner;

public class AttendanceOptimizer {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==============================================");
        System.out.println("  Attendance & Marks Optimization Engine      ");
        System.out.println("==============================================");
        
        while (true) {
            System.out.print("\nEnter Total Classes Conducted (or 0 to exit): ");
            int total = 0;
            if (scanner.hasNextInt()) {
                total = scanner.nextInt();
            } else {
                System.out.println("Error: Please enter a valid integer.");
                scanner.next(); // consume invalid input
                continue;
            }

            if (total == 0) {
                System.out.println("Exiting the engine. Goodbye!");
                break;
            }
            if (total < 0) {
                System.out.println("Error: Total classes cannot be negative.");
                continue;
            }
            
            System.out.print("Enter Total Classes Attended: ");
            int attended = 0;
            if (scanner.hasNextInt()) {
                attended = scanner.nextInt();
            } else {
                System.out.println("Error: Please enter a valid integer.");
                scanner.next(); // consume invalid input
                continue;
            }
            
            if (attended < 0 || attended > total) {
                System.out.println("Error: Attended classes must be between 0 and Total Classes.");
                continue;
            }
            
            double percentage = calculatePercentage(total, attended);
            int marks = calculateMarks(percentage);
            String zone = determineZone(percentage);
            
            System.out.println("\n--- Analysis Report ---");
            System.out.printf("Current Percentage : %.2f%%\n", percentage);
            System.out.printf("Marks Secured      : %d Marks\n", marks);
            System.out.printf("Current Status     : %s\n", zone);
            System.out.println("-----------------------");
            
            if (zone.equals("Defaulter Zone")) {
                int neededFor75 = calculateRecovery(total, attended, 75.0);
                System.out.printf("Recovery Strategy  : You need to attend %d consecutive classes to reach 75%% and become Safe.\n", neededFor75);
            } else {
                double currentTarget = getCurrentTarget(percentage);
                int safeLeaves = calculateLeaves(total, attended, currentTarget);
                System.out.printf("Safety Buffer      : You can miss %d consecutive upcoming classes without dropping below %d marks (%.0f%%).\n", safeLeaves, marks, currentTarget);
            }
            
            double nextTarget = getNextTarget(percentage);
            if (nextTarget > 0) {
                int targetMarks = calculateMarks(nextTarget);
                int neededForNext = calculateRecovery(total, attended, nextTarget);
                System.out.printf("Next Bracket Goal  : Attend %d more consecutive classes to reach %.0f%% and get %d marks.\n", neededForNext, nextTarget, targetMarks);
            } else {
                System.out.println("Next Bracket Goal  : You are already in the maximum marks bracket!");
            }
        }
        scanner.close();
    }
    
    // Calculates current percentage (P)
    public static double calculatePercentage(int total, int attended) {
        if (total == 0) return 0.0;
        return ((double) attended / total) * 100.0;
    }
    
    // Categorizes mark bracket based on percentage tier
    public static int calculateMarks(double percentage) {
        // We use Math.round or just rely on raw double comparisons. 95.0 <= P <= 100
        if (percentage >= 95.0) return 5;
        if (percentage >= 90.0) return 4;
        if (percentage >= 85.0) return 3;
        if (percentage >= 80.0) return 2;
        if (percentage >= 75.0) return 1;
        return 0; // Defaulter
    }
    
    // Identifies zone 
    public static String determineZone(double percentage) {
        if (percentage < 75.0) return "Defaulter Zone";
        if (percentage < 80.0) return "Risk Zone";
        return "Safe Zone";
    }
    
    // Computes target percentage boundary for the current tier
    public static double getCurrentTarget(double percentage) {
        if (percentage >= 95.0) return 95.0;
        if (percentage >= 90.0) return 90.0;
        if (percentage >= 85.0) return 85.0;
        if (percentage >= 80.0) return 80.0;
        if (percentage >= 75.0) return 75.0;
        return 0.0; // Defaulter no floor target
    }
    
    // Computes target percentage boundary for the next tier
    public static double getNextTarget(double percentage) {
        if (percentage < 75.0) return 75.0;
        if (percentage < 80.0) return 80.0;
        if (percentage < 85.0) return 85.0;
        if (percentage < 90.0) return 90.0;
        if (percentage < 95.0) return 95.0;
        return -1.0; // Max tier has no next target
    }
    
    // Function to calculate safe upcoming leaves (L)
    public static int calculateLeaves(int total, int attended, double target) {
        if (target == 0.0) return 0;
        double l = (100.0 * attended / target) - total;
        return (int) Math.floor(l);
    }
    
    // Function to calculate recovery classes (R)
    public static int calculateRecovery(int total, int attended, double target) {
        // Avoid division by zero if target is somehow 100 (though max target is 95 here)
        if (target >= 100.0) return -1; 
        
        double numerator = (target * total) - (100.0 * attended);
        double denominator = 100.0 - target;
        double r = numerator / denominator;
        
        // If they already meet the target or weird states exist, clamp to 0
        if (r < 0) return 0; 
        return (int) Math.ceil(r);
    }
}
