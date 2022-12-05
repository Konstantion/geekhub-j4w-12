package edu.geekhub.homework.hw2;

import edu.geekhub.homework.hw2.entity.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToDoListImpl<E extends Task> implements ToDoList<E> {
    private List<E> tasksStorage = new ArrayList<>();

    @Override
    public E getTopPriorityTask() {
        return getSortedPriorityTasks().get(0);
    }

    @Override
    public E getTaskByIndex(int index) {
        return tasksStorage.get(index);
    }

    @Override
    public List<E> getAllTasks() {
        return tasksStorage;
    }

    @Override
    public List<E> getSortedPriorityTasks() {
        List<E> priorityTasks = new ArrayList<>(tasksStorage);

        priorityTasks.sort(Collections.reverseOrder(new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return  Integer.compare(
                        o1.getPriority(),
                        o2.getPriority()
                );
            }
        }));
        return priorityTasks;
    }

    @Override
    public List<E> getSortedByAlphabetTasks() {
        List<E> priorityTasks = new ArrayList<>(tasksStorage);

        priorityTasks.sort(new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return o1.getName()
                        .compareToIgnoreCase(
                                o2.getName()
                        );
            }
        });
        return priorityTasks;
    }

    @Override
    public boolean addTaskToTheEnd(E task) {
        return tasksStorage.add(task);
    }

    @Override
    public boolean addTaskToTheStart(E task) {
        tasksStorage.add(0, task);
        return true;
    }

    @Override
    public boolean deleteTaskByIndex(E task) {
        int index = tasksStorage.indexOf(task);
        try {
            tasksStorage.remove(index);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}
