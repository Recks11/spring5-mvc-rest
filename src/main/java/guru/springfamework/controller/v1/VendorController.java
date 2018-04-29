package guru.springfamework.controller.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(VendorController.API_URL)
public class VendorController {

    public static final String API_URL = "/api/v1/vendors";
    private final VendorService vendorService;

    @Autowired
    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors() {
        return new VendorListDTO(vendorService.getAllVendors());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.createNewVendor(vendorDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findByID(id);
    }

    @PutMapping("/{id}")
    public VendorDTO updateVendorByPut(@PathVariable("id") Long id, @RequestBody VendorDTO vendorDTO) {

        return vendorService.updateVendor(id, vendorDTO);
    }

    @PatchMapping("/{id}")
    public VendorDTO updateVendorByPatch(@PathVariable("id") Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.patchVendor(id, vendorDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendorById(@PathVariable("id") Long id) {
        vendorService.deleteVendor(id);
    }
}
