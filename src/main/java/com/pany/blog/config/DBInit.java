package com.pany.blog.config;

import com.pany.blog.repositories.RoleRep;
import com.pany.blog.services.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DBInit implements ApplicationListener<ApplicationReadyEvent> {

    @Value(value = "${path.data}")
    private String path;

    private RoleRep roleRep;

    @Autowired
    public DBInit(RoleRep roleRep) {
        this.roleRep = roleRep;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        initRoles();
    }

    private void initRoles() {
        File file = new File(path + "roles.json");
        roleRep.saveAll(JsonMapper.toRole(file));
    }
}
