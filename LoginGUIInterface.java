public interface LoginGUIInterface {

    // FOR PHASE 2/3

    User[] getUsers();

    void setUsers(User[] users);

    // if they would rather sign in using email or phone number
    String getEmail();

    void setEmail(String email);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);
}
