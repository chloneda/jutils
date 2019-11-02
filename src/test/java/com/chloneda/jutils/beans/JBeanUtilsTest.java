package com.chloneda.jutils.beans;

import org.junit.Test;

/**
 * @Created by chloneda
 * @Description:
 */
public class JBeanUtilsTest {

    @Test
    public void testCopyObject() {
        Penson penson = new Penson();
        penson.setName("zhang3");
        penson.setSex("man");
        penson.setAge(18);
        penson.setAddress("BeiJing");
        penson.setTellphone("13126897583");
        DtoPenson dtoPenson = JBeanUtils.copyObject(DtoPenson.class, penson);
        System.out.println("DtoPenson: {} " + dtoPenson.toString());
    }

    public static class Penson {
        private String name;
        private String sex;
        private int age;
        private String address;
        private String tellphone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTellphone() {
            return tellphone;
        }

        public void setTellphone(String tellphone) {
            this.tellphone = tellphone;
        }

        @Override
        public String toString() {
            return "Penson{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", tellphone='" + tellphone + '\'' +
                    '}';
        }
    }

    public static class DtoPenson {
        private String name;
        private String sex;
        private int age;
        private String address;
        private String weight;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "DtoPenson{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", weight='" + weight + '\'' +
                    '}';
        }
    }
}
