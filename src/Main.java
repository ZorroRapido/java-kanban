public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task firstTask = new Task("Купить билеты в Мексику", "Описание З-1");
        Task secondTask = new Task("Заказать новое колесо для машины", "Описание З-2");

        Epic firstEpic = new Epic("Переезд", "Нужно завершить до 31 августа");
        Subtask firstSubtask = new Subtask("Собрать коробки", "Описание П-1", firstEpic);
        Subtask secondSubtask = new Subtask("Упаковать кошку", "Описание П-2", firstEpic);

        Epic secondEpic = new Epic("Важный эпик 2", "Описание Э-2");
        Subtask thirdSubtask = new Subtask("Подзадача 3", "Описание П-3", secondEpic);

        manager.createTask(firstTask);
        manager.createTask(secondTask);

        manager.createEpic(firstEpic);
        manager.createSubtask(firstSubtask);
        manager.createSubtask(secondSubtask);

        manager.createEpic(secondEpic);
        manager.createSubtask(thirdSubtask);

        System.out.println("----");
        System.out.println("Вывод различных типов задач");
        System.out.println("Обычные задачи:");
        System.out.println(manager.getTasks());
        System.out.println("Эпики:");
        System.out.println(manager.getEpics());
        System.out.println("Подзадачи:");
        System.out.println(manager.getSubtasks());
        System.out.println("----");

        System.out.println("Изменение статуса обычной задачи firstTask (id=" + firstTask.getId() + ") (NEW -> " +
                "IN_PROGRESS -> DONE)");
        System.out.println(manager.getTaskById(firstTask.getId()));
        firstTask.setStatus("IN_PROGRESS");
        manager.updateTask(firstTask);
        System.out.println(manager.getTaskById(firstTask.getId()));
        firstTask.setStatus("DONE");
        manager.updateTask(firstTask);
        System.out.println(manager.getTaskById(firstTask.getId()));
        System.out.println("----");

        System.out.println("Изменение статуса эпика firstEpic (id=" + firstEpic.getId() + ") (NEW -> IN_PROGRESS -> " +
                "DONE) посредством обновления двух его подзадач");
        System.out.println("Эпик: " + manager.getEpicById(firstEpic.getId()));
        System.out.println("Подзадачи: " + manager.getSubtasksByEpic(firstEpic));
        firstSubtask.setStatus("IN_PROGRESS");
        manager.updateSubtask(firstSubtask);
        System.out.println("Эпик: " + manager.getEpicById(firstEpic.getId()));
        System.out.println("Подзадачи: " + manager.getSubtasksByEpic(firstEpic));
        secondSubtask.setStatus("DONE");
        manager.updateSubtask(secondSubtask);
        System.out.println("Эпик: " + manager.getEpicById(firstEpic.getId()));
        System.out.println("Подзадачи: " + manager.getSubtasksByEpic(firstEpic));
        firstSubtask.setStatus("DONE");
        manager.updateSubtask(firstSubtask);
        System.out.println("Эпик: " + manager.getEpicById(firstEpic.getId()));
        System.out.println("Подзадачи: " + manager.getSubtasksByEpic(firstEpic));
        System.out.println("----");

        System.out.println("Удаление обычной задачи firstTask (id=" + firstTask.getId() + ")");
        System.out.println("До: " + manager.getTasks());
        manager.deleteTask(firstTask.getId());
        System.out.println("После: " + manager.getTasks());
        System.out.println("----");

        System.out.println("Удаление эпика firstEpic (id=" + firstEpic.getId() + ")");
        System.out.println("До:");
        System.out.println(manager.getEpicById(firstEpic.getId()));
        System.out.println(manager.getSubtasksByEpic(firstEpic));
        manager.deleteEpic(firstEpic.getId());
        System.out.println("После:");
        System.out.println(manager.getEpicById(firstEpic.getId()));
        System.out.println(manager.getSubtasksByEpic(firstEpic));
        System.out.println("----");

        System.out.println("Удаление подзадачи thirdSubtask (id=" + thirdSubtask.getId() + ") из эпика secondEpic " +
                        "(id=" + secondEpic.getId() + ")");
        System.out.println("До:");
        System.out.println(manager.getEpicById(secondEpic.getId()));
        System.out.println(manager.getSubtasksByEpic(secondEpic));
        manager.deleteSubtask(thirdSubtask.getId());
        System.out.println("После:");
        System.out.println(manager.getEpicById(secondEpic.getId()));
        System.out.println(manager.getSubtasksByEpic(secondEpic));
        System.out.println("----");
    }
}
