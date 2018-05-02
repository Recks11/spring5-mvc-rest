package guru.springframework.service;

import guru.springframework.api.v1.mapper.VendorMapper;
import guru.springframework.api.v1.model.VendorDTO;
import guru.springframework.controller.v1.VendorController;
import guru.springframework.domain.Vendor;
import guru.springframework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VendorServiceImplTest {

    @Mock
    VendorRepository vendorRepository;

    VendorService vendorService;
    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vendorService = new VendorServiceImpl(vendorMapper, vendorRepository);
    }

    @Test
    public void getAllVendors() {
        Vendor vendor1 = new Vendor();
        Vendor vendor2 = new Vendor();

        List<Vendor> allVendors = Arrays.asList(vendor1, vendor2);

        when(vendorRepository.findAll()).thenReturn(allVendors);
        assertEquals(vendorService.getAllVendors().size(),2);
    }

    @Test
    public void createNewVendor() {
        Vendor newVendor = new Vendor();
        newVendor.setId(1L);
        newVendor.setName("Benoly");

        VendorDTO tempVendorDTO = vendorMapper.vendorToVendorDTO(newVendor);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(newVendor);
        VendorDTO returnDTO = vendorService.createNewVendor(tempVendorDTO);

//        assertEquals(newVendor.getId(), returnDTO.getId());
        assertEquals(newVendor.getName(), returnDTO.getName());
        assertEquals(VendorController.API_URL+'/'+newVendor.getId(), returnDTO.getUrl());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByID() {
        Vendor newVendor = new Vendor();
        newVendor.setId(1L);
        newVendor.setName("Benoly");

        when(vendorRepository.findById(1L)).thenReturn(Optional.of(newVendor));
        willThrow(new ResourceNotFoundException()).given(vendorRepository).findById(404L);

        VendorDTO returnDTO = vendorService.findByID(1L);

//        assertEquals(newVendor.getId(), returnDTO.getId());
        assertEquals(newVendor.getName(), returnDTO.getName());
        assertEquals(VendorController.API_URL+'/'+newVendor.getId(), returnDTO.getUrl());

        vendorService.findByID(404L);
    }

    @Test
    public void updateVendor() {
        Vendor initialVendor = new Vendor();
        initialVendor.setId(1L);
        initialVendor.setName("Benomart");

        Vendor updatedVendor = new Vendor();
        updatedVendor.setId(1L);
        updatedVendor.setName("updated");

        VendorDTO updateDTO = vendorMapper.vendorToVendorDTO(updatedVendor);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(initialVendor));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(updatedVendor);

        VendorDTO returnDTO = vendorService.updateVendor(1L, updateDTO);

//        assertEquals(initialVendor.getId(), returnDTO.getId());
        assertEquals(updatedVendor.getName(), returnDTO.getName());
        assertEquals(VendorController.API_URL+'/'+initialVendor.getId(), returnDTO.getUrl());
    }

    @Test
    public void patchVenDor() {
        //given
        Vendor sameVendor = new Vendor();
        sameVendor.setId(1L);
        sameVendor.setName("Benomart");

        VendorDTO sameDTO = vendorMapper.vendorToVendorDTO(sameVendor);
        when(vendorRepository.findById(1L)).thenReturn(Optional.of(sameVendor));

        //testing the object returns itself if it is unchanged
        VendorDTO unchangedDTO = vendorService.patchVendor(1L, sameDTO);

        verify(vendorRepository, times(0)).save(any(Vendor.class));
        assertEquals(unchangedDTO.getName(), sameDTO.getName());



        Vendor savedVendor = new Vendor();
        savedVendor.setId(2L);
        savedVendor.setName("Benomart");

        VendorDTO differentDTO = new VendorDTO();
//        differentDTO.setId(2L);
        differentDTO.setName("Benoly");

        when(vendorRepository.findById(2L)).thenReturn(Optional.of(savedVendor));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(vendorMapper.vendorDTOToVendor(differentDTO));

        VendorDTO patchedVendorDTO = vendorService.patchVendor(2L, differentDTO);
        //testing save is called when saved object and updated object are different
        verify(vendorRepository, times(1)).save(any(Vendor.class));

        assertNotEquals(savedVendor.getName(), differentDTO.getName());
        assertEquals(differentDTO.getName(), patchedVendorDTO.getName());
    }

    @Test
    public void deleteVendor() {
        vendorService.deleteVendor(anyLong());
        verify(vendorRepository, times(1)).deleteById(anyLong());
    }
}