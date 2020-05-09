package com.artemfo.springboottask;

import com.artemfo.springboottask.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(value = "AdminTest", userDetailsServiceBeanName = "adminService")
@TestPropertySource("/application-test.properties")
@Sql("/initDB.sql")
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository repository;

    @Test
    public void homePageTest() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(xpath("/html/body/header/nav/a/span").string("AdminTest"));
    }

    @Test
    public void profilesListTest() throws Exception {
        this.mockMvc.perform(get("/profiles"))
                .andDo(print())
                .andExpect(xpath("//tr[@id='users_list']").nodeCount(3));
    }

    @Test
    public void addUser() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/signup")
                .param("name", "mockUser")
                .param("email", "m@m.m")
                .param("password", "123456789")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
        assertThat(adminService.loadUserByUsername("mockUser")).isNotNull();
    }

    @Test
    public void updateUser() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/profiles/3")
                .param("name", "qwert")
                .param("email", "m@m.m")
                .param("password", "123456789")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles"));
        assertThat(repository.findByName("qwert").getEmail()).isEqualTo("m@m.m");
    }

    @Test
    public void deleteUser() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/profiles/delete/3")
                .with(SecurityMockMvcRequestPostProcessors.csrf());
        this.mockMvc.perform(multipart)
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profiles"));
        assertThat(repository.findById(3L));
    }
}
