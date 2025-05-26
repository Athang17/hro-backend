package com.eos.admin.serviceImpl;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.VendorReqRep;
import com.eos.admin.entity.VendorEntity;
import com.eos.admin.jwt.JWTUtilsImpl;
import com.eos.admin.repository.VendorRepository;

@Service
public class VendorManagementServiceImpl {


	@Autowired
	private VendorRepository vendorRepository;
	
	@Autowired
	private PasswordEncoder passwordEncode;

//	@Autowired
//	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTUtilsImpl jwtUtilsImpl;
	
	@Autowired
	@Qualifier("vendorAuthenticationManager")
	private AuthenticationManager vendorAuthenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public VendorReqRep register(VendorReqRep registrationRequest) {
		VendorReqRep resp = new VendorReqRep();
		try {
			// checking user having unique id
			String email = registrationRequest.getEmail();
			if (email != null && !email.isEmpty()) {
				Optional<VendorEntity> existingUser = vendorRepository.findByEmail(email);

				if (existingUser.isPresent()) {
					resp.setStatusCode(400);
					resp.setError("Vendor id is already in use");
					return resp;
				}
			} else {
				resp.setStatusCode(400);
				resp.setError("Vendor id cann't be empty");
			}

			VendorEntity ourUsers = new VendorEntity();
			ourUsers.setEmail(email);
			ourUsers.setVendorAddress(registrationRequest.getVendorAddress());
			ourUsers.setRole(registrationRequest.getRole());
			ourUsers.setVendorName((registrationRequest.getVendorName()));
			ourUsers.setVendorMobile(registrationRequest.getVendorMobile());
			
			
			ourUsers.setPassword(passwordEncode.encode(registrationRequest.getPassword()));

			VendorEntity ourUsersResult = vendorRepository.save(ourUsers);
			if (ourUsersResult.getId() > 0) {
				resp.setMessage("User save sucessfully");
				resp.setStatusCode(200);
			}
		} catch (Exception e) {
			resp.setStatusCode(500);
			resp.setError("An error occurred: " + e.getMessage());
		}
		return resp;
	}

	public VendorReqRep login(VendorReqRep loginRequest) {
		VendorReqRep response = new VendorReqRep();
		try {
			
			vendorAuthenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
			var user = vendorRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
			var jwt = jwtUtilsImpl.generateToken(user);
			var refreshToken = jwtUtilsImpl.generateRefreshToken(new HashMap<>(), user);
			response.setStatusCode(200);
			response.setToken(jwt);
			response.setRole(user.getRole());
			response.setVendorName(user.getVendorName());
			response.setEmail(user.getEmail());
			response.setRefreshToken(refreshToken);
			response.setExpirationTime("24Hrs");
			response.setMessage("SuccessFull login ");
		} catch (Exception e) {
			response.setStatusCode(500);
			response.setMessage("Incorrect Details");
		}
		return response;
	}



}
