package com.example.demo;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the {@link BaseDocController}
 */
@SpringBootTest
@AutoConfigureMockMvc()
public class BaseDocControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BaseDocRepository baseDocRepository;

    @BeforeAll
    public static void set(@Autowired BaseDocRepository bdr) {
        BaseDoc baseDoc = new BaseDoc();
        for (int i = 0; i < 10; i++) {
            String id = "id" + i;
            String firstname = "firstname" + i;
            String lastname = "lastname" + i;
            String email = "mail" + i + "@host.com";
            baseDoc.setId(id);
            baseDoc.setFirstname(firstname);
            baseDoc.setLastname(lastname);
            baseDoc.setEmail(email);
            bdr.save(baseDoc).subscribe();
        }
    }

    @AfterAll
    public static void endAll(@Autowired BaseDocRepository bdr) {
        bdr.deleteAll().subscribe();
    }

    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void close() {

    }

    @Test
    public void findAll() throws Exception {
        MvcResult result = mockMvc.perform(get("/base"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(result))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").isString())
                .andExpect(jsonPath("$[1].email").isString());
    }

    @Test
    public void findById() throws Exception {

        MvcResult result = mockMvc.perform(get("/base/id0"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value("firstname0"))
                .andExpect(jsonPath("$.lastname").value("lastname0"))
                .andExpect(jsonPath("$.email").value("mail0@host.com"));
    }

    @Test
    public void save() throws Exception {
        String entity = """
                {
                    "id": "crid",
                    "email": "newmail@temp.com",
                    "firstname": "Ann",
                    "lastname": "Joys"
                }""";

        mockMvc.perform(post("/base")
                        .content(entity)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/base/crid"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstname").value("Ann"))
                .andExpect(jsonPath("$.lastname").value("Joys"))
                .andExpect(jsonPath("$.email").value("newmail@temp.com"));
    }
}