package com.pany.blog.services;

import com.pany.blog.config.SecurityPasswordEncoder;
import com.pany.blog.dtos.UserDto;
import com.pany.blog.exceptions.EntryDuplicateException;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.RoleRep;
import com.pany.blog.repositories.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserService {

    private final static int PASSWORD_LENGTH = 8;

    private final UserRep userRep;

    private final RoleRep roleRep;

    private final SecurityPasswordEncoder passwordEncoder;

    private final MailImpl mail;

    @Autowired
    public UserService(UserRep userRep, RoleRep roleRep, SecurityPasswordEncoder passwordEncoder, MailImpl mail) {
        this.userRep = userRep;
        this.roleRep = roleRep;
        this.passwordEncoder = passwordEncoder;
        this.mail = mail;
    }

    @Transactional
    public void createUser(UserDto reqUserDto) {

        if (userRep.findUserByLogin(reqUserDto.login).isPresent()) {
            throw new EntryDuplicateException();
        }

        String password = generatePassword();
        userRep.save(new User(reqUserDto.login,
                passwordEncoder.encoder().encode(password),
                new HashSet<>(Collections.singleton(roleRep.getRoleByName("USER_ROLE"))),
                null));
    }

    @Transactional
    public UserDto getUserById(final Long id) {
        return toDto(userRep.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    @Transactional
    public UserDto getUserByLogin(final String login) {
        return toDto(userRep.findUserByLogin(login).orElseThrow(ResourceNotFoundException::new));
    }

    @Transactional
    public List<UserDto> getUsersList() {
        List<User> users = userRep.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(toDto(user)));
        return userDtos;
    }

    @Transactional
    public void updateUser(final UserDto userDto) {
        User user = userRep.findById(userDto.id).orElseThrow(ResourceNotFoundException::new);

        user.setLogin(userDto.login);

        if (userDto.changePassword) {
            String password = generatePassword();
            user.setPassword(password);
        }

    }

    @Transactional
    public void deleteUser(final Long id) {
        userRep.delete(userRep.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.login = user.getLogin();
        userDto.id = user.getId();
        userDto.roles = user.getRoles();
        userDto.posts = user.getPosts();
        return userDto;
    }

    public User fromDto(UserDto userDto, String password) {
        return new User(userDto.login, password, userDto.roles, userDto.posts);
    }

    private static String generatePassword() {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

}
