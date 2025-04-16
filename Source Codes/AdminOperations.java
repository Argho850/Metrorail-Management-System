interface AdminOperations {
void addAdmin(String name, String contact, String adminId, String password);

    void addAdmin(String name, String contact, String adminId);

    void setTrainSchedule(String route, String destination, String time);

    void acceptRechargeRequest(String username, double amount);

    void setTrainSchedule(String route, String destination, String time, double fare);

    void acceptRechargeRequest();

    void viewRegisteredUsers();

    void viewFeedbackAndComplaints();
}