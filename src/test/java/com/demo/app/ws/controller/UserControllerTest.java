package com.demo.app.ws.controller;

import com.demo.app.ws.entities.User;
import com.demo.app.ws.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, secure = false)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;
    private User user;
    private User user2;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        user = new User();
        user.setFirstName("Steve");
        user.setLastName("Rob");
        user.setEmail("steverob@gmail.com");
        user.setId(1L);
        user.setFirstLineOfAddress("12 Avenue");
        user.setSecondLineOfAddress("Commercial street");
        user.setTown("London");
        user.setPostCode("HA6 0EW");

        user2 = new User();
        user2.setFirstName("Stev");
        user2.setLastName("Ro");
        user2.setEmail("stevob@gmail.com");
        user2.setId(2L);
        user2.setFirstLineOfAddress("13 Avenue");
        user2.setSecondLineOfAddress("Commercial street");
        user2.setTown("London");
        user2.setPostCode("HA7 0EW");

    }

    @Test
    void testGetUser() throws Exception {
        String expected = objectMapper.writeValueAsString(user);
        when(userService.getUserById(anyLong())).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/users/" + user.getId()).accept(
                MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(expected, mvcResult.getResponse().getContentAsString());
    }

    @Test
    public void testGetAllUsers() throws Exception {

        List<User> users = Arrays.asList(user, user2);
        String expected = objectMapper.writeValueAsString(users);

        when(userService.getUsers()).thenReturn(users);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/users").accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        assertEquals(expected, mvcResult.getResponse().getContentAsString());
    }

    @Test
    void testCreateUser() throws Exception {
        String expected = objectMapper.writeValueAsString(user);
        when(userService.createUser(any(User.class))).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        assertEquals(expected, mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testUpdateUser() throws Exception {
        User updateUser = new User();
        updateUser.setFirstName("Steveupdated");
        updateUser.setLastName("Robupdated");
        updateUser.setEmail("steverobupdated@gmail.com");
        updateUser.setFirstLineOfAddress("12 Avenueupdated");
        updateUser.setSecondLineOfAddress("Commercial streetupdated");
        updateUser.setTown("London");
        updateUser.setPostCode("HA6 0EW");

        User updateUserWithId = new User();
        new ModelMapper().map(updateUser, updateUserWithId);
        updateUserWithId.setId(1L);
        //Exclude null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String putBody = objectMapper.writeValueAsString(updateUser);
        String expectedResponse = objectMapper.writeValueAsString(updateUserWithId);

        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(updateUserWithId);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/users/{userId}",
                updateUserWithId.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(putBody)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testPatchUpdateUser() throws Exception {
        User updateUser = new User();
        updateUser.setFirstName("Steveupdated");
        updateUser.setLastName("Robupdated");

        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());

        //Exclude null values
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String patchBody = objectMapper.writeValueAsString(updateUser);
        String expectedResponse = objectMapper.writeValueAsString(user);

        when(userService.patchUpdateUser(anyLong(), any(User.class))).thenReturn(user);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/users/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(patchBody)
                .accept(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        assertEquals(expectedResponse, mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(anyLong());
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();
        assertEquals("", mvcResult.getResponse().getContentAsString());

    }

}
