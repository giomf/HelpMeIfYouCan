package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.model.Filters;
import de.helpmeifyoucan.helpmeifyoucan.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userModelController;

    @Autowired
    public UserDetailsServiceImpl(UserService userModelController) {

        this.userModelController = userModelController;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var optUser = this.userModelController.getOptional(Filters.eq("email", email));
        if (optUser.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        var user = optUser.get();
        return new User(user.getId().toString(), user.getPassword(), this.rolesToAuthoritys(user.getRoles()));
    }

    private List<SimpleGrantedAuthority> rolesToAuthoritys(List<Roles> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());
    }

}