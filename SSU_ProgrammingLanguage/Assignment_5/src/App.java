import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.*;
import java.util.Date;


public class App {
    public static ArrayList<Customer> customerList = new ArrayList<>();
    public static int[] countMenuList = new int[5];
    public static int couponCount;

    private JPanel panelMain;
    private JTabbedPane panelTabMain;
    private JPanel customerManagement;
    private JPanel orderManagement;
    private JButton orderCancelButton;
    private JButton orderButton;
    private JButton deleteCustomer;
    private JButton registerCustomer;
    private JButton searchCustomer;
    private JTextField customerPhoneNumber;
    private JTextField customerRegisterDate;
    private JTextField customerNumber;
    private JTextField customerName;
    private JTextField orderCustomerNumber;
    private JTextField orderDate;
    private JComboBox menuSelected;
    private JPanel salesManagement;
    private JButton LookupButton;
    private JButton CancelButton;
    private JTextArea salesInfo;
    private JTextField toDate;
    private JTextField fromDate;

    public App() {
        /**
         * 고객등록 버튼 클릭 시
         */
        registerCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String number = customerNumber.getText();
                String name = customerName.getText();
                String phoneNum = customerPhoneNumber.getText();
                String registerDate = customerRegisterDate.getText();
                writePersonInfo(number, name, phoneNum, registerDate);

                Runnable r = new Manage(1, number, name, phoneNum, registerDate);
                Thread t = new Thread(r);
                t.start();

            }
        });

        /**
         * 고객검색 버튼 클릭 시
         */
        searchCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = customerNumber.getText();

                Runnable r = new Manage(2, number);
                Thread t = new Thread(r);
                t.start();
            }
        });

        /**
         * 고객삭제 버튼 클릭 시
         */
        deleteCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = customerNumber.getText();
                removePersonInfo(number);

                Runnable r = new Manage(3, number);
                Thread t = new Thread(r);
                t.start();
            }
        });

        /**
         * 주문 버튼 클릭 시
         */
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = orderDate.getText();
                String number = orderCustomerNumber.getText();
                String menu = (String)menuSelected.getSelectedItem();
                writeOrderInfo(date, number, menu);

                Runnable r = new Manage(4, new Order(date, menu), number);
                Thread t = new Thread(r);
                t.start();
            }
        });

        /**
         * 주문취소 버튼 클릭 시
         */
        orderCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String date = orderDate.getText();
                String number = orderCustomerNumber.getText();
                String menu = (String)menuSelected.getSelectedItem();
                removeOrderInfo(date, number, menu);

                Runnable r = new Manage(5, new Order(date, menu), number);
                Thread t = new Thread(r);
                t.start();
            }
        });

        /**
         * 매출조회 탭에서 조회 클릭 시
         */
        LookupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    salesInfo.setText("");
                    BufferedReader in = new BufferedReader(new FileReader("./custom.txt"));
                    String result = "";

                    countMenu();

                    result += "메뉴\t갯수\t매출금액\r\n";
                    result += "================================\r\n";
                    result += "김밥\t" + App.countMenuList[0] + "\t" + App.countMenuList[0] * 2000 + "\r\n";
                    result += "떡볶이\t" + App.countMenuList[1] + "\t" + App.countMenuList[1] * 3000 + "\r\n";
                    result += "순대\t" + App.countMenuList[2] + "\t" + App.countMenuList[2] * 3000 + "\r\n";
                    result += "오뎅\t" + App.countMenuList[3] + "\t" + App.countMenuList[3] * 1000 + "\r\n";
                    result += "튀김\t" + App.countMenuList[4] + "\t" + App.countMenuList[4] * 2000 + "\r\n";
                    result += "쿠폰\t" + App.couponCount + "\r\n";
                    result += "================================\r\n";
                    result += "매출합계\t\t" + String.valueOf(App.countMenuList[0] * 2000 + App.countMenuList[1] * 3000
                            + App.countMenuList[2] * 3000 + App.countMenuList[3] * 1000 + App.countMenuList[4] * 2000);

                    salesInfo.setText(result);
                    for (int i = 0; i < App.countMenuList.length; i++)
                        App.countMenuList[i] = 0;

                    in.close();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        });

        /**
         * 매출조회 탭에서 취소 클릭 시
         */
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salesInfo.setText("");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("App");
        frame.setPreferredSize(new Dimension(500,300));
        frame.setContentPane(new App().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * 고객등록 클릭 시 해당 고객을 custom.txt에 기록한다.
     * @param number
     * @param name
     * @param phoneNum
     * @param registerDate
     */
    public static void writePersonInfo(String number, String name, String phoneNum, String registerDate) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./custom.txt",true));
            out.write("INFO: ");
            out.write(number + "\t");
            out.write(name + "\t");
            out.write(phoneNum + "\t");
            out.write(registerDate + "\r\n");

            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured"+ e);
        }
    }

    /**
     * 고객삭제 클릭 시 해당 고객을 custom.txt에서 삭제한다.
     * @param number
     */
    public static void removePersonInfo(String number) {
        try {
            File inFile = new File("./custom.txt");

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader("./custom.txt"));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
                if (line.contains("INFO")) {
                    if (!line.contains(number)) {
                        pw.println(line);
                        pw.flush();
                    }
                }
            }
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");

        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 주문 클릭 시 해당 메뉴를 custom.txt에 기록한다.
     * @param date
     * @param number
     * @param menu
     */
    synchronized public static void writeOrderInfo(String date, String number, String menu) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("./custom.txt",true));
            out.write(date + "\t");
            out.write(menu + "\t");
            out.write(number + "\r\n");

            out.close();
        }
        catch (IOException e) {
            System.out.println("exception occoured"+ e);
        }
    }

    /**
     * 주문취소 클릭 시 해당 주문을 custom.txt에서 삭제한다.
     * @param date
     * @param number
     * @param menu
     */
    synchronized public static void removeOrderInfo(String date, String number, String menu) {
        try {
            File inFile = new File("./custom.txt");

            if (!inFile.isFile()) {
                System.out.println("Parameter is not an existing file");
                return;
            }

            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader("./custom.txt"));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {
                String str = date + "\t" + menu +"\t" + number;
                if (!line.trim().equals(str)) {
                    pw.println(line);
                    pw.flush();
                }

            }
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile))
                System.out.println("Could not rename file");

        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * custom.txt 파일을 읽어 메뉴 개수를 App.countMenuList 배열에 저장하는 함수
     */
    public void countMenu() {
        try {
            BufferedReader in = new BufferedReader(new FileReader("./custom.txt"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            Date dateMin = format.parse(fromDate.getText());
            Date dateMax = format.parse(toDate.getText());

            String s;
            while ((s = in.readLine()) != null) {
                if (s.contains("INFO") == false) {
                    String array[] = s.split("\t");

                    Date myDate = format.parse(array[0]);
                    if (dateMin.compareTo(myDate) <= 0 && dateMax.compareTo(myDate) >= 0) {
                        if (array[1].equals("김밥")) {
                            App.countMenuList[0]++;
                        } else if (array[1].equals("떡볶이")) {
                            App.countMenuList[1]++;
                        } else if (array[1].equals("순대")) {
                            App.countMenuList[2]++;
                        } else if (array[1].equals("오뎅")) {
                            App.countMenuList[3]++;
                        } else if (array[1].equals("튀김")) {
                            App.countMenuList[4]++;
                        }

                    }
                }
            }

            in.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }
}
