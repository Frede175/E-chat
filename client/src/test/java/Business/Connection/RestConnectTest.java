package Business.Connection;

import Acquaintence.ConnectionState;
import Business.Models.Chat;
import Business.Models.Page;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class RestConnectTest {

    @Mock
    HttpClient client;

    @Mock
    HttpResponse response;

    @Mock
    HttpEntity entity;

    @Mock
    StatusLine line;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private void setup(String content, int code) throws IOException {
        when(line.getStatusCode()).thenReturn(code);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes()));
        when(response.getStatusLine()).thenReturn(line);
        when(response.getEntity()).thenReturn(entity);
        when(client.execute(any(HttpRequestBase.class))).thenReturn(response);
    }

    @Test
    public void post_has_correct_url_and_params() throws IOException {
        HttpPost post = spy(new HttpPost());

        setup("{ \"name\": \"Unit_test\", \"id\": 1, \"isGroupChat\": true, \"departmentId\": 1}", 201);

        new RestConnect(client, PathEnum.CreateChatroom, "").create(post).execute(1, new Chat("Unit_test"));

        assertEquals("/api/chat/1", post.getURI().getPath());

        String json = EntityUtils.toString(post.getEntity());
        assertTrue(json.contains("\"name\":\"Unit_test\""));

    }

    @Test
    public void post_has_correct_headers() throws IOException {
        HttpPost post = spy(new HttpPost());

        String token = "tempToken";

        setup("{ \"name\": \"Unit_test\", \"id\": 1, \"isGroupChat\": true, \"departmentId\": 1}", 201);

        new RestConnect(client, PathEnum.CreateChatroom, token).create(post).execute(1, new Chat("Unit_test"));

        Header header = post.getFirstHeader("Authorization");
        assertEquals("Bearer " + token, header.getValue());

        header = post.getFirstHeader("Content-Type");
        assertEquals("application/json; charset=UTF-8", header.getValue());

    }

    @Test
    public void post_has_correct_return() throws IOException {
        HttpPost post = spy(new HttpPost());

        setup("{ \"name\": \"Unit_test\", \"id\": 1, \"isGroupChat\": true, \"departmentId\": 1}", 201);

        RequestResponse<Chat> result = new RestConnect(client, PathEnum.CreateChatroom, "").create(post).execute(1, new Chat("Unit_test"));

        assertNotNull(result);
        assertNotNull(result.getResponse());

        assertEquals(result.getConnectionState(), ConnectionState.SUCCESS);

        assertEquals(result.getResponse().getName(), "Unit_test");
        assertEquals(result.getResponse().getId(), 1);

    }

    @Test
    public void get_has_correct_url_and_params() throws IOException {
        HttpGet get = spy(new HttpGet());

        setup("[]", 200);

        new RestConnect(client, PathEnum.GetMessages, "").create(get).execute(1, new Page(0,20).toMap());

        assertEquals("/api/Messages/1", get.getURI().getPath());
        assertEquals("pageNumber=0&pageSize=20", get.getURI().getQuery());
    }

    @Test
    public void get_has_correct_headers() throws IOException {
        HttpGet get = spy(new HttpGet());

        String token = "tempToken";

        setup("[]", 200);

        new RestConnect(client, PathEnum.GetMessages, token).create(get).execute(1, new Page(0,20).toMap());

        Header header = get.getFirstHeader("Authorization");
        assertEquals("Bearer " + token, header.getValue());

    }


}
