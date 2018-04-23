package com.pany.blog.services;

import com.pany.blog.config.SecurityPasswordEncoder;
import com.pany.blog.dtos.UserDto;
import com.pany.blog.exceptions.EntryDuplicateException;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    private final static int PASSWORD_LENGTH = 8;

    private final UserRep userRep;

    private final SecurityPasswordEncoder passwordEncoder;

    private final MailImpl mail;

    @Autowired
    public UserService(UserRep userRep, SecurityPasswordEncoder passwordEncoder, MailImpl mail) {
        this.userRep = userRep;
        this.passwordEncoder = passwordEncoder;
        this.mail = mail;
    }

    public void createUser(UserDto reqUserDto) {

        if (userRep.findUserByLogin(reqUserDto.login) != null) {
            throw new EntryDuplicateException();
        }

        if (reqUserDto.roles.isEmpty()) {
            reqUserDto.roles.add(new Role("USER_ROLE"));
        }

        String password = generatePassword();
        userRep.save(new User(reqUserDto.login, passwordEncoder.encoder().encode(password), reqUserDto.roles, null));
        mail.sendMail(reqUserDto.email, password);

    }

    public UserDto getUserById(Long id) {
        return toDto(userRep.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    public List<UserDto> getUsersList() {
        List<User> users = userRep.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        users.forEach(user -> userDtos.add(toDto(user)));
        return userDtos;
    }

    public void updateUser(UserDto userDto) {
        User user = userRep.findById(userDto.id).orElseThrow(ResourceNotFoundException::new);

        user.setLogin(userDto.login);

        if (userDto.changePassword) {
            String password = generatePassword();
            user.setPassword(password);
            mail.sendMail(userDto.email, password);
        }

        user.setRoles(userDto.roles);
    }

    public void deleteUser(Long id) {
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
