package com.company;

import org.omg.CORBA.Environment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;
import java.util.function.Function;

public class Main {
    static final int loopCount = 10000000; // 10M

    public static class DemoEntity {
        private String name;
        private Integer age;

        public void setName(String name) {
            this.name = name;
        }

        public void setAge(Integer age) {
            this.age = age;
        }
    }

    public static void testReference(Class<?> clz, Integer age, String name) {
        try {
            Object instance = clz.newInstance();
            Method setName = clz.getMethod("setName", String.class);
            Method setAge = clz.getMethod("setAge", Integer.class);
            setName.invoke(instance, name);
            setAge.invoke(instance, age);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void testMockedSourceGeneration(Integer age, String name) {
        DemoEntity instance = new DemoEntity();;
        instance.setName(name);
        instance.setAge(age);
    }

    public static void run(Consumer fun,String prefix){
        long  startTime =  System.currentTimeMillis();
        fun.accept(null);
        System.out.println(prefix + String.valueOf(System.currentTimeMillis()-startTime) );
    }

    public static void main(String[] args) {
        run(x->{
            for (int i = 0; i < loopCount; i++) {
                testReference(DemoEntity.class, i, "name_"+i);
            }
        },"reference way runs "+String.valueOf(loopCount)+" times costing: ");

        run(x->{
            for (int i = 0; i < loopCount; i++) {
                testMockedSourceGeneration( i, "name_"+i);
            }
        }, "source generator way runs "+String.valueOf(loopCount)+" times costing: ");
    }
}
