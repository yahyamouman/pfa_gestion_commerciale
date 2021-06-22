package gc.rec.security.service;


import gc.rec.dao.UserRepository;
import gc.rec.entities.Role;
import gc.rec.entities.User;
import gc.rec.entities.UsersRoles;
import gc.rec.security.entity.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("NotFound: " + username));

        return user.map(MyUserDetails::new).get();
    }

    public String loadRolesByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        UsersRoles[] roles = (UsersRoles[]) user.get().getRoles().toArray();
        String result = "";
        for (UsersRoles usersRoles : roles){
            result+=usersRoles.getRole().getRole();
        }

        return result;
    }
}
