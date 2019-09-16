package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.NewPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResetPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.services.UserServices;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.EmailUtility;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.Utility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController extends CrossOriginController{

    @Autowired
    UserServices userService;

    @Autowired
    EmailUtility emailUtility;

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public ResponseEntity<Map<Object, Object>> userRegister(@RequestBody User user){

        if(userService.findUserByUsername(user.getUsername()).getMessage().equals("User data was found")){
            return ResponseEntity.ok(new Utility("Your username is not available, please find the new one", user).getResponseData());
        }

        APIResponse response = userService.userRegistration(user);

        if(response.isSuccess()){
            String bytesEncoded = new String(Base64.encodeBase64(user.getUsername().getBytes()));
            String contents = "Please klik the following link to activate your account, <br/> <a href = \"http://localhost:8090/api/activate?id=" +bytesEncoded+"\">Activate Account</a>";

            try {
                log.info("username--:"+ user.getUsername());
                log.info("role--:"+ user.getRole());
                log.info("token--:"+ bytesEncoded);
                emailUtility.sendEmail(user.getEmail(), bytesEncoded, user.getUsername(), contents, "[Admin] Please Activate Your Account !!");
                return ResponseEntity.ok(new Utility("Check your email to activate your account", user).getResponseData());

            }catch(Exception e) {
                log.error(e.getMessage(), e);

            }
        }
        return ResponseEntity.ok(new Utility("Sent Email was failed when User Registration", response).getResponseData());
    }

    @RequestMapping(value="/update/user", method = RequestMethod.PATCH)
    public ResponseEntity<Map<Object, Object>> updateUserData(@RequestBody User user){
        return ResponseEntity.ok(new Utility("Update User Data", userService.updateUserData(user)).getResponseData());
    }

    @RequestMapping(value="/delete/user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<Map<Object, Object>> deleteUserData(@PathVariable("username") String username){
        return ResponseEntity.ok(new Utility("Delete User Data", userService.deleteUserByUsername(username)).getResponseData());
    }

    @RequestMapping(value="/find/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<Object, Object>> deleteUserData(@PathVariable("id") int id){
        return ResponseEntity.ok(new Utility("Find User Data", userService.findUserById(id)).getResponseData());
    }

    @RequestMapping(value="/find-user-by-username/{username}", method = RequestMethod.GET)
    public ResponseEntity<Map<Object, Object>> findUserByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(new Utility("Find User Data By Username", userService.findUserByUsername(username)).getResponseData());
    }

    @RequestMapping(value="/all-users", method = RequestMethod.GET)
    public ResponseEntity<Map<Object, Object>> getAllUsers(){
        return ResponseEntity.ok(new Utility("Find User Data", userService.getAllUsers()).getResponseData());
    }

    /**
     * url to handle reset password request, this api will send email that contain link to reset the password
     * */
    @RequestMapping(value="/resetpassword", method = RequestMethod.POST)
    public ResponseEntity<Map<Object, Object>> resetPasswordRequest(@RequestBody ResetPasswordPayload request){

        String email = request.getEmail();
        User user = userService.findUserByEmail(email);
        String encodedUsername = new String(Base64.encodeBase64(user.getUsername().getBytes()));
        String contents = "Please klik the following link to reset your password, <br/> <a href = \"http://localhost:3000/resetpassword?id=" +encodedUsername+"\">Reset Password</a>";

        if(user == null){
            return ResponseEntity.ok(new Utility("User data not found", null).getResponseData());
        }

        try {
            emailUtility.sendEmail(email, encodedUsername, user.getUsername(), contents, "[Admin] Your password reset request");
            return ResponseEntity.ok(new Utility("Check your email to reset your password", user).getResponseData());

        }catch(Exception e) {
            log.error(e.getMessage(), e);

        }
        return ResponseEntity.ok(new Utility("Sending email to reset password was failed", user).getResponseData());

    }

    @RequestMapping(value="/reset", method = RequestMethod.POST)
    public ResponseEntity<Map<Object, Object>> resetPassword(@RequestBody NewPasswordPayload password){

        try{
            return ResponseEntity.ok(new Utility("Change password process have done successfully", userService.resetPassword(new String(Base64.decodeBase64(password.getId().getBytes())), password.getPassword())).getResponseData());
        }catch (Exception e){

        }
        return ResponseEntity.ok(new Utility("Change password process was failed", new String(Base64.decodeBase64(password.getId().getBytes()))).getResponseData());
    }

    @RequestMapping(value="/activate", method = RequestMethod.GET)
    public ResponseEntity<Map<Object, Object>> activateUser(@RequestParam("id") String id){

        //byte[] usernameDecoded = Base64.decodeBase64(id.getToken().getBytes());
        byte[] usernameDecoded = Base64.decodeBase64(id.getBytes());
        String username = new String(usernameDecoded);
        log.info("USERNAME", username);
        APIResponse response = userService.activateUser(username);

        if(response.isSuccess())
        {
            return ResponseEntity.ok(new Utility("Your account has been activated", username).getResponseData());
        }

        return ResponseEntity.ok(new Utility("User activated was failed", null).getResponseData());
    }
}