package com.pany.blog.config;

import com.pany.blog.dtos.BlogAttrsDto;
import com.pany.blog.dtos.RoleDto;
import com.pany.blog.dtos.UserDto;
import com.pany.blog.model.BlogAttrs;
import com.pany.blog.model.Role;
import com.pany.blog.model.User;
import com.pany.blog.repositories.BlogAttrsRep;
import com.pany.blog.repositories.RoleRep;
import com.pany.blog.repositories.UserRep;
import com.pany.blog.services.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DBInit implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JsonMapper jsonMapper;
    private RoleRep roleRep;
    private UserRep userRep;
    private BlogAttrsRep blogAttrsRep;
    private SecurityPasswordEncoder encoder;

    @Autowired
    public DBInit(JsonMapper jsonMapper, RoleRep roleRep, UserRep userRep, BlogAttrsRep blogAttrsRep, SecurityPasswordEncoder encoder) {
        this.jsonMapper = jsonMapper;
        this.roleRep = roleRep;
        this.userRep = userRep;
        this.blogAttrsRep = blogAttrsRep;
        this.encoder = encoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            initRoles();
            initUser();
            initBlogAttrs();
        } catch (IOException e) {
            logger.error("Error initializing DB: " + e.getMessage());
        }
        logger.info("DB is ready");
    }

    @Transactional
    void initRoles() throws IOException {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/roles.json")) {
            List<RoleDto> roleDtos = jsonMapper.toRole(in);
            if (roleDtos != null) {
                roleRep.saveAll(roleDtos.stream().map(roleDto -> new Role(roleDto.name)).collect(Collectors.toList()));
                logger.info("Roles loaded successfully");
            }
        } catch (IOException e) {
            logger.error("Error loading \"roles\" entries to entity: " + e.getMessage());
        }
    }

    @Transactional
    void initUser() throws IOException {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("test-data/user.json")) {
            List<UserDto> userDtos = jsonMapper.toUser(in);
            if (userDtos != null) {
                userRep.saveAll(userDtos.stream().map(userDto -> new User(userDto.login, encoder.encoder()
                        .encode(userDto.password), userDto.email, getRoles(userDto.roles), null))
                        .collect(Collectors.toList()));
                logger.info("Users loaded successfully");
            }
        } catch (IOException e) {
            logger.error("Error loading \"user\" entries to entity: " + e.getMessage());
        }
    }

    @Transactional
    void initBlogAttrs() throws IOException {
        try (InputStream in =this.getClass().getClassLoader().getResourceAsStream("test-data/blogSettings.json")) {
            List<BlogAttrsDto> dtos = jsonMapper.toBlogAttrsDtos(in);
            if (dtos != null) {
                blogAttrsRep.saveAll(dtos.stream().map(blogAttrsDto -> new BlogAttrs(blogAttrsDto.value,
                        blogAttrsDto.description)).collect(Collectors.toList()));
                logger.info("Blog attributes loaded successfully");
            }
        } catch (IOException e) {
            logger.error("Error loading \"Blog attributes\" entries to entity: " + e.getMessage());
        }
    }

    private Set<Role> getRoles(List<String> stRoles) {
        return stRoles.stream().map(Role::new).collect(Collectors.toSet());
    }
}
