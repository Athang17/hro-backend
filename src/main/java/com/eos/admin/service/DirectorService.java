package com.eos.admin.service;

import java.util.List;

import com.eos.admin.dto.DirectorDTO;

public interface DirectorService {
    DirectorDTO saveDirector(DirectorDTO directorDto);
    DirectorDTO getDirectorById(Long id);
    List<DirectorDTO> getAllDirectors();
    void deleteDirector(Long id);
}
