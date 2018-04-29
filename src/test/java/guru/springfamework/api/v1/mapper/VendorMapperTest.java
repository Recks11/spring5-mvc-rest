package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class VendorMapperTest {

    Vendor initialVendor;

    VendorDTO initialVendorDTO;

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        initialVendor = new Vendor();
        initialVendor.setId(1L);
        initialVendor.setName("Benoly");
        initialVendorDTO = new VendorDTO();
//        initialVendorDTO.setId(2L);
        initialVendorDTO.setName("Benomart");
    }

    @Test
    public void vendorToVendorDTO() {
        VendorDTO convertedVendorDTO = vendorMapper.vendorToVendorDTO(initialVendor);

//        assertEquals(convertedVendorDTO.getId(), initialVendor.getId());
        assertEquals(convertedVendorDTO.getName(), initialVendor.getName());
    }

    @Test
    public void vendorDTOToVendor() {

        Vendor convertedVendor = vendorMapper.vendorDTOToVendor(initialVendorDTO);

//        assertEquals(convertedVendor.getId(), initialVendorDTO.getId());
        assertEquals(convertedVendor.getName(), initialVendorDTO.getName());
    }
}