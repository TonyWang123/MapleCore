package org.maple.core.increment.tracetree;

public class Test {
	
	String content = new String();

	public static void main(String[] args){
		Test t1 = new Test();
		Test t2 = new Test();
		t1.content = "111";
		t2.content = "222";
		t1.content = t2.content;
		System.out.println(t1.content);
		t2.content = "333";
		System.out.println(t1.content);
	}
}
