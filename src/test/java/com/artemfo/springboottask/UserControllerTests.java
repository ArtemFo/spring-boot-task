package com.artemfo.springboottask;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "UserTest", userDetailsServiceBeanName = "userService")
@TestPropertySource("/application-test.properties")
@Sql("/initDB.sql")
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void profilePage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/header/nav/a/span").string("UserTest"));
    }

    @Test
    public void notAdmin() throws Exception {
        this.mockMvc.perform(get("/profiles"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}