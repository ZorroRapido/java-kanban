package managers;

import tasks.Task;
import tasks.Epic;
import tasks.Subtask;

import tasks.Status;
import tasks.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVFormat {
    public static String toString(Task task) {
        String startTime = task.getStartTime() != null ? task.getStartTime().toString() : "null";
        String outputString = String.join(",", Integer.toString(task.getId()), task.getType().toString(),
                task.getName(), task.getStatus().toString(), task.getDescription(), startTime,
                Long.toString(task.getDuration()));

        switch (task.getType()) {
            case TASK:
            case EPIC:
                return outputString;
            case SUBTASK:
                return outputString + "," + ((Subtask) task).getEpic().getId();
            default:
                return null;
        }
    }

    public static Task fromString(String value, FileBackedTasksManager manager) {
        String[] info = value.split(",");

        if (TaskType.TASK.toString().equals(info[1])) {
            Task newTask = new Task(info[2], info[4]);
            newTask.setId(Integer.parseInt(info[0]));
            newTask.setType(TaskType.valueOf(info[1]));
            newTask.setStatus(Status.valueOf(info[3]));
            if ("null".equals(info[5])) {
                newTask.setStartTime(null);
            } else {
                newTask.setStartTime(LocalDateTime.parse(info[5]));
            }
            newTask.setDuration(Long.parseLong(info[6]));
            return newTask;
        } else if (TaskType.EPIC.toString().equals(info[1])) {
            Task newEpic = new Epic(info[2], info[4]);
            newEpic.setId(Integer.parseInt(info[0]));
            newEpic.setType(TaskType.valueOf(info[1]));
            newEpic.setStatus(Status.valueOf(info[3]));
            if ("null".equals(info[5])) {
                newEpic.setStartTime(null);
            } else {
                newEpic.setStartTime(LocalDateTime.parse(info[5]));
            }
            newEpic.setDuration(Long.parseLong(info[6]));
            return newEpic;
        } else if (TaskType.SUBTASK.toString().equals(info[1])) {
            Task newSubtask = new Subtask(info[2], info[4], manager.getEpics().get(Integer.parseInt(info[7])));
            newSubtask.setId(Integer.parseInt(info[0]));
            newSubtask.setType(TaskType.valueOf(info[1]));
            newSubtask.setStatus(Status.valueOf(info[3]));
            if ("null".equals(info[5])) {
                newSubtask.setStartTime(null);
            } else {
                newSubtask.setStartTime(LocalDateTime.parse(info[5]));
            }
            newSubtask.setDuration(Long.parseLong(info[6]));
            return newSubtask;
        }
        return null;
    }

    public static String historyToString(HistoryManager historyManager) {
        List<Task> historyTasks = historyManager.getHistory();
        String[] arrayOfIds = new String[historyTasks.size()];

        int counter = 0;
        for (Task task : historyTasks) {
            arrayOfIds[counter] = Integer.toString(task.getId());
            counter++;
        }
        return String.join(",", arrayOfIds);
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> list = new ArrayList<>();
        try {
            String[] ids = value.split(",");
            for (int i = ids.length - 1; i >= 0; i--) {
                list.add(Integer.parseInt(ids[i]));
            }
        } catch (NullPointerException e) {
            System.out.println("Ошибка при обработке строки с историей! Возможно она пуста!");
        }
        return list;
    }
}
