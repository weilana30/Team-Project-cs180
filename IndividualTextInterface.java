/**
 *IndividualTextInterface
 * <p>
 * An interface for the IndividualText class
 *
 * @author Andrew Weiland, lab section 15
 * @version March 31, 2024
 */
public interface IndividualTextInterface {
    User getUser();
    String getText();
    int getTextNumber();
    void setTextNumber(int textNumber);
    boolean equals(Object o);
    String toString();
}
