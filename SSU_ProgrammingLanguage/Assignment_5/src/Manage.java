import myExceptions.MyExceptions;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Manage implements Runnable {
    int option;
    String number;
    String name;
    String phoneNum;
    String registerDate;
    Order order;

    public Manage(int option, String customerNum) {
        this.option = option;
        this.number = customerNum;
    }

    public Manage(int option, Order order, String number) {
        this.option = option;
        this.number = number;
        this.order = new Order(order.date, order.menu);
    }

    public Manage(int option, String customerNum, String name, String phoneNum, String registerDate) {
        this.option = option;
        this.number = customerNum;
        this.name = name;
        this.phoneNum = phoneNum;
        this.registerDate = registerDate;
    }

    /**
     * option의 종류에 따라 각기 다른 일을 수행한다.
     * option 1: 고객 등록 클릭 시 수행
     * option 2: 고객 검색 클릭 시 수행
     * option 3: 고객 삭제 클릭 시 수행
     * option 4: 주문 클릭 시 수행
     * oprion 5: 주문 취소 클릭 시 수행
     */
    public void run() {
        boolean isDuplicated;
        boolean isSameName;

        // 고객 등록
        if (option == 1) {
            try {
                checkCustomerErrors(number, name, phoneNum, registerDate);
                isDuplicated = hasNumberInList(number);

                if (isDuplicated) {
                    isSameName = hasSameNameInList(name);
                    if (isSameName) {
                        removeCustomer(number);
                        App.customerList.add(new Customer(number, name, phoneNum, registerDate));
                    } else {
                        throw new MyExceptions.Exception6("고객번호가 중복되었습니다.");
                    }
                } else {
                    App.customerList.add(new Customer(number, name, phoneNum, registerDate));
                    System.out.println("고객이 등록되었습니다.");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // 고객 검색
        else if (option == 2) {
            try {
                showCustomerInfo(number);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // 고객 삭제
        else if (option == 3) {
            try {
                removeCustomer(number);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // 주문
        else if (option == 4) {
            try {
                checkOrderErrors(order.date, number, order.menu);
                getCustomer(App.customerList, number).orderCount++;
                getCustomer(App.customerList, number).orderList.add(order);
                System.out.println(getCustomer(App.customerList, number).number+ "번 고객님의 " + order.menu +" 주문이 완료되었습니다.");
                checkFreeCoupon(getCustomer(App.customerList, number));
            } catch (NullPointerException ne) {
                System.out.println("해당 회원이 없습니다.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        // 주문 취소
        else if (option == 5) {
            try {
                checkOrderErrors(order.date, number, order.menu);

                getCustomer(App.customerList, number).removeOrder(order.date, getCustomer(App.customerList, number).number, order.menu);
                if (getCustomer(App.customerList, number).orderCount > 0) {
                    getCustomer(App.customerList, number).orderCount--;
                }
                else {
                    System.out.println("주문 횟수가 0입니다.");
                }
            } catch (NullPointerException ne) {
                System.out.println("해당 회원이 없습니다.");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * customer 인스턴스 생성 시 형식 오류 체크
     * @param number
     * @param name
     * @param phoneNum
     * @param registerDate
     * @throws MyExceptions.Exception1
     * @throws MyExceptions.Exception3
     * @throws MyExceptions.Exception4
     * @throws MyExceptions.Exception6
     */
    public void checkCustomerErrors(String number, String name, String phoneNum, String registerDate)
            throws MyExceptions.Exception1, MyExceptions.Exception3, MyExceptions.Exception4, MyExceptions.Exception6 {
        if (number.isEmpty()) {
            throw new MyExceptions.Exception6();
        }

        if (! number.matches("[0-9]*")) {
            throw new MyExceptions.Exception3();
        }

        if (name.length() > 10) {
            throw new MyExceptions.Exception4();
        } else if (!name.matches("[ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
            throw new MyExceptions.Exception3();
        }

        if (! phoneNum.matches("[0-9|\\-]*")) {
            throw new MyExceptions.Exception1();
        } else if (phoneNum.length() != 13) {
            throw new MyExceptions.Exception1();
        }

        if (! registerDate.matches("[0-9|\\/]*")) {
            throw new MyExceptions.Exception1();
        }

        if (number.isEmpty() || name.isEmpty() || phoneNum.isEmpty() || registerDate.isEmpty()) {
            throw new MyExceptions.Exception6();
        }
    }

    /**
     * Order 인스턴스 생성 시 형식 오류 체크
     * @param date
     * @param number
     * @param menu
     * @throws MyExceptions.Exception3
     * @throws MyExceptions.Exception6
     */
    public void checkOrderErrors(String date, String number, String menu)
            throws  MyExceptions.Exception3, MyExceptions.Exception6 {
        if (! date.matches("[0-9|\\/]*")) {
            throw new MyExceptions.Exception3();
        }

        if (! number.matches("[0-9]*")) {
            throw new MyExceptions.Exception3();
        }

        if (!menu.matches("[ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
            throw new MyExceptions.Exception3();
        }

        if (date.isEmpty() || number.isEmpty()) {
            throw new MyExceptions.Exception6();
        }
    }

    /**
     * Customer List에 해당 번호를 가진 사람이 있는지 확인
     * @param customerNumber
     * @return : 있으면 true / 없으면 false
     */
    public boolean hasNumberInList(String customerNumber) {
        Iterator<Customer> itr = App.customerList.iterator();
        if (itr.hasNext()) {
            while (itr.hasNext()) {
                Customer customer = itr.next();
                if (customerNumber.equals(customer.number)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Customer List에 동일한 이름을 가진 사람이 있는지 확인
     * @param customerName
     * @return : 있으면 true / 없으면 false
     */
    public boolean hasSameNameInList(String customerName) {
        Iterator<Customer> itr = App.customerList.iterator();
        if (itr.hasNext()) {
            while (itr.hasNext()) {
                Customer customer = itr.next();
                if (customerName.equals(customer.name)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 해당 번호의 customer를 Customer List에서 삭제한다.
     * @param number
     * @throws MyExceptions.Exception5
     */
    public void removeCustomer(String number) throws  MyExceptions.Exception5 {
        int i = 0;
        Iterator<Customer> itr = App.customerList.iterator();
        if (itr.hasNext()) {
            while (itr.hasNext()) {
                Customer customer = itr.next();
                if (number.equals(customer.number)) {
                    App.customerList.remove(i);
                    System.out.println("고객이 삭제되었습니다.");
                    break;
                }
                i++;
            }
        } else {
            throw new MyExceptions.Exception5();
        }
    }

    /**
     * 검색 버튼 눌렀을 때 해당 번호를 가진 customer의 정보를 보여준다.
     * @param number
     * @throws MyExceptions.Exception5
     */
    public void showCustomerInfo(String number) throws  MyExceptions.Exception5 {
        String customerInfo;
        Iterator<Customer> itr = App.customerList.iterator();
        if (itr.hasNext()) {
            while (itr.hasNext()) {
                Customer customer = itr.next();
                if (number.equals(customer.number)) {
                    customerInfo = "고객 번호 : " + customer.number
                            + "\n고객명 : " + customer.name
                            + "\n전화번호 : " + customer.phoneNumber
                            + "\n가입일 : " + customer.registerDate;
                    JOptionPane.showMessageDialog(null, customerInfo);

                }
            }
        } else {
            throw new MyExceptions.Exception5();
        }
    }

    /**
     * 번호에 해당하는 customer를 반환한는 함수
     * @param customerList
     * @param number
     * @return
     */
    public Customer getCustomer(ArrayList<Customer> customerList, String number) {
        Iterator<Customer> itr = customerList.iterator();
        while (itr.hasNext()) {
            Customer customer = itr.next();

            if (customer.number.equals(number)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * 3번 주문 시 무료푸폰 발행하는 함수
     * JOptionPane을 사용하여 쿠폰을 발송을 고객에게 알린다.
     * @param customer
     */
    public void checkFreeCoupon(Customer customer) {
        if (customer.orderCount == 3) {
            customer.orderCount = 0;
            App.couponCount++;
            JOptionPane.showMessageDialog(null, customer.number+"번 고객님 "+ "무료쿠폰이 배송되었습니다.");
        }
    }
}
