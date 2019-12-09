package com.mitrais.cdc.bloggatewayapplicationv1.controller;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.NewPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResetPasswordPayload;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.ResponseWrapper;
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

    @Autowired
    UserServices userService;

    @Autowired
    EmailUtility emailUtility;

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

            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Utility("Sent Email was failed when User Registration", response).getResponseData());
    }

    @PatchMapping("/update/user")
    public ResponseEntity<ResponseWrapper> updateUserData(@RequestBody User user){
        return ResponseEntity.ok(new Utility("Update User Data", userService.updateUserData(user)).getResponseData());
    }

    @DeleteMapping("/delete/user/{username}")
    public ResponseEntity<ResponseWrapper> deleteUserDataByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(new Utility("Delete User Data", userService.deleteUserByUsername(username)).getResponseData());
    }

    @GetMapping("/find/user/{id}")
    public ResponseEntity<ResponseWrapper> findUserDataById(@PathVariable("id") int id){
        return ResponseEntity.ok(new Utility("Find User Data", userService.findUserById(id)).getResponseData());
    }

    @GetMapping("/find-user-by-username/{username}")
    public ResponseEntity<ResponseWrapper> findUserByUsername(@PathVariable("username") String username){
        return ResponseEntity.ok(new Utility("Find User Data By Username", userService.findUserByUsername(username)).getResponseData());
    }

    @GetMapping("/all-users")
    public ResponseEntity<ResponseWrapper> getAllUsers(){

        log.info("Token in Controller {} ", UserContextHolder.getContext().getAuthToken());

        return ResponseEntity.ok(new Utility("Find User Data", userService.getAllUsers()).getResponseData());
    }

    /**
     * url to handle reset password request, this api will send email that contain link to reset the password
     * */
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
            return ResponseEntity.ok(new Utility("Check your email to reset your password", user).getResponseData());

        }catch(Exception e) {
            log.error(e.getMessage(), e);

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Utility("Sending email to reset password was failed", user).getResponseData());

    }

    @PostMapping("/reset")
    public ResponseEntity<ResponseWrapper> resetPassword(@RequestBody NewPasswordPayload password){

        try{
            return ResponseEntity.ok(new Utility("Change password process have done successfully", userService.resetPassword(new String(Base64.decodeBase64(password.getId().getBytes())), password.getPassword())).getResponseData());
        }catch (Exception e){

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Utility("Change password process was failed", new String(Base64.decodeBase64(password.getId().getBytes()))).getResponseData());
    }

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

}