package guru.springframework.service;

import guru.springframework.api.v1.model.VendorDTO;

import java.util.List;

public interface VendorService {

    List<VendorDTO> getAllVendors();

    VendorDTO createNewVendor(VendorDTO vendorDTO);

    VendorDTO findByID(Long id);

    VendorDTO updateVendor(Long id, VendorDTO vendorDTO);

    VendorDTO patchVendor(Long id, VendorDTO vendorDTO);

    void deleteVendor(Long id);

}
