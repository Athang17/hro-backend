package com.eos.admin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eos.admin.entity.VendorEntity;

public interface VendorRepository extends JpaRepository<VendorEntity, Long> {

	Optional<VendorEntity> findByEmail(String email);

}
