## 아이템31. 한정적 와일드 카드를 사용해 API 유연성을 높이라 
<hr>

```java
public class Stack<E>{
    public Stack();
    public void push(E e);
    public E pop();
    public boolean isEmpty(); 
}
```
```java
// (1) 제네릭 메소드 
public void pushAll(Iterable<E> src){
    for(E e: src)
        push(e);
}
```
Stack의 기본적인 API에 pushAll()이라는 메소드를 추가하려고 한다. 
```java
Stack<Number> stack = new Stack();
Iterable<Integer> integers = ...;
stack.pushAll(integers);
```
Number 타입의 스택에 Integer 형태의 인스턴스를 넣으려고 한다. 

논리적으로 따지면 Integer는 Number의 하위타입이기 때문에 오류가 나면 안되지만, 
`매개변수화 타입`은 `불공변`이기 때문에 오류가 날 것이다. (item28 참고)

이러한 문제점을 해결하기 위하여 `한정적 와일드카드 타입`이라는 매개변수화 타입을 지원한다. 

<hr>


````java
//(2) 한정적 타입 매개변수를 적용시킨 메소드
public void pushAll(Iterable<? extends E> src){
        for(E e: src)
        push(e);
        }
````

메소드(1)과 (2)를 비교하면 `Iterable<E>`는 단순하게 `E에 대한 Iterable`를 의미하므로 Number 타입에 대한 Iterable이다. (그러나 구현할 때 Integer타입으로 Iterable을 구현했기 때문에 오류가 났던 것!)   
`Iterable<? extends E>`는 `E의 하위타입의 Iterable` 를 의미하기 때문에 
E 타입에 해당하는 Number의 하위 타입인 Integer로 Iterable을 구현해도 오류가 나지 않는 것이다.

<hr>

마찬가지로 이번에는 popAll()이라는 메소드를 기본 Stack API에 추가해보자.

````java
//(1) 제네릭 메소드
public void popAll(Collection<E> dst){
    while(!isEmpth())
        dst.add(pop());
}
````
```java
Stack<Number> stack = new Stack();
Collection<Object> collection = ...;
stack.pushAll(collection);
```

pushAll()처럼 맞지 않는 타입으로 인해 오류가 날 것이다. 

Collection의 타입은 Object인데 Number 타입의 stack 인스턴스를 Object에 넣어야하니까 오류가 발생한다.  

다만, pushAll()과 다른 점은 매개변수에 넣어줘야 하는 값의 타입(Integer)이 기준 타입(E에 해당하는 Number)보다 하위 타입이였기 때문에 오류가 났던 것이고,
이번에는 매개변수에 들어가야하는 값의 타입(Object)보다 낮은 기준 타입(E에 해당하는 Number)이 들어가려하기 때문에 오류가 나는 것이다. 

````java
//(2) 한정적 타입 매개변수를 적용시킨 메소드
public void popAll(Collection<? super E> dst){
    while(!isEmpth())
        dst.add(pop());
}
````
따라서 `super` 키워드를 이용한 한정적 타입 매개변수를 사용하여 `E의 상위타입의 Collection`을 구현한다. 


<hr>

#### 와일드카드 타입을 사용하는 기본원칙
PECS (팩스) 공식 : Producer-Extends, Consumer-Super

이펙티브 자바에서는 "매개변수화 타입 T가 생산자라면 `<? extends T>`를 사용하고, 
소비자라면 `<? super T>`를 사용하라."라고 나와있다. 

사실 생산자와 소비자라는 단어가 와닿지 않기 때문에, 일단은 " `<? extends T>` 해당 객체가 다운캐스팅 되야 하는 경우에 쓰이고, 
 `<? super T>` 해당 객체가 업캐스팅 되야 하는 경우에 쓰인다."
라고 생각하면 이해하기 쉬울 것 같다. 