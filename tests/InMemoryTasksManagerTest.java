import jdk.jfr.Description;
import managers.InMemoryTasksManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryTasksManagerTest extends TasksManagerTest<InMemoryTasksManager> {
    public InMemoryTasksManagerTest() {
        super(new InMemoryTasksManager());
    }

    @Override
    @Test
    public void shouldReturnNewStatusForEmptyEpic() {
        super.shouldReturnNewStatusForEmptyEpic();
    }

    @Override
    @Test
    public void shouldReturnNewStatusForEpicWithAllNewSubtasks() {
        super.shouldReturnNewStatusForEpicWithAllNewSubtasks();
    }

    @Override
    @Test
    public void shouldReturnDoneStatusForEpicWithAllDoneSubtasks() {
        super.shouldReturnDoneStatusForEpicWithAllDoneSubtasks();
    }

    @Override
    @Test
    public void shouldReturnInProgressStatusForEpicWithNewAndDoneSubtasks() {
        super.shouldReturnInProgressStatusForEpicWithNewAndDoneSubtasks();
    }

    @Override
    @Test
    public void shouldReturnInProgressStatusForEpicWithAllInProgressTasks() {
        super.shouldReturnInProgressStatusForEpicWithAllInProgressTasks();
    }

    @Override
    @Test
    public void createTaskTest() {
        super.createTaskTest();
    }

    @Override
    @Test
    public void createEpicTest() {
        super.createEpicTest();
    }

    @Override
    @Test
    public void createSubtaskTest() {
        super.createSubtaskTest();
    }

    @Override
    @Test
    public void getTasks() {
        super.getTasks();
    }

    @Override
    @Test
    public void getTasksForEmptyTaskMapTest() {
        super.getTasksForEmptyTaskMapTest();
    }

    @Override
    @Test
    public void getEpicsTest() {
        super.getEpicsTest();
    }

    @Override
    @Test
    public void getEpicsForEmptyEpicMapTest() {
        super.getEpicsForEmptyEpicMapTest();
    }

    @Override
    @Test
    public void getSubtasksTest() {
        super.getSubtasksTest();
    }

    @Override
    @Test
    public void getSubtasksForEmptySubtaskMapTest() {
        super.getSubtasksForEmptySubtaskMapTest();
    }

    @Override
    @Test
    public void deleteAllTasksTest() {
        super.deleteAllTasksTest();
    }

    @Override
    @Test
    public void deleteAllTasksForEmptyTaskMapTest() {
        super.deleteAllTasksForEmptyTaskMapTest();
    }

    @Override
    @Test
    public void deleteAllEpicsTest() {
        super.deleteAllEpicsTest();
    }

    @Override
    @Test
    public void deleteAllEpicsForEmptySubtaskMapTest() {
        super.deleteAllEpicsForEmptySubtaskMapTest();
    }

    @Override
    @Test
    public void deleteAllSubtasksTest() {
        super.deleteAllSubtasksTest();
    }

    @Override
    @Test
    public void deleteAllSubtasksForEmptySubtaskMapTest() {
        super.deleteAllSubtasksForEmptySubtaskMapTest();
    }

    @Override
    @Test
    public void getTaskById() {
        super.getTaskById();
    }

    @Override
    @Test
    public void getTaskByNonexistentId() {
        super.getTaskByNonexistentId();
    }

    @Override
    @Test
    public void getEpicById() {
        super.getEpicById();
    }

    @Override
    @Test
    public void getEpicByNonexistentId() {
        super.getEpicByNonexistentId();
    }

    @Override
    @Test
    public void getSubtaskById() {
        super.getSubtaskById();
    }

    @Override
    @Test
    public void getSubtaskByNonexistentId() {
        super.getSubtaskByNonexistentId();
    }

    @Override
    @Test
    public void updateTask() {
        super.updateTask();
    }

    @Override
    @Test
    public void updateNonexistentTask() {
        super.updateNonexistentTask();
    }

    @Override
    @Test
    public void updateEpic() {
        super.updateEpic();
    }

    @Override
    @Test
    public void updateNonexistentEpic() {
        super.updateNonexistentEpic();
    }

    @Override
    @Test
    public void updateSubtask() {
        super.updateSubtask();
    }

    @Override
    @Test
    public void updateNonexistentSubtask() {
        super.updateNonexistentSubtask();
    }

    @Override
    @Test
    public void deleteTask() {
        super.deleteTask();
    }

    @Override
    @Test
    public void deleteNonexistentTask() {
        super.deleteNonexistentTask();
    }

    @Override
    @Test
    public void deleteEpic() {
        super.deleteEpic();
    }

    @Override
    @Test
    public void deleteNonexistentEpic() {
        super.deleteNonexistentEpic();
    }

    @Override
    @Test
    public void deleteSubtask() {
        super.deleteSubtask();
    }

    @Override
    @Test
    public void deleteNonexistentSubtask() {
        super.deleteNonexistentSubtask();
    }

    @Override
    @Test
    public void createIntersectingTasksFirstTypeIntersection() {
        super.createIntersectingTasksFirstTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingTasksSecondTypeIntersection() {
        super.createIntersectingTasksSecondTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingTasksThirdTypeIntersection() {
        super.createIntersectingTasksThirdTypeIntersection();
    }

    @Override
    @Test
    public void createNonintersectingTasks() {
        super.createNonintersectingTasks();
    }

    @Override
    @Test
    public void createIntersectingSubtasksFirstTypeIntersection() {
        super.createIntersectingSubtasksFirstTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingSubtasksSecondTypeIntersection() {
        super.createIntersectingSubtasksSecondTypeIntersection();
    }

    @Override
    @Test
    public void createIntersectingSubtasksThirdTypeIntersection() {
        super.createIntersectingSubtasksThirdTypeIntersection();
    }

    @Override
    @Test
    public void createNonintersectingSubtasks() {
        super.createNonintersectingSubtasks();
    }

    @Override
    @Test
    public void getHistory() {
        super.getHistory();
    }

    @Override
    @Test
    public void getHistoryForEmptyTaskList() {
        super.getHistoryForEmptyTaskList();
    }

    @Override
    @Test
    public void getHistoryAfterDoubleCalling() {
        super.getHistoryAfterDoubleCalling();
    }

    @Override
    @Test
    public void getHistoryAfterTaskDeleteBeginning() {
        super.getHistoryAfterTaskDeleteBeginning();
    }

    @Override
    @Test
    public void getHistoryAfterTaskDeleteMiddle() {
        super.getHistoryAfterTaskDeleteMiddle();
    }

    @Override
    @Test
    public void getHistoryAfterTaskDeleteEnd() {
        super.getHistoryAfterTaskDeleteEnd();
    }
}
