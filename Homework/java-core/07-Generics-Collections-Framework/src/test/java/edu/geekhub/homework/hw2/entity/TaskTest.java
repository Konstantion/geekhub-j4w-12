package edu.geekhub.homework.hw2.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void task_hash_code_work_good() {
        Task task_1 = new Task("Task_1", "", 10);
        Task task_2 = new Task("Task_1", "", 10);
        Task task_3 = new Task("Task_3", "asdf", 10);

        assertEquals(
                task_1.hashCode(),
                task_2.hashCode()
        );
        assertNotEquals(
                task_1.hashCode(),
                task_3.hashCode()
        );
    }

    @Test
    void task_to_string_test() {
        Task task = new Task("Task", "Hello i am task", 1);

        String expectedString = "Task{" +
                                "name='" + "Task" + '\'' +
                                ", description='" + "Hello i am task" + '\'' +
                                ", priority=" + "1" +
                                '}';

        assertEquals(
                expectedString,
                task.toString()
        );
    }

    @Test
    void task_get_description() {
        Task task = new Task("Task","Bla bla", 1);

        String expectedDescription = "Bla bla";

        assertEquals(
                expectedDescription,
                task.getDescription()
        );
    }
}