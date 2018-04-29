package guru.springfamework.controller.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controller.RestResponseEntityExceptionHandler;
import guru.springfamework.domain.Vendor;
import guru.springfamework.service.ResourceNotFoundException;
import guru.springfamework.service.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.print.attribute.standard.Media;

import java.util.Arrays;
import java.util.List;

import static guru.springfamework.controller.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Behavior Driven test
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = VendorController.class)
public class VendorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VendorService vendorService;

    private VendorDTO initialVendor, returnVendor;

    @Before
    public void setUp() throws Exception {
        initialVendor = new VendorDTO();
        initialVendor.setName("Benomart");
        returnVendor = new VendorDTO();
        returnVendor.setName("Benoly");
    }

    @Test
    public void getAllVendors() throws Exception {

        List<VendorDTO> vendorDTOList = Arrays.asList(initialVendor, returnVendor);
        given(vendorService.getAllVendors()).willReturn(vendorDTOList);
        mockMvc.perform(get(VendorController.API_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    public void getVendorById() throws Exception {

        given(vendorService.findByID(1L)).willReturn(returnVendor);
        mockMvc.perform(get(VendorController.API_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.name", is("Benoly")));
    }

    @Test
    public void createVendor() throws Exception {

        given(vendorService.createNewVendor(returnVendor)).willReturn(returnVendor);

        mockMvc.perform(post(VendorController.API_URL + '/')
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(returnVendor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Benoly")));
    }

    @Test
    public void deleteVendorById() throws Exception {
        mockMvc.perform(delete(VendorController.API_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService, times(1)).deleteVendor(1L);
    }

    @Test
    public void updateVendorByPatch() throws Exception {

        given(vendorService.patchVendor(1L, initialVendor)).willReturn(returnVendor);

        mockMvc.perform(patch(VendorController.API_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(initialVendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Benoly")));
        verify(vendorService, times(1)).patchVendor(1L, initialVendor);
    }

    @Test
    public void updateVendorByPut() throws Exception {


        given(vendorService.updateVendor(1L, initialVendor)).willReturn(returnVendor);

        mockMvc.perform(put(VendorController.API_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(initialVendor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Benoly")));

        verify(vendorService, times(1)).updateVendor(1L, initialVendor);
    }

    @Test
    public void getVendorNotFound() throws Exception {

        doThrow(new ResourceNotFoundException()).when(vendorService).findByID(anyLong());
        mockMvc.perform(get(VendorController.API_URL + "/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}