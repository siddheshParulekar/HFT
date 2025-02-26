package com.thrift.hft.service.serviceImpl;

import com.thrift.hft.controller.CartController;
import com.thrift.hft.dto.CartDTO;
import com.thrift.hft.entity.Cart;
import com.thrift.hft.entity.Product;
import com.thrift.hft.exceptions.AlreadyExistsException;
import com.thrift.hft.exceptions.NotFoundException;
import com.thrift.hft.repository.CartRepository;
import com.thrift.hft.repository.ProductRepository;
import com.thrift.hft.response.TokenResponse;
import com.thrift.hft.service.ICartService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements ICartService {
    private static final Logger logger = LogManager.getLogger(CartServiceImpl.class);

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartRepository cartRepository;

    @Override
    public CartDTO addToCart(String productId, TokenResponse tokenResponse) {
        logger.info("CartServiceImpl - Inside addToCart method");
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Article not found"));
        Optional<Cart> optionalCart = cartRepository.findByUserIdAndIsOrdered(tokenResponse.getUserId(), Boolean.FALSE);
        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
            List<Product> products = new ArrayList<>(cart.getProductList());
            if (products.contains(product))
                throw new AlreadyExistsException("This article already has been added to the cart");
            products.add(product);
            cart.setProductList(products);
            cart.setCartAmount(getCartValue(products));
            cart.setTotal(cart.getCartAmount().add(BigDecimal.valueOf(1000)));
        } else {
            List<Product> products = new ArrayList<>();
            products.add(product);
           cart = Cart.builder().userId(tokenResponse.getUserId())
                                .productList(products)
                   .cartAmount(getCartValue(products))
                   .total(getCartValue(products).add(BigDecimal.valueOf(1000))).build();
        }

        Cart savedCart = cartRepository.save(cart);
        return savedCart.getCartDTO();
    }

    @Override
    public void deleteFromCart(String productId, TokenResponse tokenResponse) {
        logger.info("CartServiceImpl - Inside deleteFromCart method");
        Product product = productRepository.findById(productId).orElseThrow(() -> new NotFoundException("Article not found"));
        Optional<Cart> optionalCart = cartRepository.findByUserIdAndIsOrdered(tokenResponse.getUserId(), Boolean.FALSE);
        if (optionalCart.isEmpty())
            throw new NotFoundException("There are no articles in your cart");

        Cart cart =optionalCart.get();
        if (!cart.getProductList().contains(product))
            throw new NotFoundException("This article is not present in your cart");

        cart.getProductList().remove(product);
        if (cart.getProductList().isEmpty()){
            cartRepository.deleteById(cart.getId());
            return;
        }

        cart.setCartAmount(getCartValue(cart.getProductList()));
        cart.setTotal(cart.getCartAmount().add(BigDecimal.valueOf(1000)));
        cartRepository.save(cart);
    }

    @Override
    public CartDTO viewMyCart(TokenResponse tokenResponse) {
        logger.info("CartServiceImpl - Inside viewMyCart method");
        Optional<Cart> optionalCart = cartRepository.findByUserIdAndIsOrdered(tokenResponse.getUserId(), Boolean.FALSE);
        if (optionalCart.isEmpty())
            return new CartDTO();


        return optionalCart.get().getCartDTO();
    }


    private BigDecimal getCartValue(List<Product> products){
        logger.info("CartServiceImpl - Inside getCartValue method");

        return   products.stream()
                .map(Product::getPrize)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}



