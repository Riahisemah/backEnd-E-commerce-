package com.semah.project.dao;

import com.semah.project.entity.Cart;
import com.semah.project.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDao extends CrudRepository<Cart, Integer > {
    public List<Cart> findByUser(User user);
}
