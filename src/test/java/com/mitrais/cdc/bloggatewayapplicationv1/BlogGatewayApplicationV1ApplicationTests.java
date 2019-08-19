package com.mitrais.cdc.bloggatewayapplicationv1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.services.UserDetailsServices;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest
@Slf4j
public class BlogGatewayApplicationV1ApplicationTests {

    private final String USERNAME_FOR_ID_2 = "test2";
    private final String ROLE_FOR_ID_2 = "ROLE_USER";
    private final int ID = 2;
    private final String USERNAME = "test2";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private MockMvc mockMvc;
    private UserDetails userDetails;
    private Authentication authToken;

    @Autowired
    UserDetailsServices userDetailsServices;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void contextLoads() {

        userDetails = userDetailsServices.loadUserByUsername("admin");
        authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void userRegistration() throws Exception{
        User user = new User(4,"arkhyterima", "pass123", true, "ROLE_USER", "srf.hidayat@gmail.com");
        String userJson = mapper.writeValueAsString(user);

        mockMvc.perform(post("http://localhost:8090/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['message']", containsString("Check your email to activate your account")))
                .andExpect(jsonPath("$['contents']['username']", containsString("arkhyterima")))
                .andExpect(jsonPath("$['contents']['role']", containsString("ROLE_USER")));
    }

    @Test
    public void UpdateUserData() throws Exception{

        User user = new User(2,"test2", "test123", true, "ROLE_USER", "srf.hidayat@gmail.com");
        String userJson = mapper.writeValueAsString(user);

        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/api/update/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                        .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['contents']['message']", containsString("Update user data has been updated successfully")))
                .andExpect(jsonPath("$['contents']['data']['username']", containsString("test2")))
                .andExpect(jsonPath("$['contents']['data']['role']", containsString("ROLE_USER")));
    }

    @Test
    public void deleteUserByUsername() throws Exception{
        String username = "arkhyterima";
        mockMvc.perform(delete("/api/delete/user/"+username))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['contents']['message']", containsString("Delete user data has been executed successfully")))
                .andExpect(jsonPath("$['contents']['data']", containsString("arkhyterima")));

    }


    @Test
    public void findUserByID() throws Exception{

        mockMvc.perform(get("/api/find/user/"+ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$['contents']['data']['username']", containsString(USERNAME_FOR_ID_2)));

    }

    @Test
    public void findUserByUsername() throws Exception{

        mockMvc.perform(get("/api/find-user-by-username/"+USERNAME))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$['contents']['data']['role']", containsString(ROLE_FOR_ID_2)));

    }

    @Test
    public void getAllUsers() throws Exception{

        mockMvc.perform(get("/api/all-users/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$['contents']['message']", containsString("Users data was founds")))
                .andExpect(jsonPath("$['contents']['data'][0]['username']", containsString("admin")))
                .andExpect(jsonPath("$['contents']['data'][0]['role']", containsString("ROLE_ADMIN")));
    }

}
