package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomerMapperTest {

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    @Test
    public void customerToCustomerDTO() {
        Customer vica = new Customer("Vica","Odey");
        vica.setId(1L);

        CustomerDTO vicaDTO = customerMapper.customerToCustomerDTO(vica);
        assertEquals(vica.getId(), vicaDTO.getId());
        assertEquals(vica.getFirstName(), vicaDTO.getFirstName());
        assertEquals(vica.getLastName(), vicaDTO.getLastName());
    }
}