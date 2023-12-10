package org.demointernetshop.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.demointernetshop.entity.Category;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Category", description = "Category data")
public class CategoryDto {
    @Schema(description = "Category identifier", example = "1")
    private Integer id;
    @Schema(description = "Name of the Category", example = "smartphones")
    private String categoryName;

    public static CategoryDto from(Category category){
        return new CategoryDto().builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
