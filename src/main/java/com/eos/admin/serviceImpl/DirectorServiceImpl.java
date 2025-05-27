package com.eos.admin.serviceImpl;

import org.springframework.stereotype.Service;

import com.eos.admin.dto.DirectorDTO;
import com.eos.admin.entity.Director;
import com.eos.admin.repository.DirectorRepository;
import com.eos.admin.service.DirectorService;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DirectorServiceImpl implements DirectorService {

    private final DirectorRepository directorRepository;
    private final ModelMapper modelMapper;

    public DirectorServiceImpl(DirectorRepository directorRepository, ModelMapper modelMapper) {
        this.directorRepository = directorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DirectorDTO saveDirector(DirectorDTO directorDto) {
        try {
            Director director = modelMapper.map(directorDto, Director.class);
            Director savedDirector = directorRepository.save(director);
            return modelMapper.map(savedDirector, DirectorDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while saving Director: " + e.getMessage(), e);
        }
    }

    @Override
    public DirectorDTO getDirectorById(Long id) {
        try {
            Director director = directorRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Director not found with id: " + id));
            return modelMapper.map(director, DirectorDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching Director by id: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DirectorDTO> getAllDirectors() {
        try {
            List<Director> directors = directorRepository.findAll();
            return directors.stream()
                    .map(director -> modelMapper.map(director, DirectorDTO.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching all Directors: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteDirector(Long id) {
        try {
            directorRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while deleting Director with id: " + id + " - " + e.getMessage(), e);
        }
    }
}
