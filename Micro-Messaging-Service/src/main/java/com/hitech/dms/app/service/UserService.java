package com.hitech.dms.app.service;


import java.util.List;

import com.hitech.dms.app.common.ServerResponse;
import com.hitech.dms.web.entity.user.UserEntity;

public interface UserService {

    List<UserEntity> getAll();

    UserEntity getOne(Integer id);

    void add(UserEntity user);

    void update(UserEntity user);

    void delete(Integer id);

    UserEntity getByUsernameAndPassword(String username, String password);

    ServerResponse login(String username, String password);

    void batchInsert(List<UserEntity> list);

    void batchUpdate(List<UserEntity> list);

}
