/* DiaryGUI.java created by Liz Smith
assessment part 2 due 13/10/2020
 */

package cm2100.diary;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


public class DiaryGUI extends JFrame implements ActionListener {

    private Container container = new Container();
    private JPanel controlPanel = new JPanel();
    private JComboBox monthList;
    private JPanel buttonPanel = new JPanel();
    private Diary diary = new Diary();
    private JTextArea appointments;
    private JLabel currentDate;


    private static final int startWidth = 900;
    private static final int startHeight = 500;
    private final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public Date selectedDate = new Date(2020, 1, 1);
    private static Dialog d;
    private Appointment appointmentBuffer = new TimedAppointment(null, null, null, null);


    public DiaryGUI() {
        //container
        container = getContentPane();
        container.setLayout(new FlowLayout(FlowLayout.LEFT));

        //window settings
        setTitle("Diary");
        setSize(startWidth, startHeight);
        setResizable(true);

        //menu bar
        JMenu file, about;
        JMenuItem save, open, exit;
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        about = new JMenu("About");
        about.setMnemonic(KeyEvent.VK_A);
        save = new JMenuItem("Save");
        save.addActionListener(this);
        open = new JMenuItem("Open");
        open.addActionListener(this);
        exit = new JMenuItem("Exit");
        exit.addActionListener(this);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        file.add(save);
        file.add(open);
        file.add(exit);
        menuBar.add(about);


        //control panel
        controlPanel.setLayout(new GridLayout(1, 5, 5, 5));
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton newButton = new JButton("new");
        JButton editButton = new JButton("edit");
        JButton deleteButton = new JButton("delete");
        JButton prevButton = new JButton("< year");
        JButton nextButton = new JButton("year >");
        JComboBox monthList = new JComboBox(months);
        currentDate = new JLabel(selectedDate.toString());


        //listeners
        newButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        prevButton.addActionListener(this);
        nextButton.addActionListener(this);
        monthList.addActionListener(this);

        //fill
        controlPanel.add(newButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(prevButton);
        controlPanel.add(nextButton);
        controlPanel.add(monthList);
        controlPanel.add(currentDate);


        //button panel
        buttonPanel.setLayout(new GridLayout(5, 7, 5, 5));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        addButtons();

        //output
        appointments = new JTextArea();
        appointments.setEditable(false);


        //consolidate
        container.add(menuBar);
        container.add(controlPanel);
        container.add(buttonPanel);
        container.add(appointments);


        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        DiaryGUI gui = new DiaryGUI();
        gui.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        //button press events
        if (actionEvent.getSource() instanceof JButton) {
            JButton pressedButton = (JButton) actionEvent.getSource();
            switch (pressedButton.getText()) {
                case "new":
                    newAppointment();
                    break;
                case "edit":
                    //System.out.println("edit");
                    break;
                case "delete":
                    diary.clear();
                    buttonPanel.removeAll();
                    addButtons();
                    buttonPanel.updateUI();
                    break;

                case "< year":
                    selectedDate.setYear(selectedDate.getYear() - 1);
                    buttonPanel.removeAll();
                    addButtons();
                    buttonPanel.updateUI();
                    currentDate.setText(selectedDate.toString());
                    break;

                case "year >":
                    selectedDate.setYear(selectedDate.getYear() + 1);
                    buttonPanel.removeAll();
                    addButtons();
                    buttonPanel.updateUI();
                    currentDate.setText(selectedDate.toString());
                    break;

                default:
                    selectedDate.setDay(Integer.parseInt(pressedButton.getText()));
                    appointmentUpdate();
                    currentDate.setText(selectedDate.toString());
            }
        }

        //combo box events
        else if (actionEvent.getSource() instanceof JComboBox) {

            JComboBox selectedMonth = (JComboBox) actionEvent.getSource();
            selectedDate.setMonth(selectedMonth.getSelectedIndex() + 1);
            buttonPanel.removeAll();
            addButtons();
            buttonPanel.updateUI();
            currentDate.setText(selectedDate.toString());
        }

        //menu item events
        else if (actionEvent.getSource() instanceof JMenuItem) {
            switch (((JMenuItem) actionEvent.getSource()).getText()) {
                case "Save":
                    saveDialogue();
                    break;

                case "Open":
                    openDialogue();
                    break;

                case "Exit":

                    System.exit(0);

            }

        } else if (actionEvent.getSource() instanceof JTextField) {
            System.out.println(((JTextField) actionEvent.getSource()).getText());
        }
    }

    private void addButtons() {
        int[] buttons = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        for (int i = 1; i < (Date.daysInMonth(selectedDate.getYear(), selectedDate.getMonth())) + 1; ++i) {
            JButton button = new JButton(String.valueOf(i));
            Date date = selectedDate;
            date.setDay(i);
            //if there is an appointment today
            if (diary.findAppointments(date).size() != 0) {
                button.setBackground(Color.blue);
            }
            button.addActionListener(this);
            buttonPanel.add(button);
        }
    }

    private void saveDialogue() {
        JFileChooser saveDialogue = new JFileChooser();
        saveDialogue.setDialogTitle("Save as...");
        int accept = saveDialogue.showSaveDialog(saveDialogue);
        if (accept == JFileChooser.APPROVE_OPTION) {
            File savePath = saveDialogue.getSelectedFile();
            diary.save(savePath);
            JOptionPane.showMessageDialog(null, "Saved");
        }
    }

    public void openDialogue() {
        JFileChooser openDialogue = new JFileChooser();
        openDialogue.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = openDialogue.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File loadPath = openDialogue.getSelectedFile();
            diary.load(loadPath);
        }

    }

