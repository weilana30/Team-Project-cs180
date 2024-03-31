public class Viewer implements ViewerInterface {
    private Profile profile;
    public ProfileViewer(Profile profile) {
        this.profile = profile;
    }
    public String displayUserInformation(String username) {
        StringBuilder userInfo = new StringBuilder();
        User user = profile.getUserByUsername(username);
        if (user != null) {
            userInfo.append("Name: ").append(user.getName()).append("\n");
            userInfo.append("Username: ").append(user.getUsername()).append("\n");
            userInfo.append("Email: ").append(user.getEmail()).append("\n");
            userInfo.append("Phone Number: ").append(user.getPhoneNumber()).append("\n");
            userInfo.append("Birthday: ").append(user.getBirthday()).append("\n");
        } else {
            userInfo.append("User not found!");
        }
        return userInfo.toString();
    }
}
