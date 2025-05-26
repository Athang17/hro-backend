package com.eos.admin.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eos.admin.repository.VendorRepository;

@Service
public class VendorDetailsServiceImpl implements UserDetailsService{
	
	@Autowired
	private VendorRepository vendorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return vendorRepository.findByEmail(username)
				.orElseThrow(() ->new UsernameNotFoundException("Vendor not Found with email: " + username));
	}

}
