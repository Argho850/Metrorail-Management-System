import java.io.* ;

// Concrete User Class
class RapidPassUser extends User implements UserOperations {
    private static final String USER_DATA_FILE = "user_data.txt";
    private static final String TRAIN_SCHEDULE_FILE = "train_schedule.txt";
    private static final String RECHARGE_REQUESTS_FILE = "recharge_requests.txt";
    private static final String FEEDBACK_FILE = "feedback.txt";
    private static final String COMPLAINTS_FILE = "complaints.txt";

    public RapidPassUser(String username, String password) {
        super(username, password);
    }

    // Overriding abstract method
    @Override
    public void displayUserInfo() {
        System.out.println("\tUsername: " + getUsername());
        System.out.println("\tBalance: $" + getBalance());
    }

    // Implementing UserOperations interface methods
    @Override
    public void buyTicket(String source, String destination) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_SCHEDULE_FILE))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] trainData = line.split(",");
                if (trainData[0].equals(source) && trainData[1].equals(destination)) {
                    double fare = Double.parseDouble(trainData[3]);
                    double totalFare = fare + (fare * 0.05); // Adding 5% VAT
                    if (getBalance() >= totalFare) {
                        setBalance(getBalance() - totalFare);
                        System.out.println("\tTicket purchased from " + source + " to " + destination);
                        System.out.println("\tFare: $" + fare + ", VAT (5%): $" + (fare * 0.05) + ", Total: $" + totalFare);
                        System.out.println("\tRemaining Balance: $" + getBalance());
                        saveUserData();
                        found = true;
                        break;
                    } else {
                        System.out.println("\tInsufficient balance to buy a ticket.");
                        found = true;
                        break;
                    }
                }
            }
            if (!found) {
                System.out.println("\tNo train found for the specified route.");
            }
        } catch (IOException e) {
            System.out.println("\tError searching for train: " + e.getMessage());
        }
    }

    @Override
    public void searchTrainByRoute(String route) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TRAIN_SCHEDULE_FILE))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] trainData = line.split(",");
                if (trainData[0].equals(route)) {
                    System.out.println("\tTrain found: Route: " + trainData[0] + ", Destination: " + trainData[1] + ", Time: " + trainData[2] + ", Fare: $" + trainData[3]);
                    found = true;
                }
            }
            if (!found) {
                System.out.println("\tNo train found for the specified route.");
            }
        } catch (IOException e) {
            System.out.println("\tError searching for train: " + e.getMessage());
        }
    }

    @Override
    public void rechargeBalance(double amount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECHARGE_REQUESTS_FILE, true))) {
            writer.write(getUsername() + "," + amount + "\n");
            System.out.println("\tRecharge request submitted. Waiting for admin approval.");
        } catch (IOException e) {
            System.out.println("\tError submitting recharge request: " + e.getMessage());
        }
    }

    @Override
    public void checkNextTrainTime(String route) {
        searchTrainByRoute(route); // Reuse the searchTrainByRoute method
    }

    @Override
    public void submitFeedback(String feedback) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FEEDBACK_FILE, true))) {
            writer.write(getUsername() + "," + feedback + "\n");
            System.out.println("\tThank you for your feedback!");
        } catch (IOException e) {
            System.out.println("\tError submitting feedback: " + e.getMessage());
        }
    }

    @Override
    public void submitComplaint(String complaint) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COMPLAINTS_FILE, true))) {
            writer.write(getUsername() + "," + complaint + "\n");
            System.out.println("\tYour complaint has been recorded.");
        } catch (IOException e) {
            System.out.println("\tError submitting complaint: " + e.getMessage());
        }
    }

    @Override
    public void checkBalance() {
        System.out.println("\tYour current balance is: $" + getBalance());
    }

    // Save user data to file
    void saveUserData() {
        try {
            File file = new File(USER_DATA_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder inputBuffer = new StringBuilder();
            String line;
            boolean userExists = false;

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(getUsername())) {
                    line = getUsername() + "," + getPassword() + "," + getBalance();
                    userExists = true;
                }
                inputBuffer.append(line).append("\n");
            }
            reader.close();

            if (!userExists) {
                inputBuffer.append(getUsername()).append(",").append(getPassword()).append(",").append(getBalance()).append("\n");
            }

            FileOutputStream fileOut = new FileOutputStream(file);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        } catch (IOException e) {
            System.out.println("\tError saving user data: " + e.getMessage());
        }
    }

    // Load user data from file
    public static RapidPassUser loadUserData(String username) {
        try {
            File file = new File(USER_DATA_FILE);
            if (!file.exists()) {
                System.out.println("\tNo users registered yet. Please register first.");
                return null;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData[0].equals(username)) {
                    RapidPassUser user = new RapidPassUser(username, userData[1]);
                    user.setBalance(Double.parseDouble(userData[2]));
                    reader.close();
                    return user;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("\tError loading user data: " + e.getMessage());
        }
        return null;
    }
}
