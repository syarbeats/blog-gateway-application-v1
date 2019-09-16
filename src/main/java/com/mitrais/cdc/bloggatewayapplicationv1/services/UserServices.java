package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;

    public APIResponse userRegistration(User user){

        try{
            user.setRole("ROLE_USER");
            user.setEnabled(false);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            return new APIResponse(true,"User has been registered successfully", userRepository.save(user));
        }catch (Exception e){
            log.info(e.getMessage(), e);

        }

        return new APIResponse(false, "User registration was failed", null);
    }

    public APIResponse updateUserData(User user){

        try{
            User userData = userRepository.findByUsername(user.getUsername());

            if(!user.getPassword().isEmpty() && user.getPassword() != null){
                userData.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }

            if(!user.getEmail().isEmpty() && user.getEmail() != null){
                userData.setEmail(user.getEmail());
            }

            userData.setEnabled(user.isEnabled());
            return new APIResponse(true, "Update user data has been updated successfully", userRepository.save(userData));
        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Update user data was failed", null);
    }

    public APIResponse resetPassword(String username, String password){

        try{
            log.info("USERNAME THAT WANT TO CHANGE HIS PASSWORD:", username);
            User userData = userRepository.findByUsername(username);
            userData.setPassword(new BCryptPasswordEncoder().encode(password));
            return new APIResponse(true, "Update user password has been updated successfully", userRepository.save(userData));
        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Update user password was failed", null);
    }

    public APIResponse deleteUserByUsername(String username){
        User userData = null;
        try{
            userData = userRepository.findByUsername(username);
            userRepository.delete(userData);
            return new APIResponse(true, "Delete user data has been executed successfully", username);
        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Delete user data was failed", userData);
    }

    public APIResponse findUserById(int id){

        try{
            User user = userRepository.findById(id).get();

            if(user == null ){
                return new APIResponse(true, "User data was not found", null);
            }else
            {
                return new APIResponse(true, "User data was found", user);
            }

        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Internal System Error", null);
    }

    public APIResponse findUserByUsername(String username){

        try{
            User user = userRepository.findByUsername(username);
            if(user == null ){
                return new APIResponse(true, "User data was not found", null);
            }else
            {
                return new APIResponse(true, "User data was found", user);
            }

        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Internal System Error", null);
    }

    public User findUserByEmail(String email){

        try{
            User user = userRepository.findByEmail(email).get();

            if(user != null ){
                return user;
            }

        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return null ;
    }

    public APIResponse getAllUsers(){

        try{
            return new APIResponse(true, "Users data was founds", userRepository.findAll());
        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Data was not found", null);
    }

    public APIResponse activateUser(String username){

        User user = userRepository.findByUsername(username);
        user.setEnabled(true);
        User userData = userRepository.save(user);
        return new APIResponse(true, "User has been activated", userData);
    }
}
