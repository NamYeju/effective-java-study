package chapter2.item1;

/**
 * item1. 정적 팩토리 메소드
 * 클래스의 인스턴스를 얻을 수 있는 두가지 방법 (1) public 생성자 이용 (2)정적 팩토리 메소드 이용
 * */
public class Member {
    private String name;

    public Member(String name){
        this.name = name;
    }

    static Member memberName(String name){
        return new Member(name);
    }

    public static void main(String args[]){
        //(1) public 생성자 이용
        Member test = new Member("yeju");
        System.out.println(test.name);
        //(2) 정적 팩토리 메소드 이용
        Member test1 = test.memberName("yeju");
        System.out.println(test1.name);
    }
}
