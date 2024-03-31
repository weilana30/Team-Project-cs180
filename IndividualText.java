public class IndividualText implements IndividualTextInterface{
    private final User user;
    private final String text;
    private int textNumber;
    public IndividualText (User user, String text, int lineNumber) {
        this.user = user;
        this.text = text;
        this.textNumber = lineNumber;
    }
    public IndividualText(User user, String text) {
        this.user = user;
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public int getTextNumber() {
        return this.textNumber;
    }

    public void setTextNumber(int textNumber) {
        this.textNumber = textNumber;
    }
    //checks if two texts are equal by checking if they have the same message and
    //username of the user
    public boolean equals(Object o) {
        if (o.getClass() != IndividualText.class) {
            return false;
        }
        if (this.user.getUsername().equals(((IndividualText) o).getUser().getUsername()) &&
        this.text.equals(((IndividualText) o).getText()) &&
                (this.textNumber == ((IndividualText) o).getTextNumber())) {
            return true;
        }
        return false;
    }
    public String toString() {
        return user.getUsername() + ":   " + text;
    }
}
