package songPlayerDemo;

import java.time.LocalDate;

public class ShowLocalDate {  

  public static void main(String[] args) {
    // Store yesterdays current date 
    LocalDate lastDatePlayed = LocalDate.now().minusDays(1);
    LocalDate today = LocalDate.now();
    System.out.println(today.compareTo(lastDatePlayed) + " day ago");
    
    // See if two calendar dates are equal or not
    System.out.println(today.equals(today) + " ? true");
    System.out.println(today.equals(lastDatePlayed) + " ? false");
  }
}