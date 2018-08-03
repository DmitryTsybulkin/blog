package com.pany.blog.services;

import com.pany.blog.config.SecurityPasswordEncoder;
import com.pany.blog.dtos.UserDto;
import com.pany.blog.exceptions.EntryDuplicateException;
import com.pany.blog.exceptions.ResourceNotFoundException;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.RoleRep;
import com.pany.blog.repositories.UserRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
            logger.warn("User with login: " + reqUserDto.login + " already exists.");
            throw new EntryDuplicateException();
        }

        userRep.save(new User(reqUserDto.login,
                passwordEncoder.encoder().encode(reqUserDto.password),
                reqUserDto.email,
                reqUserDto.roles.stream().map(Role::new).collect(Collectors.toSet()),
                null));
        logger.info("Created new user with login: " + reqUserDto.login);
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(final Long id) {
        return toDto(userRep.findById(id).orElseThrow(this::getUserNotFoundException));
    }

    @Transactional(readOnly = true)
    public UserDto getUserByLogin(final String login) {
        return toDto(userRep.findUserByLogin(login).orElseThrow(this::getUserNotFoundException));
    }

    @Transactional(readOnly = true)
    public List<UserDto> getUsersList() {
        return userRep.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public void updateUser(final UserDto userDto) {
        User user = userRep.findById(userDto.id).orElseThrow(this::getUserNotFoundException);
        user.setLogin(userDto.login);
        user.setPassword(userDto.password);
        user.setEmail(userDto.email);
        user.setRoles(userDto.roles.stream().map(Role::new).collect(Collectors.toSet()));
        logger.info("User: " + user.getLogin() + " updated successfully.");
    }

    @Transactional
    public void deleteUser(final Long id) {
        userRep.delete(userRep.findById(id).orElseThrow(this::getUserNotFoundException));
        logger.info("User by id: " + id + " deleted successfully.");
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getLogin(), user.getEmail(), user.getRoles().stream()
                .map(Role::getName).collect(Collectors.toList()));
    }

    public User fromDto(UserDto userDto) {
        return new User(userDto.login, userDto.password, userDto.email, userDto.roles.stream()
                .map(s -> roleRep.getRoleByName(s).orElseThrow(this::getRoleNotFoundException)).collect(Collectors.toSet()), null);
    }

    private ResourceNotFoundException getUserNotFoundException() {
        logger.warn("User not found.");
        return new ResourceNotFoundException();
    }

    private ResourceNotFoundException getRoleNotFoundException() {
        logger.warn("Role doesn't exists.");
        return new ResourceNotFoundException();
    }

}
