package com.hitech.dms.app.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.entity.user.UserTypeEntity;
import com.hitech.dms.web.repo.user.UserRepository;

//@Service
//@Transactional
public class MyUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	@Autowired
	public MyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		UserEntity user = userRepository.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException("Could not find Username");
		List<UserTypeEntity> roleList = new ArrayList<UserTypeEntity>(1);
		roleList.add(user.getUserType());
//		return new MyUserDetails(user, getGrantedAuthorities(roleList));
		return user;
	}

	private List<GrantedAuthority> getGrantedAuthorities(Collection<UserTypeEntity> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (UserTypeEntity role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getUserType()));
		}
		return authorities;
	}
}
