package edu.geekhub.homework.hw2;

import edu.geekhub.homework.hw2.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ToDoListImplTest {

    private ToDoList<Task> toDoList;
    private List<Task> taskList;
    private final SecureRandom random = new SecureRandom();

    @BeforeEach
    void setUp() {
        toDoList = new ToDoListImpl<>();
        taskList = new ArrayList<>();
    }

    @Test
    void toDoList_add_to_start_equals_first_element() {
        Task task = new Task("Test", "Testing add to start method", 1);

        toDoList.addTaskToTheStart(task);

        assertEquals(
                toDoList.getTaskByIndex(0),
                task
        );
    }

    @Test
    void toDoList_add_to_end_equals_last_element() {
        Task task_1 = new Task("Test_2", "Testing add to end method", 1);
        Task task_2 = new Task("Test_2", "Testing getting last element", 2);

        toDoList.addTaskToTheEnd(task_1);
        toDoList.addTaskToTheEnd(task_2);

        assertEquals(
                toDoList.getTaskByIndex(1),
                task_2
        );
    }

    @Test
    void toDoList_get_top_priority_task() {
        Task task_1 = new Task("Test_3", "Testing tasks with priority", 100);
        Task task_2 = new Task("Test_3", "Getting this task as most important", 999);
        Task task_3 = new Task("Test_3", "Not important task", 0);

        toDoList.addTaskToTheEnd(task_1);
        toDoList.addTaskToTheEnd(task_2);
        toDoList.addTaskToTheEnd(task_3);

        assertEquals(
                task_2,
                toDoList.getTopPriorityTask()
        );
    }

    @Test
    void toDoList_get_all_tasks() {
        for (int i = 0; i < 3; i++) {
            Task task = new Task("Task " + i, "Do something", i);

            toDoList.addTaskToTheEnd(task);
            taskList.add(task);
        }

        assertEquals(
                taskList,
                toDoList.getAllTasks()
        );
    }

    @Test
    void toDoList_get_all_sorted_by_priority_tasks() {
        for (int i = 0; i < 3; i++) {
            Task task = new Task("Task " + i, "Do something", random.nextInt(0, 999));

            toDoList.addTaskToTheEnd(task);
            taskList.add(task);
        }

        taskList.sort(Collections.reverseOrder(Comparator.comparingInt(Task::getPriority)));


        assertEquals(
                taskList,
                toDoList.getSortedPriorityTasks()
        );
    }

    @Test
    void toDoList_get_all_sorted_by_alphabetic_tasks() {
        for (int i = 0; i < 3; i++) {
            Task task = new Task("Task " + Character.highSurrogate(random.nextInt(100)), "Do something", random.nextInt(0, 999));

            toDoList.addTaskToTheEnd(task);
            taskList.add(task);
        }

        taskList.sort(
                (t1, t2) ->
                        t1.getName().compareToIgnoreCase(t2.getName())
        );

        assertEquals(
                taskList,
                toDoList.getSortedByAlphabetTasks()
        );
    }

    @Test
    void toDoList_delete_element_by_id() {
        Task task_1 = new Task("Task_1", "", 10);
        Task task_2 = new Task("Task_2", "", 10);
        toDoList.addTaskToTheEnd(task_1);
        toDoList.addTaskToTheEnd(task_2);

        toDoList.deleteTaskByIndex(task_1);

        assertEquals(
                task_2,
                toDoList.getTaskByIndex(0)
        );
    }

    @Test
    void toDoList_delete_element_false_when_no_such_element() {
        Task task_1 = new Task("Task_1", "", 10);
        Task task_2 = new Task("Task_2", "", 10);
        Task task_3 = new Task("Task_3", "", 10);

        toDoList.addTaskToTheEnd(task_1);
        toDoList.addTaskToTheEnd(task_2);

        assertFalse(toDoList.deleteTaskByIndex(task_3));
    }
}