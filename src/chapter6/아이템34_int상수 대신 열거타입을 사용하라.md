## 아이템34. int 상수 대신 열거 타입을 사용하라

#### 정수 열거 패턴 (static final을 이용하여 정의한 상수)
- 타입의 안정성이 보장되지 않는다. 
    ```java
    public static final int BANANA = 1;   
    public static final int APPLE = 2;
    
    ```
    
    예를 들어 사과를 매개변수로 필요로 하는 메소드가 있다고 할 때, 매개변수에 사과가 아닌 바나나가
    들어가도 오류가 나지 않는다. 
- 문자열 출력 시 값이 명확하게 나타나지 않음
    ```java
    System.out.println(classname.BANANA); // 바나나의 의미가 아닌 단순 숫자값인 1 출력 
    System.out.println(FRUIT.BANANA); // enum 타입은 BANANA 출력
    ```
#### 열거타입 (enum type
)
자바 클래스의 일종인 열거 타입 `enum`은 일정 개수의 `상수 값을 정의`하고, 그 외의 값은 허용하지 않는다.   
열거 타입 자체는 클래스이며, 상수 하나당 자신의 인스턴스를 하나씩 만들어 public static final 필드로 공개한다.

```java
public enum Fruit {
    APPLE, BANANA, MANGO;
}
```

```java
// 내부적으로 위의 enum 클래스가 아래처럼 바뀐다.
class Color
{
     public static final Fruit APPLE = new Fruit();
     public static final Fruit BANANA = new Fruit();
     public static final Fruit MANGO = new Fruit();
}
```

열거 타입은 밖에서 접근할 수 있는 생성자를 제공하지 않으므로 사실상 final이다. 

따라서 클라이언트가 인스턴스를 직접 생성하거나 확장할 수 없으니 열거 타입 선언으로 만들어진 인스턴스들은
딱 하나씩만 존재함이 보장된다. (`싱글톤`의 형태)

#### enum 특징
- 컴파일타임 타입 안전성 제공   
enum타입을 매개변수로 받는 메소드는 enum 타입 중 하나의 값을 참조하는 것이 확실하기 때문에 
(다른 타입의 값을 받으면 컴파일 오류 발생 ) 안전성을 제공한다. 

- 임의의 메서드나 필드를 추가할 수 있고 인터페이스 구현 가능    
열거타입은 가장 단순하게는 그저 상수 모음일 뿐이지만, 실제로는 클래스이므로 추상개념 표현이 가능하다. 

    ```java
    public enum Fruit {
        APPLE(1000, 34), // 생성자에 넘겨지는 매개변수 
        BANANA(1200, 5),
        MANGO(3400, 18);
    
        private final int price;
        private final int totalNum;
    
        Fruit(int totalNum, int price) {
            this.price = price;
            this.totalNum = totalNum;
        }
    
    
        @Override
        public String toString() {
            return name() + " ===> 가격은 : " + totalNum + "원 입니다. 남은 수량은: " + price + "개 입니다.";
        }
    }
    ```
    - 열거 타입 상수 각각을 특정 데이터와 연결 지으려면 생성자에서 데이터를 받아 인스턴스 필드에 저장하면 된다. 
    - 열거 타입은 근본적으로 불변이라 모든 필드는 `final`이여야 한다.
    - 필드를 public 보다는 `private`으로 두고 별도의 `public 접근자 메서드` 생성 
  
- 상수별 메소드 구현 
    ```java
    public enum Operation {
        PLUS, MINUS, TIMES, DIVIDE;
        
        // case가 추가될 때마다 수정해야함
        public double apply(double x, double y){
            switch(this){
                case PLUS: return x + y;
                case MINUS: return x - y;
                case TIMES: return x * y;
                case DIVIDE: return x / y;
            }
            throw new AssertionError("알 수 없는 타입");
        }
    }
    ```
  switch를 이용한 분기 방법은 새로운 상수 추가시 case도 추가해야한다.   
  열거 타입에 추상메소드를 선언하고 각 상수에서 재정의 해보자. (`상수별 메서드 구현`)
    ```java
    public enum Operation {
        // 상수에서 추상메소드 재정의
        PLUS("+"){public double apply(double x, double y) {return x + y;} },
        MINUS("-"){public double apply(double x, double y) {return x - y;} },
        TIMES("*"){public double apply(double x, double y) {return x * y;} },
        DIVIDE("/"){public double apply(double x, double y) {return x / y;} };
    
        // 추상메소드를 열거타입에 추가
        public abstract double apply(double x, double y);
    
        private final String symbol;
        Operation(String symbol){
            this.symbol = symbol;
        }
    
        @Override
        public String toString(){
            return symbol;
        }
    
        public static void main(String[] args) {
            double x = 3.000;
            double y = 4.000;
    
            for(Operation op : Operation.values()){
                System.out.print(x);
                System.out.print(op);
                System.out.print(y);
                System.out.print("=" + op.apply(x,y)+ "\n");
    
            }
        }
    }
    ``` 

#### +) enum 메소드 
- values() : 정의된 상수들의 값을 배열에 담아 반환 

```java
// ex
for (Fruit f : Fruit.values())
    System.out.println(f);
```


> 결론 : 필요한 원소를 컴파일타임에 다 알 수 있는 상수 집합이라면 열거 타입 사용 