    public void appointmentUpdate() {
        String display = "";
        ArrayList<Appointment> todaysAppts = diary.findAppointments(selectedDate);
        if (todaysAppts.size() > 0) {
            for (Appointment a : todaysAppts) {
                if (a instanceof TimedAppointment | a instanceof TimedRepeatAppointment)
                    display += "Event: " + a.getDescription() + ", from: " + ((TimedAppointment) a).getStartTime() + " until; " + ((TimedAppointment) a).getEndTime() + '\n';
                else
                    display += "All day event: " + a.getDescription() + '\n';
            }
        } else display = "No Appointments today";
        appointments.setText(null);
        appointments.append(display);
    }

    private void newAppointment() {
        JFrame f = new JFrame();
        d = new JDialog(f, "New Appointment", true);
        d.setLayout(new FlowLayout());
        JRadioButton timed = new JRadioButton("Timed");
        JRadioButton untimed = new JRadioButton("All Day");
        ButtonGroup timedButtons = new ButtonGroup();
        timedButtons.add(timed);
        timedButtons.add(untimed);
        JRadioButton repeat = new JRadioButton("Repeat");
        JRadioButton once = new JRadioButton("Just Once");
        ButtonGroup repeatButtons = new ButtonGroup();
        repeatButtons.add(repeat);
        repeatButtons.add(once);

        JButton next = new JButton("next");
        next.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timed.isSelected()) {
                    d.setVisible(false);
                    timedDialogueFrame();

                    if (repeat.isSelected()) {
                        repeatDialogue();
                    }
                }
                if (untimed.isSelected()) {
                    d.setVisible(false);
                    untimedDialogueFrame();
                }
            }
        });
        d.add(timed);
        d.add(untimed);
        d.add(next);
        d.setSize(300, 300);
        d.setVisible(true);
    }

    public void timedDialogueFrame() {
        JTextField description = new JTextField();
        JTextField date = new JTextField();
        JTextField start = new JTextField();
        JTextField end = new JTextField();
        TimedAppointment newAppt = new TimedAppointment(null, null, null, null);
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Description"), description,
                new JLabel("Date (dd/mm/yyyy"), date,
                new JLabel("Start time (hh/mm)"),
                start,
                new JLabel("End Time (hh/mm)"),
                end,
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, "Add Appointment", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

            int day = Integer.valueOf(date.getText().substring(0, 1));
            int month = Integer.valueOf(date.getText().substring(3, 4));
            int year = Integer.valueOf(date.getText().substring(6, 9));

            Date finalDate = new Date(year, month, day);

            int hoursStart = Integer.valueOf(start.getText().substring(0, 1));
            int minutesStart = Integer.valueOf(start.getText().substring(3));

            Time startTime = new Time(hoursStart, minutesStart);

            int hoursEnd = Integer.valueOf(end.getText().substring(0, 1));
            int minutesEnd = Integer.valueOf(end.getText().substring(3));

            Time endtime = new Time(hoursEnd, minutesEnd);


            System.out.println("entered " +
                    description.getText() + ", " +
                    day + "/" + month + "/" + year + ", " +
                    start.getText() + ", " +
                    end.getText());

            newAppt.setDescription(description.getText());
            newAppt.setDate(finalDate);
            newAppt.setStartTime(startTime);
            newAppt.setEndTime(endtime);

        }
        appointmentBuffer = newAppt;
        repeatDialogue();
    }

    private void untimedDialogueFrame() {
        JTextField description = new JTextField();
        JTextField date = new JTextField();
        JTextField start = new JTextField();
        JTextField end = new JTextField();
        UntimedAppointment newAppt = new UntimedAppointment(null, null);
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Description"), description,
                new JLabel("Date (dd/mm/yyyy"), date,
        };
        int result = JOptionPane.showConfirmDialog(null, inputs, "Add Appointment", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {

            int day = Integer.valueOf(date.getText().substring(0, 1));
            int month = Integer.valueOf(date.getText().substring(3, 4));
            int year = Integer.valueOf(date.getText().substring(6, 9));

            Date finalDate = new Date(year, month, day);

            System.out.println("entered " +
                    description.getText() + ", " +
                    day + "/" + month + "/" + year + ", " +
                    start.getText() + ", " +
                    end.getText());

            newAppt.setDescription(description.getText());
            newAppt.setDate(finalDate);

        }
        appointmentBuffer = newAppt;
        repeatDialogue();

    }

    private void repeatDialogue() {
        /*
        JFrame f = new JFrame();
        d = new JDialog(f, "Repeat?", true);
        d.setLayout(new FlowLayout());
        JRadioButton repeatDaily = new JRadioButton("Repeat Daily");
        JRadioButton repeatWeekly = new JRadioButton("Repeat Weekly");
        JRadioButton repeatAnually = new JRadioButton("Repeat Anually");
        JRadioButton once = new JRadioButton("Just Once");
        JTextField endMonth = new JTextField("month to stop");
        JTextField endYear = new JTextField("year to stop");
        ButtonGroup repeatButtons = new ButtonGroup();
        repeatButtons.add(repeatDaily);
        repeatButtons.add(repeatWeekly);
        repeatButtons.add(repeatAnually);
        repeatButtons.add(once);

        JButton add = new JButton("add");
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (repeatDaily.isSelected()) {
                    Date repeatUntil = new Date(Integer.parseInt(endYear.getText()), Integer.parseInt(endMonth.getText()), appointmentBuffer.getDate().getDay());
                    if (appointmentBuffer instanceof TimedAppointment) {
                        TimedRepeatAppointment tda = new TimedRepeatAppointment(appointmentBuffer.getDescription(), appointmentBuffer.getDate(), repeatUntil, ((TimedAppointment) appointmentBuffer).getStartTime(), ((TimedAppointment) appointmentBuffer).getEndTime(), RepeatType.DAILY);
                        diary.add(tda);

                    } else {
                        UntimedAppointment uda = new UntimedRepeatAppointment(appointmentBuffer.getDescription(), appointmentBuffer.getDate(), repeatUntil, RepeatType.DAILY);
                        diary.add(uda);

                    }
                } else if (repeatWeekly.isSelected()) {
                    Date repeatUntil = new Date(Integer.parseInt(endYear.getText()), Integer.parseInt(endMonth.getText()), appointmentBuffer.getDate().getDay());
                    if (appointmentBuffer instanceof TimedAppointment) {
                        TimedRepeatAppointment twa = new TimedRepeatAppointment(appointmentBuffer.getDescription(), appointmentBuffer.getDate(), repeatUntil, ((TimedAppointment) appointmentBuffer).getStartTime(), ((TimedAppointment) appointmentBuffer).getEndTime(), RepeatType.WEEKLY);
                        diary.add(twa);

                    } else {
                        UntimedAppointment uwa = new UntimedRepeatAppointment(appointmentBuffer.getDescription(), appointmentBuffer.getDate(), repeatUntil, RepeatType.WEEKLY);
                        diary.add(uwa);

                    }

                } else if (repeatAnually.isSelected()) {
                    Date repeatUntil = new Date(Integer.parseInt(endYear.getText()), Integer.parseInt(endMonth.getText()), appointmentBuffer.getDate().getDay());
                    if (appointmentBuffer instanceof TimedAppointment) {
                        TimedRepeatAppointment tya = new TimedRepeatAppointment(appointmentBuffer.getDescription(), appointmentBuffer.getDate(), repeatUntil, ((TimedAppointment) appointmentBuffer).getStartTime(), ((TimedAppointment) appointmentBuffer).getEndTime(), RepeatType.YEARLY);
                        diary.add(tya);

                    } else {
                        UntimedAppointment uya = new UntimedRepeatAppointment(appointmentBuffer.getDescription(), appointmentBuffer.getDate(), repeatUntil, RepeatType.YEARLY);
                        diary.add(uya);

                    }
                } else if (once.isSelected()) {
                    diary.add(appointmentBuffer);

                }
            }
        });
        d.add(repeatAnually);
        d.add(repeatWeekly);
        d.add(repeatDaily);
        d.add(once);
        d.add(endMonth);
        d.add(endYear);
        d.add(add);
        d.setVisible(true);
    */
        System.out.println("for some reason I can't get this to work for the life of me");
    }

}

