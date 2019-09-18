package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponseTest;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.UserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServicesTest {

    private UserDetails userDetails;
    private Authentication authToken;

    @Autowired
    UserDetailsServices userDetailsServices;

    @Autowired
    UserServices userServices;

    @Before
    public void setUp() throws Exception {
        userDetails = userDetailsServices.loadUserByUsername("admin");
        authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void userRegistration() {
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        User user = new User("unit.test", "pass123", true, roles, "syarbeat@gmail.com");
        APIResponse response = userServices.userRegistration(user);

        User userdata = (User)response.getData();

        assertThat("User has been registered successfully", is(response.getMessage()));
        assertThat("unit.test", is(userdata.getUsername()));
        assertThat("syarbeat@gmail.com", is(userdata.getEmail()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void updateUserData() {
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        User user = new User(2,"test2", "pass123", true, roles, "syarbeat@gmail.com");

        APIResponse response = userServices.updateUserData(user);
        System.out.println("Response:"+response);
        //APIResponse dataresponse = (APIResponse) response.getData();
        User userdata = (User)response.getData();

        assertThat("Update user data has been updated successfully", is(response.getMessage()));
        assertThat("test2", is(userdata.getUsername()));
        assertThat("syarbeat@gmail.com", is(userdata.getEmail()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void resetPassword() {
        APIResponse response = userServices.resetPassword("test3", "pass123");
        User userdata = (User)response.getData();
        assertThat("Update user password has been updated successfully", is(response.getMessage()));
        assertThat("test3", is(userdata.getUsername()));
        assertThat("syarbeat@gmail.com", is(userdata.getEmail()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteUserByUsername() {
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        User user = new User("unit.test2", "pass123", true, roles, "syarbeat@gmail.com");
        APIResponse response = userServices.userRegistration(user);
        User userdata = (User)response.getData();

        APIResponse deleteResponse = userServices.deleteUserByUsername(userdata.getUsername());
        User userdelete = (User)deleteResponse.getData();
        assertThat("unit.test2", is(userdelete.getUsername()));
        assertThat("syarbeat@gmail.com", is(userdelete.getEmail()));
    }

    @Test
    public void findUserById() {
        APIResponse response = userServices.findUserById(2);
        User userdata = (User)response.getData();

        assertThat("User data was found", is(response.getMessage()));
        assertThat("test2", is(userdata.getUsername()));
        assertThat("srf.hidayat@gmail.com", is(userdata.getEmail()));
    }

    @Test
    public void findUserByUsername() {
        APIResponse response = userServices.findUserByUsername("test2");
        User userdata = (User)response.getData();

        assertThat("User data was found", is(response.getMessage()));
        assertThat("test2", is(userdata.getUsername()));
        assertThat("srf.hidayat@gmail.com", is(userdata.getEmail()));
    }

    @Test
    public void findUserByEmail() {
        User user = userServices.findUserByEmail("syarif.hidayat@mitrais.com");

        assertThat("admin", is(user.getUsername()));
        assertThat("admin", is(user.getFirstname()));
        assertThat("admin", is(user.getLastname()));
    }

    @Test
    public void getAllUsers() {
        APIResponse response = userServices.getAllUsers();
        List<User> userdata = (List<User>)response.getData();

        assertThat("Users data was founds", is(response.getMessage()));
        assertThat("admin", is(userdata.get(0).getUsername()));
        assertThat("syarif.hidayat@mitrais.com", is(userdata.get(0).getEmail()));
    }

    @Test
    @Transactional
    @Rollback(true)
    public void activateUser() {
        List<String> roles = new ArrayList<String>();
        roles.add("ROLE_USER");
        User user = new User("adam.jordan", "pass123", false, roles, "syarbeat@gmail.com");
        APIResponse response = userServices.userRegistration(user);

        APIResponse activateResponse = userServices.activateUser("adam.jordan");

        User userdata = (User)activateResponse.getData();

        assertThat("User has been activated", is(activateResponse.getMessage()));
        assertThat("adam.jordan", is(userdata.getUsername()));
        assertThat("syarbeat@gmail.com", is(userdata.getEmail()));
    }
}