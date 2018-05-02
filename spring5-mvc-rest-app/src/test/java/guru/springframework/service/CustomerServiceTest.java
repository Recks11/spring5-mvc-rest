package guru.springframework.service;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.domain.Customer;
import guru.springframework.model.CustomerDTO;
import guru.springframework.repositories.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {
    CustomerMapper customerMapper = CustomerMapper.INSTANCE;
    @Mock
    CustomerRepository customerRepository;

    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(customerRepository, customerMapper);
    }

    @Test
    public void getAllCustomers() {
        List<Customer> customerList = Arrays.asList(new Customer(), new Customer(), new Customer());

        when(customerRepository.findAll()).thenReturn(customerList);
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();

        assertEquals(customerDTOList.size(), 3);
    }

    @Test
    public void getCustomerById() {
        Customer bob = new Customer("Bob", "The Builder");
        bob.setId(2L);

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(bob));
        CustomerDTO bobDTO = customerService.getCustomerById(2L);

        assertEquals(bobDTO.getFirstName(), bob.getFirstName());
    }

    @Test
    public void createNewCustomer() throws Exception {

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("jimmy");
        customerDTO.setLastName("Neutron");

        Customer savedCustomer = new Customer(customerDTO.getFirstName(), customerDTO.getLastName());
        savedCustomer.setId(2L);

        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);
        CustomerDTO testCustomerDTO = customerService.createNewCustomer(customerDTO);

        assertEquals("/api/v1/customers/2", testCustomerDTO.getCustomerUrl());

    }

    @Test
    public void updateCustomer() throws Exception{
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName("jimmy");
        customerDTO.setLastName("Neutron");


        Customer customer = new Customer("jimmy","neutron");
        customer.setId(2L);
        Customer updatedCustomer = new Customer("updated","neutron");
        updatedCustomer.setId(2L);


        CustomerDTO newCustomer = new CustomerDTO();
        newCustomer.setCustomerUrl("/api/v1/customers/"+ 2);
        newCustomer.setFirstName("updated");
        newCustomer.setLastName("neutron");

        when(customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO))).thenReturn(customer);
        CustomerDTO savedInstance = customerService.createNewCustomer(customerDTO);


        when(customerRepository.findById(2L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        CustomerDTO updatedInstance = customerService.saveCustomerByDTO(2L, newCustomer);



        assertNotEquals(savedInstance.getFirstName(), updatedInstance.getFirstName());
        assertEquals(savedInstance.getLastName(), updatedInstance.getLastName());
        verify(customerRepository, times(2)).save(any(Customer.class));

    }

    @Test
    public void deleteById() {
        Long id = 2L;


        customerService.deleteCustomerById(id);
        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}