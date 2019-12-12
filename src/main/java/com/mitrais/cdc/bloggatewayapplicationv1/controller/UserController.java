/**
 * <h1>User Controller</h1>
 * Class to create API Controller for User related activities
 * like UserRegistration, User delete, Update and read user data
 * as well.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */


package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.NewPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResetPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResponseWrapper;
import com.mitrais.cdc.bloggatewayapplicationv1.security.jwt.InvalidJwtAuthenticationException;
import com.mitrais.cdc.bloggatewayapplicationv1.services.UserServices;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.EmailUtility;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.UserContextHolder;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController extends CrossOriginController{


    private UserServices userService;
    private EmailUtility emailUtility;

    /**
     * This method will be used to Create User,
     * For successful user creation, the system will send email
     * that contain link to activate User.
     *
     * @param user
     * @return ResponseEntity that contain text confirmation to check email.
     */
    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> userRegister(@RequestBody User user){

        if(userService.findUserByUsername(user.getUsername()).getMessage().equals("User data was found")){
            return ResponseEntity.ok(new Utility("Your username is not available, please find the new one", user).getResponseData());
        }

        APIResponse response = userService.userRegistration(user);

        if(response.isSuccess()){
            String bytesEncoded = new String(Base64.encodeBase64(user.getUsername().getBytes()));
            String contents = "Please klik the following link to activate your account, <br/> <a href = \"http://localhost:8090/api/activate?id=" +bytesEncoded+"\">Activate Account</a>";

            try {
                log.info("username--:"+ user.getUsername());

                List<String> roles = user.getRoles();
                for(String role : roles){
                    log.info("role--:"+ role);
                }

                log.info("token--:"+ bytesEncoded);
                Map<String, String> data = new HashMap<String, String>();
                data.put("email", user.getEmail());
                data.put("token", bytesEncoded);
                data.put("username", user.getUsername());
                data.put("subject", "[Admin] Please Activate Your Account !!");
                data.put("contents", contents);
                emailUtility.sendEmail(data);
                return ResponseEntity.ok(new Utility("Check your email to activate your account", user).getResponseData());

            }catch(Exception e) {
                log.error(e.getMessage(), e);
                throw new ControllerException(e.getMessage());

            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Utility("Sent Email was failed when User Registration", response).getResponseData());
    }

    /**
     * This method will be used to update user data,
     * and will return the updated user data for successfully update process.
     *
     * @param user
     * @return Updated User Data
     */
    @PatchMapping("/update/user")
    public ResponseEntity<ResponseWrapper> updateUserData(@RequestBody User user){
        return ResponseEntity.ok(new Utility("Update User Data", userService.updateUserData(user)).getResponseData());
    }

    /**
     * This method will be used to delete user data,
     * and will return deleted user data for successfully deletion process.
     *
     * @param username
     * @return Deleted user data
     */
    @DeleteMapping("/delete/user/{username}")
    public ResponseEntity<ResponseWrapper> deleteUserDataByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(new Utility("Delete User Data", userService.deleteUserByUsername(username)).getResponseData());
    }

    /**
     * This method will be used to find user data based on the given user id
     * and will return the user data.
     *
     * @param id
     * @return User data
     */
    @GetMapping("/find/user/{id}")
    public ResponseEntity<ResponseWrapper> findUserDataById(@PathVariable("id") int id){
        return ResponseEntity.ok(new Utility("Find User Data", userService.findUserById(id)).getResponseData());
    }

    /**
     * This method will be used to find user data based on the given username
     *
     * @param username
     * @return User data
     */
    @GetMapping("/find-user-by-username/{username}")
    public ResponseEntity<ResponseWrapper> findUserByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(new Utility("Find User Data By Username", userService.findUserByUsername(username)).getResponseData());
    }

    /**
     * This method will be used to get All User Data
     *
     * @return User Data list
     */
    @GetMapping("/all-users")
    public ResponseEntity<ResponseWrapper> getAllUsers(){

        log.info("Token in Controller {} ", UserContextHolder.getContext().getAuthToken());

        return ResponseEntity.ok(new Utility("Find User Data", userService.getAllUsers()).getResponseData());
    }

    /**
     * This method will be used to send email to the user
     * that contain reset password link
     *
     */
    @PostMapping("/resetpassword")
    public ResponseEntity<ResponseWrapper> resetPasswordRequest(@RequestBody ResetPasswordPayload request){

        String email = request.getEmail();
        User user = userService.findUserByEmail(email);
        String encodedUsername = new String(Base64.encodeBase64(user.getUsername().getBytes()));
        String contents = "Please klik the following link to reset your password, <br/> <a href = \"http://localhost:3000/resetpassword?id=" +encodedUsername+"\">Reset Password</a>";

        try {
            Map<String, String> resetData = new HashMap<String, String>();
            resetData.put("email", email);
            resetData.put("token", encodedUsername);
            resetData.put("username", user.getUsername());
            resetData.put("contents", contents);
            resetData.put("subject", "[Admin] Your password reset request");
            emailUtility.sendEmail(resetData);

        }catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new ControllerException(e.getMessage());
        }

        return ResponseEntity.ok(new Utility("Check your email to reset your password", user).getResponseData());

    }

    /**
     * This method will be used to handle Reset Password
     *
     * @param password
     * @return Edited user data
     */
    @PostMapping("/reset")
    public ResponseEntity<ResponseWrapper> resetPassword(@RequestBody NewPasswordPayload password){
        return ResponseEntity.ok(new Utility("Change password process have done successfully", userService.resetPassword(new String(Base64.decodeBase64(password.getId().getBytes())), password.getPassword())).getResponseData());
    }

    /**
     * This method will be used to activate user based on the given encoded username
     * and will return the activated user data.
     *
     * @param id
     * @return Activated user data
     */
    @GetMapping("/activate")
    public ResponseEntity<ResponseWrapper> activateUser(@RequestParam("id") String id){

        byte[] usernameDecoded = Base64.decodeBase64(id.getBytes());
        String username = new String(usernameDecoded);
        log.info("USERNAME", username);
        APIResponse response = userService.activateUser(username);

        if(response.isSuccess())
        {
            return ResponseEntity.ok(new Utility("Your account has been activated", username).getResponseData());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Utility("User activated was failed", null).getResponseData());
    }

    public UserServices getUserService() {
        return userService;
    }

    @Autowired
    public void setUserService(UserServices userService) {
        this.userService = userService;
    }

    public EmailUtility getEmailUtility() {
        return emailUtility;
    }

    @Autowired
    public void setEmailUtility(EmailUtility emailUtility) {
        this.emailUtility = emailUtility;
    }
}