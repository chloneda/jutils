package com.chloneda.jutils.commons;

import com.chloneda.jutils.test.Person;
import org.junit.Test;

/**
 * @Created by chloneda
 * @Description:
 */
public class SerializationUtilsTest {

    private static Person person;

    static {
        person = new Person();
        person.setName("Lili");
        person.setAddress("BeiJing");
        person.setSex("Women");
        person.setAge(18);
    }

    @Test
    public void testSerialize() {
        byte[] bytes = SerializationUtils.serialize(person);
        System.out.println(new String(bytes));
    }

    @Test
    public void testDeserialize() {
        byte[] bytes = SerializationUtils.serialize(person);
        Person person = (Person) SerializationUtils.deserialize(bytes);
        System.out.println(person.toString());
    }

}
