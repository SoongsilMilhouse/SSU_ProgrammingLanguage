import java.util.ArrayList;
import java.util.Iterator;

class Customer {
    int orderCount;
    String number;
    String name;
    String phoneNumber;
    String registerDate;
    ArrayList<Order> orderList;

    public Customer(String number, String name, String phoneNumber, String registerDate) {
        this.orderCount = 0;
        this.number = number;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.registerDate = registerDate;
        this.orderList = new ArrayList<>();
    }

    /**
     * 주문을 취소(삭제)하는 함수
     * @param date
     * @param customerNumber
     * @param menu
     */
    public void removeOrder(String date, String customerNumber, String menu) {
        int i = 0;

        Iterator<Order> itr = orderList.iterator();
        while (itr.hasNext()) {
            Order order = itr.next();

            if (order.date.equals(date) && order.menu.equals(menu)) {
                orderList.remove(i);
                System.out.println(customerNumber + "번 고객님의 " + menu +" 주문이 취소되었습니다.");
                break;
            }
            i++;
        }
    }

    public int getOrderCount() { return orderCount;}

    public String getNumber() { return number; }

    public String getName() { return name; }

    public String getPhoneNumber() { return phoneNumber; }

    public String getRegisterDate() { return registerDate; }
}
