import java.util.*;
import java.io.*;

public class MetroRailManagementSystem {
    private static final String MAIN_ADMIN_NAME = "Argho Saha";
    private static final String MAIN_ADMIN_PASSWORD = "Argho2025";

    // Method to display centered text
    public static void displayCentered(String text) {
        int padding = (80 - text.length()) / 2;
        for (int i = 0; i < padding; i++) System.out.print(" ");
        System.out.println(text);
    }

    // Method to display animated title
    public static void displayTitle() {
        System.out.println("\033[1;36m"); // Cyan color
        displayCentered("=====================================================");
        displayCentered("       B A N G L A D E S H   M E T R O R A I L       ");
        displayCentered("=====================================================");
        System.out.println("\033[0m"); // Reset color
        try {
            Thread.sleep(1000); // Animation delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    // Method to display user panel welcome message
    public static void displayUserWelcome() {
        System.out.println("\033[1;35m"); // Magenta color
        displayCentered("=======================================================");
        displayCentered("       W E L C O M E   T O   U S E R   P A N E L       ");
        displayCentered("=======================================================");
        System.out.println("\033[0m"); // Reset color
    }

    // Method to display admin panel welcome message
    public static void displayAdminWelcome() {
        System.out.println("\033[1;34m"); // Blue color
        displayCentered("=========================================================");
        displayCentered("       W E L C O M E   T O   A D M I N   P A N E L       ");
        displayCentered("=========================================================");
        System.out.println("\033[0m"); // Reset color
    }

    // Method to authenticate main admin


    // Method to authenticate main admin
    public static boolean authenticateMainAdmin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\tEnter main admin name: ");
        String name = scanner.nextLine();
        System.out.print("\tEnter main admin password: ");
        String password = scanner.nextLine();

        return name.equals(MAIN_ADMIN_NAME) && password.equals(MAIN_ADMIN_PASSWORD);
    }

    // Method to authenticate worker admin
    public static boolean authenticateWorkerAdmin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\tEnter admin ID: ");
        String adminId = scanner.nextLine();
        System.out.print("\tEnter admin password: ");
        String password = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader("admin_data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] adminData = line.split(",");
                if (adminData[2].equals(adminId) && adminData[3].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("\tError authenticating worker admin: " + e.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Display animated title
        displayTitle();

        while (true) {
            // Main menu
            displayCentered("1. Admin Panel");
            displayCentered("2. User Panel");
            displayCentered("3. Exit");

            int mainChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (mainChoice == 1) {
                // Admin Panel
                if (!authenticateMainAdmin()) {
                    System.out.println("\tInvalid main admin credentials. Access denied.");
                    continue;
                }

                displayAdminWelcome();
                Admin admin = new Admin();

                // Admin menu
                while (true) {
                    displayCentered("1. Add Admin");
                    displayCentered("2. Set Train Schedule");
                    displayCentered("3. Accept Recharge Request");
                    displayCentered("4. View Registered Users");
                    displayCentered("5. View Feedback and Complaints");
                    displayCentered("6. Back to Main Menu");

                    int adminChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (adminChoice) {
                        case 1:
                            if (!authenticateWorkerAdmin()) {
                                System.out.println("\tInvalid worker admin credentials. Access denied.");
                                break;
                            }
                            displayCentered("Enter admin name:");
                            String name = scanner.nextLine();
                            displayCentered("Enter admin contact:");
                            String contact = scanner.nextLine();
                            displayCentered("Enter admin ID:");
                            String adminId = scanner.nextLine();
                            displayCentered("Enter admin password:");
                            String password = scanner.nextLine();
                            admin.addAdmin(name, contact, adminId, password);
                            break;
                        case 2:
                            displayCentered("Enter route:");
                            String route = scanner.nextLine();
                            displayCentered("Enter destination:");
                            String destination = scanner.nextLine();
                            displayCentered("Enter time:");
                            String time = scanner.nextLine();
                            displayCentered("Enter fare:");
                            double fare = scanner.nextDouble();
                            admin.setTrainSchedule(route, destination, time, fare);
                            break;
                        case 3:
                            admin.acceptRechargeRequest();
                            break;
                        case 4:
                            admin.viewRegisteredUsers();
                            break;
                        case 5:
                            admin.viewFeedbackAndComplaints();
                            break;
                        case 6:
                            System.out.println("\tReturning to main menu...");
                            break;
                        default:
                            System.out.println("\tInvalid choice. Please try again.");
                    }

                    if (adminChoice == 6) {
                        break; // Exit admin panel and return to main menu
                    }
                }
            } else if (mainChoice == 2) {
                // User Panel
                displayUserWelcome();
                displayCentered("1. Register");
                displayCentered("2. Log In");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice == 1) {
                    displayCentered("Enter username:");
                    String username = scanner.nextLine();
                    displayCentered("Enter password:");
                    String password = scanner.nextLine();

                    RapidPassUser newUser = new RapidPassUser(username, password);
                    newUser.saveUserData();
                    displayCentered("Rapid Pass Account created successfully!");
                } else if (choice == 2) {
                    displayCentered("Enter username:");
                    String username = scanner.nextLine();
                    displayCentered("Enter password:");
                    String password = scanner.nextLine();

                    RapidPassUser user = RapidPassUser.loadUserData(username);
                    if (user != null) {
                        displayCentered("Login successful!");
                        user.displayUserInfo();

                        // User menu
                        while (true) {
                            displayCentered("1. Buy Ticket");
                            displayCentered("2. Search Train by Route");
                            displayCentered("3. Recharge Balance");
                            displayCentered("4. Check Next Train Time");
                            displayCentered("5. Submit Feedback");
                            displayCentered("6. Submit Complaint");
                            displayCentered("7. Check Balance");
                            displayCentered("8. Back to Main Menu");

                            int userChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline

                            switch (userChoice) {
                                case 1:
                                    displayCentered("Enter source station:");
                                    String source = scanner.nextLine();
                                    displayCentered("Enter destination station:");
                                    String dest = scanner.nextLine();
                                    user.buyTicket(source, dest);
                                    break;
                                case 2:
                                    displayCentered("Enter route:");
                                    String trainRoute = scanner.nextLine();
                                    user.searchTrainByRoute(trainRoute);
                                    break;
                                case 3:
                                    displayCentered("Enter recharge amount:");
                                    double rechargeAmount = scanner.nextDouble();
                                    user.rechargeBalance(rechargeAmount);
                                    break;
                                case 4:
                                    displayCentered("Enter route:");
                                    String nextTrainRoute = scanner.nextLine();
                                    user.checkNextTrainTime(nextTrainRoute);
                                    break;
                                case 5:
                                    displayCentered("Enter your feedback:");
                                    String feedback = scanner.nextLine();
                                    user.submitFeedback(feedback);
                                    break;
                                case 6:
                                    displayCentered("Enter your complaint:");
                                    String complaint = scanner.nextLine();
                                    user.submitComplaint(complaint);
                                    break;
                                case 7:
                                    user.checkBalance();
                                    break;
                                case 8:
                                    System.out.println("\tReturning to main menu...");
                                    break;
                                default:
                                    System.out.println("\tInvalid choice. Please try again.");
                            }

                            if (userChoice == 8) {
                                break; // Exit user panel and return to main menu
                            }
                        }
                    } else {
                        displayCentered("Invalid username or password.");
                    }
                } else {
                    displayCentered("Invalid choice.");
                }
            } else if (mainChoice == 3) {
                System.out.println("\tExiting the system...");
                break; // Exit the program
            } else {
                System.out.println("\tInvalid choice. Please try again.");
            }
        }

        scanner.close();
    }
}

