/**
 * <h1>UserDetailServices</h1>
 * Class to get UserDetails data based on the given username.
 *
 * @author Syarif Hidayat
 * @version 1.0
 * @since 2019-08-20
 * */

package com.mitrais.cdc.bloggatewayapplicationv1.services;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import com.mitrais.cdc.bloggatewayapplicationv1.repository.UserRepository;
import com.mitrais.cdc.bloggatewayapplicationv1.utility.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServices implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * This method will be used to get UserDetails data
     * based on the given username.
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        return new UserDetails(user);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

}
