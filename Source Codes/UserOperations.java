import java.io.*;
import java.util.*;

// Interface for User Operations
interface UserOperations {
    void buyTicket(String source, String destination);
    void searchTrainByRoute(String route);
    void rechargeBalance(double amount);
    void checkNextTrainTime(String route);
    void submitFeedback(String feedback);
    void submitComplaint(String complaint);
    void checkBalance();
}