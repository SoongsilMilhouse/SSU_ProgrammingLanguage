package myExceptions;

public class MyExceptions {
    public static class Exception1 extends Exception {
        public Exception1() {
            super("형식을 벗어났습니다.");
        }
    }
    public static class Exception2 extends Exception {
        public Exception2() {
            super("고객번호를 수정할 수 없습니다.");
        }
    }
    public static class Exception3 extends Exception {
        public Exception3() {
            super("특수문자는 입력할 수 없습니다.");
        }
        public Exception3(String msg) {
            super(msg);
        }
    }
    public static class Exception4 extends Exception {
        public Exception4() {
            super("이름은 10자 이내로 작성해야합니다..");
        }
    }
    public static class Exception5 extends Exception {
        public Exception5() {
            super("검색 회원 또는 주문정보가 없습니다.");
        }
    }
    public static class Exception6 extends Exception {
        public Exception6() {
            super("입력된 내용이 없습니다.");
        }
        public Exception6(String msg) {
            super(msg);
        }
    }
}
