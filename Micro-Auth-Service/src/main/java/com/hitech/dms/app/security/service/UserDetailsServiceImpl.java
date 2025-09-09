package com.hitech.dms.app.security.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hitech.dms.app.security.session.LoginAttemptService;
import com.hitech.dms.constants.AppConstants;
import com.hitech.dms.web.dao.user.UserAuthDao;
import com.hitech.dms.web.entity.user.UserEntity;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LoginAttemptService loginAttemptService;
	
	private UserAuthDao userAuthDao;
    
    @Autowired
    public UserDetailsServiceImpl(@Lazy UserAuthDao userAuthDao) {
        this.userAuthDao = userAuthDao;
    }

	@Autowired
	private HttpServletRequest request;

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

		UserEntity user = null;

		final String ip = getClientIP();
		if (loginAttemptService.isBlocked(ip)) {
			throw new RuntimeException("blocked");
		}
		user = userAuthDao.findByUser(userName);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", userName));
		}
		return user;
	}

	public Collection<? extends GrantedAuthority> getAuthorities(String role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}

	public List<String> getRoles(String role) {

		List<String> roles = new ArrayList<String>();

		if (role != null && role.equals(AppConstants.ACTUATOR_ADMIN)) {
			roles.add(AppConstants.ACTUATOR_ADMIN);
		}
		return roles;
	}

	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	private String getClientIP() {
		final String xfHeader = request.getHeader("X-Forwarded-For");
		if (xfHeader == null) {
			return request.getRemoteAddr();
		}
		return xfHeader.split(",")[0];
	}
}
