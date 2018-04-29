package guru.springfamework.service;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.controller.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorServiceImpl implements VendorService {

    private final VendorMapper vendorMapper;
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorServiceImpl(VendorMapper vendorMapper, VendorRepository vendorRepository){
        this.vendorMapper = vendorMapper;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public List<VendorDTO> getAllVendors() {

        return vendorRepository.findAll().stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setUrl(this.getVendorUrl(vendor.getId()));
                    return vendorDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {

        Vendor savedVendor = vendorRepository.save(vendorMapper.vendorDTOToVendor(vendorDTO));
        VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        returnDTO.setUrl(this.getVendorUrl(savedVendor.getId()));
        return returnDTO;
    }

    @Override
    public VendorDTO findByID(Long id) {
        return vendorRepository.findById(id)
                .map( vendor -> {
                    VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(vendor);
                    returnDTO.setUrl(getVendorUrl(vendor.getId()));
                    return returnDTO;
                })
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public VendorDTO updateVendor(Long id, VendorDTO vendorDTO) {
        VendorDTO vendorToBeUpdated = this.findByID(id);
        vendorToBeUpdated.setName(vendorDTO.getName());
        return this.createNewVendor(vendorToBeUpdated);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        VendorDTO savedVendor = this.findByID(id);

        if(!vendorDTO.getName().equals(savedVendor.getName()) && vendorDTO.getName()!= null) {
            savedVendor.setName(vendorDTO.getName());
            return this.createNewVendor(vendorDTO);
        } else {
            return savedVendor;
        }
    }

    @Override
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }

    private String getVendorUrl(Long id){
        return VendorController.API_URL + '/' + id;
    }
}
