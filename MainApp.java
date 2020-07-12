package cm2100.diary;

import java.util.ArrayList;
import java.io.*;

/**
 * CM2100 Part 1 testing main class.
 * 
 * @author K. Hui
 */
public class MainApp {
    public static void main(String[] args)
    {
        //Test for Task 1
        System.out.println(new Time(9,5).toString());   //should output 09:05
        System.out.println(new Time(19,55).toString()); //should output 19:55
        
        Time t1=new Time(12,0);
        Time t2=new Time(12,0);
        Time t3=new Time(1,10);
        System.out.println(t1.equals(t2));  //should output true
        System.out.println(t2.equals(t3));  //should output false

        System.out.println(t1.compareTo(t2));   //should output 0
        System.out.println(t2.compareTo(t3));   //should output a positive int
        System.out.println(t3.compareTo(t1));   //should output a negative int

        //Test for Task 2
        Date d1=new Date(2019,12,5);
        Date d2=new Date(2019,12,5);
        Date d3=new Date(2020,1,1);
        System.out.println(d1.toString());  //should output "05-Dec-2019"
        System.out.println(d3.toString());  //should output "01-Jan-2020"
        System.out.println(d1.equals(d2));  //should output true
        System.out.println(d2.equals(d3));  //should output false
        
        System.out.println(Date.daysInMonth(2019,2));   //should output 28
        System.out.println(Date.daysInMonth(2020,2));   //should output 29
        System.out.println(Date.daysInMonth(2019,12));  //should output 31
        
        //Test for Task 4
        Appointment uta1=new UntimedAppointment("Christmas",new Date(2019,12,25));
        Appointment uta2=new UntimedAppointment("My birthday",new Date(2019,12,25));
        System.out.println(uta1.occursOn(new Date(2019,12,25)));  //should output true
        System.out.println(uta1.occursOn(new Date(2020,1,1)));    //should output false
        System.out.println(uta1.clash(uta2));                       //should output true
        
        //Test for Task 5
        Appointment ta1=new TimedAppointment("Christmas party",new Date(2019,12,25),new Time(19,0),new Time(23,0));
        Appointment ta2=new TimedAppointment("Birthday party",new Date(2019,12,25),new Time(12,0),new Time(13,30));
        Appointment ta3=new TimedAppointment("Watch TV program",new Date(2019,12,25),new Time(19,30),new Time(20,0));
 
        System.out.println(ta1.clash(ta2)); //false
        System.out.println(ta1.clash(ta3)); //true
        System.out.println(ta1.clash(uta1)); //true
        
        //Test for Task 6
        Diary diary=new Diary();
        diary.add(uta1);
        diary.add(uta2);
        diary.add(ta1);
        diary.add(ta2);
        diary.add(ta3);
        
        System.out.println(diary.toString());

        Date date=new Date(2019,12,25);
        System.out.println("===Appointment(s) occur on "+date.toString()+"\n");
        ArrayList<Appointment> christmasEvents=diary.findAppointments(date);
        for (Appointment a:christmasEvents)
            System.out.println(a);

        String keyword="Party";
        System.out.println("===Appointment(s) with keyword ["+keyword+"]\n");
        ArrayList<Appointment> partyEvents=diary.findAppointments(keyword);
        for (Appointment a:partyEvents)
            System.out.println(a);

        //Test for Task 7 & 8
        diary.sortByDescription();
        System.out.println("===Sort by description ===\n"+diary.toString());
        
        //Test for Task 9 & 10
        diary.sortByDateTime();
        System.out.println("===Sort by date-time===\n"+diary.toString());
        
        //Test for Task 11
        diary.save(new File("sample.csv"));
    } //end method
} //end class
