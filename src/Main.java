public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Купить билеты в Мексику", "Описание З-1");
        Epic epic = new Epic("Переезд", "Нужно завершить до 31 августа");
        Subtask subtask = new Subtask("Собрать коробки", "Описание П-1", epic);

        manager.createTask(task);
        manager.createEpic(epic);
        manager.createSubtask(subtask);

        System.out.println("История до просмотра задач:");
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после просмотра обычной задачи:");
        manager.getTaskById(task.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после просмотра эпика:");
        manager.getEpicById(epic.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после просмотра подзадачи:");
        manager.getSubtaskById(subtask.getId());
        System.out.println(manager.getHistory());

        System.out.println("----\nИстория после просмотра 11-ой по счёту задачи:");
        for (int i = 0; i <= 7; i++)
            manager.getEpicById(epic.getId());
        System.out.println(manager.getHistory());
    }
}
