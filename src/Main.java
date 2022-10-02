import managers.Managers;
import managers.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task firstTask = new Task("Купить билеты в Мексику", "Описание 1");
        Task secondTask = new Task("Положить деньги на карту 'Тройка'", "Описание 2");

        Epic firstEpic = new Epic("Переезд", "Нужно завершить до 31 августа");
        Subtask firstSubtask = new Subtask("Собрать коробки", "Описание П-1", firstEpic);
        Subtask secondSubtask = new Subtask("Упаковать кошку", "Описание П-2", firstEpic);
        Subtask thirdSubtask = new Subtask("Выкинуть ненужный хлам", "Описание П-3", firstEpic);

        Epic secondEpic = new Epic("Эпик без подзадач", "Описание эпика без подзадач");

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        manager.createEpic(firstEpic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        manager.createSubtask(thirdSubtask);
        manager.createEpic(secondEpic);

        System.out.println("История до просмотра задач:");
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после 2-х просмотров обычной задачи firstTask (id = " + firstTask.getId()
                + "):");
        manager.getTaskById(firstTask.getId());
        manager.getTaskById(firstTask.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после 3-х просмотров эпика secondEpic (id = " + secondEpic.getId() + "):");
        manager.getEpicById(secondEpic.getId());
        manager.getEpicById(secondEpic.getId());
        manager.getEpicById(secondEpic.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после 4-х просмотров подзадачи firstSubtask (id = " + firstSubtask.getId() +
                "):");
        manager.getSubtaskById(firstSubtask.getId());
        manager.getSubtaskById(firstSubtask.getId());
        manager.getSubtaskById(firstSubtask.getId());
        manager.getSubtaskById(firstSubtask.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после 3-х просмотров эпика firstEpic (id = " + firstEpic.getId() +
                "):");
        manager.getEpicById(firstEpic.getId());
        manager.getEpicById(firstEpic.getId());
        manager.getEpicById(firstEpic.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после 2-х просмотров подзадачи thirdSubtask (id = " + thirdSubtask.getId() +
                "):");
        manager.getSubtaskById(thirdSubtask.getId());
        manager.getSubtaskById(thirdSubtask.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после удаления обычной задачи firstTask (id = " + firstTask.getId() + "):");
        manager.deleteTask(firstTask.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после удаления эпика firstEpic (id = " + firstEpic.getId() + "):");
        manager.deleteEpic(firstEpic.getId());
        System.out.println(manager.getHistory());
    }
}
