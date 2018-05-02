package guru.springfamework.controller.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.service.VendorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(description = "This is the Vendors API")
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
    @ApiOperation(value = "Gets All Vendors")
    @ResponseStatus(HttpStatus.OK)
    public VendorListDTO getAllVendors() {
        return new VendorListDTO(vendorService.getAllVendors());
    }

    @PostMapping
    @ApiOperation(value = "Creates a vendor")
    @ResponseStatus(HttpStatus.CREATED)
    public VendorDTO createVendor(@RequestBody VendorDTO vendorDTO) {
        return vendorService.createNewVendor(vendorDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get a single vendor using the ID")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO getVendorById(@PathVariable Long id) {
        return vendorService.findByID(id);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update a vendor")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendorByPut(@PathVariable("id") Long id, @RequestBody VendorDTO vendorDTO) {

        return vendorService.updateVendor(id, vendorDTO);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Patch a vendor")
    @ResponseStatus(HttpStatus.OK)
    public VendorDTO updateVendorByPatch(@PathVariable("id") Long id, @RequestBody VendorDTO vendorDTO) {
        return vendorService.patchVendor(id, vendorDTO);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a vendor", notes = "Once deleted a http status 200 is returned")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVendorById(@PathVariable("id") Long id) {
        vendorService.deleteVendor(id);
    }
}
