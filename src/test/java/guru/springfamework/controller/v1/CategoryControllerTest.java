package guru.springfamework.controller.v1;

import guru.springfamework.api.v1.model.CategoryDTO;
import guru.springfamework.controller.RestResponseEntityExceptionHandler;
import guru.springfamework.service.CategoryService;
import guru.springfamework.service.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoryControllerTest {


    @Mock
    CategoryService categoryService;

    @InjectMocks
    CategoryController categoryController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        //build the mock mvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void testListCategories() throws Exception {
        CategoryDTO bob = new CategoryDTO();
        bob.setId(1L);
        bob.setName("BOB");
        CategoryDTO jim = new CategoryDTO();
        jim.setId(2L);
        jim.setName("JIM");

        List<CategoryDTO> categoryDTOList = Arrays.asList(bob, jim);

        when(categoryService.getAllCategories()).thenReturn(categoryDTOList);

        mockMvc.perform(get("/api/v1/categories/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categories", hasSize(2)));
    }

    @Test
    public void testGetByNameCategories() throws Exception {
        CategoryDTO bob = new CategoryDTO();
        bob.setId(1L);
        bob.setName("Bob");

        when(categoryService.getCategoryByName(bob.getName())).thenReturn(bob);

        mockMvc.perform(get("/api/v1/categories/Bob")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Bob")));
    }

    @Test
    public void getNameNotFound() throws Exception {

        when(categoryService.getCategoryByName(anyString()))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CategoryController.BASE_URL + "/any")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}