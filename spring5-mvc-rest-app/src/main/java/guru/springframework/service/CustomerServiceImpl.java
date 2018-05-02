package guru.springframework.service;

import guru.springframework.api.v1.mapper.CustomerMapper;
import guru.springframework.api.v1.model.CustomerDTO;
import guru.springframework.controller.v1.CustomerController;
import guru.springframework.domain.Customer;
import guru.springframework.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDTO createNewCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO));
        CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(customer);
        returnDTO.setCustomerUrl(getCustomerUrl(returnDTO.getId()));
        return returnDTO;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> {
                    CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
                    customerDTO.setCustomerUrl(getCustomerUrl(customerDTO.getId()));
                    return customerDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Optional<CustomerDTO> customerDTO = customerRepository.findById(id)
                .map(customerMapper::customerToCustomerDTO);
        return customerDTO.orElseThrow(ResourceNotFoundException::new); //todo implement exception handler
    }

    @Override
    public CustomerDTO saveCustomerByDTO(Long id, CustomerDTO customerDTO) {

        CustomerDTO customer = this.getCustomerById(id);
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());

        Customer savedCustomer = customerRepository.save(customerMapper.customerDTOToCustomer(customer));
        CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(savedCustomer);
        returnDTO.setCustomerUrl(getCustomerUrl(returnDTO.getId()));
        return returnDTO;
    }

    @Override
    public CustomerDTO patchCustomer(Long id, CustomerDTO customerDTO) {
        return customerRepository.findById(id).map(
                customer -> {
                    if (customerDTO.getFirstName() != null) {
                        customer.setFirstName(customerDTO.getFirstName());
                    }
                    if (customerDTO.getLastName() != null) {
                        customer.setLastName(customerDTO.getLastName());
                    }

                    CustomerDTO returnDTO = customerMapper.customerToCustomerDTO(customerRepository.save(customer));
                    returnDTO.setCustomerUrl(getCustomerUrl(returnDTO.getId()));
                    return returnDTO;
                }).orElseThrow(ResourceNotFoundException::new); //todo implement exception handler
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }

    private String getCustomerUrl(Long id){
        return CustomerController.BASE_URL + '/'+ id;
    }
}
