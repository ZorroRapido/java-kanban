import managers.HTTPTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.HttpTaskServer;
import server.KVServer;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPTaskManagerTest extends TasksManagerTest<HTTPTaskManager> {
    protected KVServer kvServer;
    protected HttpTaskServer httpTaskServer;

    protected HTTPTaskManagerTest() {
        super(new HTTPTaskManager("http://localhost:8078"));
    }

    @BeforeEach
    public void start() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    public void finish() {
        kvServer.stop();
        httpTaskServer.stop();
    }
}
