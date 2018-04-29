package guru.springfamework.service;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.bootstrap.Bootstrap;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
 @DataJpaTest
public class CustomerServiceTestIT {//Integration Test

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    VendorRepository vendorRepository;

    CustomerService customerService;

    @Before
    public void setUp() throws Exception {
        System.out.println("Starting Data test");
        System.out.println(customerRepository.findAll());

        Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
        bootstrap.run();

        customerService = new CustomerServiceImpl(customerRepository, CustomerMapper.INSTANCE);
    }

    @Test
    public void patchCustomerUpdatedFirstName() throws Exception {
        String updatedName = "Updated";
        long id = getCustomerIdValue();

        Customer originalCustomer = customerRepository.getOne(id);
        assertNotNull(originalCustomer);

        String originalFirstName = originalCustomer.getFirstName();
        String originalLastName = originalCustomer.getLastName();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(updatedName);
        customerService.patchCustomer(id, customerDTO);

        Customer updatedCustomer = customerRepository.findById(id).get();
        assertNotNull(updatedCustomer);
        assertEquals(updatedName, updatedCustomer.getFirstName());
        assertThat(originalFirstName, not(equalTo(updatedCustomer.getFirstName())));
        assertThat(originalLastName, equalTo(updatedCustomer.getLastName()));
    }

    @Test
    public void patchCustomerUpdatedLastName() throws Exception {
        String updatedLastName = "Updated";
        long id = getCustomerIdValue();
        Customer originalCustomer = customerRepository.getOne(id);
        assertNotNull(originalCustomer);
        String originalFirstName = originalCustomer.getFirstName();
        String originalLastName = originalCustomer.getLastName();

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setLastName(updatedLastName);

        customerService.patchCustomer(id, customerDTO);
        Customer updatedCustomer = customerRepository.findById(id).get();
        assertNotNull(customerDTO);
        assertEquals(updatedCustomer.getFirstName(), originalFirstName);
        assertThat(updatedCustomer.getLastName(), not(equalTo(originalLastName)));
        assertThat(updatedCustomer.getLastName(), equalTo(updatedLastName));


    }

    private Long getCustomerIdValue() {
        List<Customer> customers = customerRepository.findAll();
        System.out.println("Loaded customers: " + customers.size());
        return customers.get(0).getId();
    }

}
