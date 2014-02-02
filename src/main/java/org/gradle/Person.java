package org.gradle;

import org.apache.commons.collections.list.GrowthList;

public class Person {
    private final String name;
    private final String sex = null; 
    private int age = 0;
    

    public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Person(String name) {
        this.name = name;
        new GrowthList();
    }

    public String getName() {
        return name;
    }

	public String getSex() {
		return sex;
	}
}
