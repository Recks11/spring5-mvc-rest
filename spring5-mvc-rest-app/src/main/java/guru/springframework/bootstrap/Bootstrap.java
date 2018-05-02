package guru.springframework.bootstrap;

import guru.springframework.domain.Category;
import guru.springframework.domain.Customer;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.CustomerRepository;
import guru.springframework.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Bootstrap implements CommandLineRunner {

    private CategoryRepository categoryRepository;
    private CustomerRepository customerRepository;
    private VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository,
                     CustomerRepository customerRepository,
                     VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        setUpCategories();
        setUpCustomers();
        setUpVendors();
    }

    private void setUpCategories(){
        Category fruits = new Category();
        fruits.setName("Fruits");
        Category dried = new Category();
        dried.setName("Dried");
        Category fresh = new Category();
        fresh.setName("Fresh");
        Category exotic = new Category();
        exotic.setName("Exotic");
        Category nuts = new Category();
        nuts.setName("nuts");


        categoryRepository.save(fruits);
        categoryRepository.save(dried);
        categoryRepository.save(fresh);
        categoryRepository.save(exotic);
        categoryRepository.save(nuts);


        System.out.println("Category data loaded: "+ categoryRepository.findAll());
    }

     private void setUpCustomers() {
        Customer michel = new Customer("Michel","Lachappele");
        michel.setId(1L);
        Customer david = new Customer("David","Winter");
        david.setId(2L);
        Customer anne = new Customer("Anne","Hine");
        anne.setId(3L);
        Customer alice = new Customer("Alice","Eastman");
        alice.setId(4L);

        List<Customer> customerList = Arrays.asList(michel, david, anne, alice);
        customerRepository.saveAll(customerList);

        System.out.println("Customer data loaded: "+ customerRepository.findAll());
    }

    private void setUpVendors() {
        Vendor vendor1 = new Vendor();
        vendor1.setId(1L);
        vendor1.setName("Benoly");

        Vendor vendor2 = new Vendor();
        vendor2.setId(2L);
        vendor2.setName("Benomart");

        List<Vendor> vendors = Arrays.asList(vendor1, vendor2);
        vendorRepository.saveAll(vendors);

        System.out.println("vendor data loaded: "+ vendorRepository.findAll());

    }
}
