package guru.springframework.service;


import guru.springframework.model.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO createNewCustomer(CustomerDTO customerDTO);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerById(Long id);
    CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO);
    CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO);
    void deleteCustomerById(Long id);

}
