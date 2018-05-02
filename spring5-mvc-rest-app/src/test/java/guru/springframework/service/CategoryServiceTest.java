package guru.springframework.service;

import guru.springframework.api.v1.mapper.CategoryMapper;
import guru.springframework.api.v1.model.CategoryDTO;
import guru.springframework.domain.Category;
import guru.springframework.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CategoryServiceTest {

    private static long ID = 2L;
    private static String NAME = "Jimmy";
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository, CategoryMapper.INSTANCE);
    }

    @Test
    public void getAllCategories() {
        List<Category> categoryList = Arrays.asList(new Category(), new Category());

        when(categoryRepository.findAll()).thenReturn(categoryList);

        List<CategoryDTO> catList = categoryService.getAllCategories();

        assertEquals(2, catList.size());

    }

    @Test
    public void getCategoryByName() {
        Category category = new Category();
        category.setId(ID);
        category.setName(NAME);

        when(categoryRepository.findByName(anyString())).thenReturn(category);

        CategoryDTO categoryDTO = categoryService.getCategoryByName(NAME);
        assertEquals(category.getId(), categoryDTO.getId());
        assertEquals(category.getName(), categoryDTO.getName());

    }
}