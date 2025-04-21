  import java.util.*;
  import java .io.*;

  class Admin implements AdminOperations {
      private static final String ADMIN_DATA_FILE = "admin_data.txt";
      private static final String TRAIN_SCHEDULE_FILE = "train_schedule.txt";
      private static final String RECHARGE_REQUESTS_FILE = "recharge_requests.txt";
      private static final String USER_DATA_FILE = "user_data.txt";
      private static final String FEEDBACK_FILE = "feedback.txt";
      private static final String COMPLAINTS_FILE = "complaints.txt";

      @Override
      public void addAdmin(String name, String contact, String adminId, String password) {
          try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADMIN_DATA_FILE, true))) {
              writer.write(name + "," + contact + "," + adminId + "," + password + "\n");
              System.out.println("\tAdmin added successfully!");
          } catch (IOException e) {
              System.out.println("\tError adding admin: " + e.getMessage());
          }
      }

      @Override
      public void addAdmin(String name, String contact, String adminId) {

      }

      @Override
      public void setTrainSchedule(String route, String destination, String time) {

      }

      @Override
      public void acceptRechargeRequest(String username, double amount) {

      }

      @Override
      public void setTrainSchedule(String route, String destination, String time, double fare) {
          try (BufferedWriter writer = new BufferedWriter(new FileWriter(TRAIN_SCHEDULE_FILE, true))) {
              writer.write(route + "," + destination + "," + time + "," + fare + "\n");
              System.out.println("\tTrain schedule set successfully!");
          } catch (IOException e) {
              System.out.println("\tError setting train schedule: " + e.getMessage());
          }
      }

      @Override
      public void acceptRechargeRequest() {
          try {
              File file = new File(RECHARGE_REQUESTS_FILE);
              if (!file.exists()) {
                  System.out.println("\tNo recharge requests found.");
                  return;
              }

              BufferedReader reader = new BufferedReader(new FileReader(file));
              String line;
              List<String> requests = new ArrayList<>();

              System.out.println("\tPending Recharge Requests:");
              while ((line = reader.readLine()) != null) {
                  String[] requestData = line.split(",");
                  System.out.println("\tUsername: " + requestData[0] + ", Amount: $" + requestData[1]);
                  requests.add(line);
              }
              reader.close();

              if (requests.isEmpty()) {
                  System.out.println("\tNo recharge requests found.");
                  return;
              }

              Scanner scanner = new Scanner(System.in);
              System.out.println("\tEnter the username to accept the recharge request:");
              String username = scanner.nextLine();

              boolean requestFound = false;
              StringBuilder inputBuffer = new StringBuilder();

              for (String request : requests) {
                  String[] requestData = request.split(",");
                  if (requestData[0].equals(username)) {
                      // Update user balance
                      RapidPassUser user = RapidPassUser.loadUserData(username);
                      if (user != null) {
                          double amount = Double.parseDouble(requestData[1]);
                          user.setBalance(user.getBalance() + amount);
                          user.saveUserData();
                          System.out.println("\tRecharge request accepted. New balance: $" + user.getBalance());
                          requestFound = true;
                      } else {
                          System.out.println("\tUser not found.");
                      }
                  } else {
                      inputBuffer.append(request).append("\n");
                  }
              }

              if (!requestFound) {
                  System.out.println("\tNo recharge request found for the specified user.");
              } else {
                  // Write remaining requests back to the file
                  FileOutputStream fileOut = new FileOutputStream(file);
                  fileOut.write(inputBuffer.toString().getBytes());
                  fileOut.close();
              }
          } catch (IOException e) {
              System.out.println("\tError processing recharge request: " + e.getMessage());
          }
      }

      @Override
      public void viewRegisteredUsers() {
          try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
              String line;
              System.out.println("\tRegistered Users:");
              while ((line = reader.readLine()) != null) {
                  String[] userData = line.split(",");
                  System.out.println("\tUsername: " + userData[0] + ", Balance: $" + userData[2]);
              }
          } catch (IOException e) {
              System.out.println("\tError viewing registered users: " + e.getMessage());
          }
      }

      @Override
      public void viewFeedbackAndComplaints() {
          try (BufferedReader reader = new BufferedReader(new FileReader(FEEDBACK_FILE))) {
              System.out.println("\tFeedback:");
              String line;
              while ((line = reader.readLine()) != null) {
                  String[] feedbackData = line.split(",");
                  System.out.println("\tUsername: " + feedbackData[0] + ", Feedback: " + feedbackData[1]);
              }
          } catch (IOException e) {
              System.out.println("\tError viewing feedback: " + e.getMessage());
          }

          try (BufferedReader reader = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
              System.out.println("\tComplaints:");
              String line;
              while ((line = reader.readLine()) != null) {
                  String[] complaintData = line.split(",");
                  System.out.println("\tUsername: " + complaintData[0] + ", Complaint: " + complaintData[1]);
              }
          } catch (IOException e) {
              System.out.println("\tError viewing complaints: " + e.getMessage());
          }
      }
  }
//All right reserved by Argho...
