package com.chloneda.jutils.commons;

import com.chloneda.jutils.test.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created by chloneda
 * @Description:
 */
public class SerializationUtilsTest {

    private static Person person;
    private static List<Person> personList = new ArrayList<>();

    static {
        person = new Person();
        person.setName("Lili");
        person.setAddress("BeiJing");
        person.setSex("Women");
        person.setAge(18);

        Person person1 = new Person();
        person1.setName("JingJing");
        person1.setAddress("Shanghai");
        person1.setSex("Women");
        person1.setAge(18);

        personList.add(person);
        personList.add(person1);
    }

    @Test
    public void testSerialize() {
        byte[] bytes = SerializationUtils.serialize(person);
        System.out.println("serialize:{} " + new String(bytes));
    }

    @Test
    public void testDeserialize() {
        byte[] bytes = SerializationUtils.serialize(person);
        Person person = SerializationUtils.deserialize(bytes);
        System.out.println("deserialize:{} " + person.toString());
    }

}
