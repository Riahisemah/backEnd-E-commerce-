package com.semah.project.service;

import com.semah.project.dao.CartDao;
import com.semah.project.dao.ProductDao;
import com.semah.project.dao.UserDao;
import com.semah.project.entity.Cart;
import com.semah.project.entity.Product;
import com.semah.project.entity.User;
import com.semah.project.configuration.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartDao cartDao;

    public Product addNewProduct(Product product) {
        return productDao.save(product);
    }

    public List<Product> getAllProducts(int pageNumber, String searchKey) {
        Pageable pageable = PageRequest.of(pageNumber,12);

        if(searchKey.isEmpty()) {
            return (List<Product>) productDao.findAll(pageable);
        } else {
            return (List<Product>) productDao.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
                    searchKey, searchKey, pageable
            );
        }

    }

    public Product getProductDetailsById(Integer productId) {
        return productDao.findById(productId).get();
    }

    public void deleteProductDetails(Integer productId) {
        productDao.deleteById(productId);
    }

    public List<Product> getProductDetails(boolean isSingleProductCheckout, Integer productId) {
        if(isSingleProductCheckout && productId != 0) {
            // we are going to buy a single product

            List<Product> list = new ArrayList<>();
            Product product = productDao.findById(productId).get();
            list.add(product);
            return list;
        } else {
            // we are going to checkout entire cart
            String username = JwtRequestFilter.CURRENT_USER;
            User user = userDao.findById(username).get();
            List<Cart> carts = cartDao.findByUser(user);

            return carts.stream().map(x -> x.getProduct()).collect(Collectors.toList());
        }
    }
}
