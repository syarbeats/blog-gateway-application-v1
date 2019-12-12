/**
 * <h1>UserServices</h1>
 * Class to handle User CRUD.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.payload.APIResponse;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
public class UserServices {

    private UserRepository userRepository;

    public APIResponse userRegistration(User user){

        try{
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
            return new APIResponse(true, "Delete user data has been executed successfully", userData);
        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Delete user data was failed", userData);
    }

    public APIResponse findUserById(int id) {

        Optional<User> userOptional = userRepository.findById(id);

        if(!userOptional.isPresent()){
            log.info("User Status: NOT FOUND");
            return new APIResponse(false, "User data was not found", null);
        }else {
            return new APIResponse(true, "User data was found", userOptional.get());
        }

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

        User userdata = null;
        try{
            Optional<User> user = userRepository.findByEmail(email);

            if(user.isPresent() ){
                userdata = user.get();
            }

        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("Username not found");
        }

        return userdata;
    }

    public APIResponse getAllUsers(){

        try{
            return new APIResponse(true, "Users data was founds", userRepository.findAll());
        }catch (Exception e){
            log.info(e.getMessage(), e);
        }

        return new APIResponse(false, "Data was not found", null);
    }

    /**
     * This method will be used to activate
     * user, change enabled field to true.
     *
     * @param username
     * @return will return activated user data
     */
    public APIResponse activateUser(String username){

        User user = userRepository.findByUsername(username);
        user.setEnabled(true);
        User userData = userRepository.save(user);
        return new APIResponse(true, "User has been activated", userData);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
