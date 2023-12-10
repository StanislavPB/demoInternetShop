package org.demointernetshop.controllers.product;

import lombok.RequiredArgsConstructor;
import org.demointernetshop.dto.category.CategoryDto;
import org.demointernetshop.dto.category.CategoryWithPricesDto;
import org.demointernetshop.services.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getAllCategory(){
        return categoryService.getAllCategory();
    }

    @GetMapping("/{category_id}/price")
    public CategoryWithPricesDto getCategoryWithPrices(@PathVariable("category_id") Integer categoryId) {
        return categoryService.getCategoryWithPrices(categoryId);
    }
    @GetMapping("/price")
    public List<CategoryWithPricesDto> getCategoryWithPrices() {
        return categoryService.getPriceBoundsByAllCategories();
    }
}
