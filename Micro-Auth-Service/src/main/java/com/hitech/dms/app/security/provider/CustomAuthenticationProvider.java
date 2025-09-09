/**
 * 
 */
package com.hitech.dms.app.security.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hitech.dms.app.security.util.AesUtil;
import com.hitech.dms.constants.AppConstants;
import com.hitech.dms.web.dao.user.UserAuthDao;
import com.hitech.dms.web.dao.user.UserDao;
import com.hitech.dms.web.entity.user.UserEntity;

/**
 * @author dinesh.jakhar
 *
 */
public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserAuthDao userAuthDao;
//	@Autowired
//	private UserDao userDao;
	@Autowired
	private MessageSource messageSource;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
//		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
//				.getRequest();

		final UserEntity user = userAuthDao.findByUser(auth.getName());
		if ((user == null)) {
			throw new BadCredentialsException("Invalid username or password");
		}
		if(!user.getIsActive()) {
			throw new BadCredentialsException("Dealer is Inactive");
		}
		final Authentication result = super.authenticate(auth);
		return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
	}

	public Collection<? extends GrantedAuthority> getAuthorities(String role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
		return authList;
	}

	public List<String> getRoles(String role) {

		List<String> roles = new ArrayList<String>();
		roles.add(role);
		return roles;
	}

	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}

	private boolean isValidLong(String code) {
		try {
			Long.parseLong(code);
		} catch (final NumberFormatException e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	private String extractPass(String pass) {
		String password = null;
		password = new String(java.util.Base64.getDecoder().decode(pass));
		AesUtil aesUtil = new AesUtil(128, 1000);
		if (password != null && password.split("::").length == 3) {
			try {
				password = aesUtil.decrypt(password.split("::")[1], password.split("::")[0], AppConstants.SECRET_KEY,
						password.split("::")[2]);
			} catch (IllegalArgumentException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return password;
	}

}
