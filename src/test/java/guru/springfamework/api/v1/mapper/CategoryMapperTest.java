package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.domain.Category;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryMapperTest {

    CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    @Test
    public void categoryToCategoryDTO() throws Exception {

        Category testCategory = new Category();
        testCategory.setName("test");
        testCategory.setId(1L);

        CategoryDTO categoryDTO = categoryMapper.categoryToCategoryDTO(testCategory);

        assertEquals(testCategory.getId(), categoryDTO.getId());
        assertEquals(testCategory.getName(), categoryDTO.getName());
    }
}