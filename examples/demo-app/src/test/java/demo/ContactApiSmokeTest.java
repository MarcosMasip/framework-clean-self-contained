package demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ContactApiSmokeTest {

    @Autowired
    private TestRestTemplate rest;

    @Test
    void apiShouldRespond() {
        ResponseEntity<Map> resp = rest.getForEntity("/api/demo/contacts", Map.class);
        assertEquals(200, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().containsKey("data"));
        assertTrue(resp.getBody().containsKey("response"));
    }
}
