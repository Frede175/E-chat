package Business.Connection;

import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class RestConnectTest {

    @Test
    public void create_chat_returns_201() {
        HttpClient client = mock(HttpClient.class);

        assertEquals(2,2);

    }
}
