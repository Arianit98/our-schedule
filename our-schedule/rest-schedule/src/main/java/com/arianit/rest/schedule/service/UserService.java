package com.arianit.rest.schedule.service;

import com.arianit.rest.schedule.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.util.List;

@ApplicationScoped
@Transactional(Transactional.TxType.REQUIRED)
public class UserService {

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<User> findAllUsers() {
        return User.listAll();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public User findUserById(Long id) {
        return User.findById(id);
    }

    public User persistUser(@Valid User user) {
        user.persist();
        return user;
    }

    public User updateUser(@Valid User user) {
        User entity = User.findById(user.id);
        entity.name = user.name;
        entity.surname = user.surname;
        entity.username = user.username;
        entity.email = user.email;
        entity.password = user.password;
        entity.birthday = user.birthday;
        entity.active = user.active;
        return entity;
    }

    public void deleteUser(Long id) {
        User user = User.findById(id);
        user.delete();
    }
}
