package guru.springframework.controller.v1;

import guru.springframework.controller.RestResponseEntityExceptionHandler;
import guru.springframework.model.CustomerDTO;
import guru.springframework.service.CustomerService;
import guru.springframework.service.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static guru.springframework.controller.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest {

    @InjectMocks
    CustomerController customerController;

    @Mock
    CustomerService customerService;

    MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    public void getAllCustomers() throws Exception {
        List<CustomerDTO> customers = Arrays.asList(new CustomerDTO(), new CustomerDTO(), new CustomerDTO());
        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get(CustomerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(3)));
    }

    @Test
    public void getCustomer() throws Exception{
        CustomerDTO bob = new CustomerDTO();
        bob.setLastName("The Builder");
        bob.setFirstName("Bob");

        when(customerService.getCustomerById(2L)).thenReturn(bob);

        MvcResult result = mockMvc.perform(get(CustomerController.BASE_URL+"/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Bob")))
                .andExpect(jsonPath("$.lastName", equalTo("The Builder")))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void createNewCustomer() throws Exception{
        CustomerDTO bob = new CustomerDTO();
        bob.setLastName("The Builder");
        bob.setFirstName("Bob");

        CustomerDTO returnBob = new CustomerDTO();
        returnBob.setLastName("The Builder");
        returnBob.setFirstName("Bob");
        returnBob.setCustomerUrl("/api/v1/customers/2");

        when(customerService.createNewCustomer(any(CustomerDTO.class))).thenReturn(returnBob);

        mockMvc.perform(post(CustomerController.BASE_URL)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bob)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("Bob")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL+"/2")));
    }

    @Test
    public void updateCustomer() throws Exception{
        CustomerDTO bob = new CustomerDTO();
        bob.setLastName("The Builder");
        bob.setFirstName("Bob");

        CustomerDTO returnBob = new CustomerDTO();
        returnBob.setLastName("The Builder");
        returnBob.setFirstName("Bob");
        returnBob.setCustomerUrl(CustomerController.BASE_URL+"/2");

        CustomerDTO updatedBob = new CustomerDTO();
        updatedBob.setLastName("The Builder");
        updatedBob.setFirstName("Updated");
        updatedBob.setCustomerUrl(CustomerController.BASE_URL+"/2");

        when(customerService.getCustomerById(anyLong())).thenReturn(returnBob);
        when(customerService.saveCustomerByDTO(anyLong(), any(CustomerDTO.class))).thenReturn(updatedBob);

        mockMvc.perform(put(CustomerController.BASE_URL+"/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(bob)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Updated")));

    }

    @Test
    public void patchCustomer()throws Exception {

        CustomerDTO customer = new CustomerDTO();
        customer.setFirstName("Fred");

        CustomerDTO returnDTO = new CustomerDTO();
        returnDTO.setFirstName(customer.getFirstName());
        returnDTO.setLastName("Flintstone");
        returnDTO.setCustomerUrl(CustomerController.BASE_URL+"/2");

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(CustomerController.BASE_URL+"/2")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Fred")))
                .andExpect(jsonPath("$.lastName", equalTo("Flintstone")))
                .andExpect(jsonPath("$.customerUrl", equalTo(CustomerController.BASE_URL+"/2")));
    }

    @Test
    public void deleteCustomer() throws Exception {

        mockMvc.perform(delete(CustomerController.BASE_URL+"/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(customerService, times(1)).deleteCustomerById(anyLong());
    }

    @Test
    public void getCustomerNotFound() throws Exception {

        when(customerService.getCustomerById(anyLong()))
                .thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CustomerController.BASE_URL + "/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}