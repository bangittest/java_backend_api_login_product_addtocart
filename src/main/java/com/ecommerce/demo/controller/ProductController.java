package com.ecommerce.demo.controller;

import com.ecommerce.demo.common.ApiResponse;
import com.ecommerce.demo.dto.ProductDto;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.reponsitory.CategoryRepo;
import com.ecommerce.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryRepo categoryRepo;


    @PostMapping("add")
    public ResponseEntity<ApiResponse>createProduct(@RequestBody ProductDto productDto){
        Optional<Category>optionalCategory=categoryRepo.findById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"category does not exits"), HttpStatus.BAD_REQUEST);
        }
        productService.createProduct(productDto,optionalCategory.get());
        return new ResponseEntity<>(new ApiResponse(true,"product has been added"),HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<List<ProductDto>>getProducts(){
        List<ProductDto>products= productService.getAllProduct();
        return new ResponseEntity<>(products,HttpStatus.OK);
    }


    //edit product
    @PostMapping("update{productId}")
    public ResponseEntity<ApiResponse>updateProduct(@PathVariable("productId")Integer productId, @RequestBody ProductDto productDto) throws Exception {
        Optional<Category>optionalCategory=categoryRepo.findById(productDto.getCategoryId());
        if (!optionalCategory.isPresent()){
            return new ResponseEntity<>(new ApiResponse(false,"category does not exits"), HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(productDto,productId);
        return new ResponseEntity<>(new ApiResponse(true,"product has been update"),HttpStatus.OK);
    }
}
