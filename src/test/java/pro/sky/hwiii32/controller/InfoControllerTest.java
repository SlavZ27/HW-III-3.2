package pro.sky.hwiii32.controller;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pro.sky.hwiii32.service.InfoService;

@WebMvcTest(controllers = InfoController.class)
@ActiveProfiles("infoControllerTest")
class InfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private InfoService infoService;

    @LocalServerPort
    private Integer port;

    @InjectMocks
    private InfoController infoController;

    @Test
    public void contextsLoad() {
        AssertionsForClassTypes.assertThat(infoController).isNotNull();
    }


    //GET http://localhost:8080/getPort
    @Test
    public void getPortTest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/student")
//                        .content(studentJson1.toString())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(id1))
//                .andExpect(jsonPath("$.name").value(name1))
//                .andExpect(jsonPath("$.age").value(age1));
//
//        assertThat(getPort.getStatusCode()).
//                isEqualTo(HttpStatus.OK);
//
//        assertThat(getPort.getBody())
//                .isEqualTo(port);
    }

}