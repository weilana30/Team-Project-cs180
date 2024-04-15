public interface ServerInterface {
    
    void main(String[] args);
    
    void run();
    
    void returningUser(String username, BufferedReader br, PrintWriter pw);
    
    void handleFriends(User user, BufferedReader bfr, PrintWriter pw);
    
    void handleProfileSearch(BufferedReader br, PrintWriter pw);
}
