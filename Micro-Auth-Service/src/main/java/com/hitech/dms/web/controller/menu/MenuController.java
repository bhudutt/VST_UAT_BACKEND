/**
 * 
 */
package com.hitech.dms.web.controller.menu;

import java.util.List;

import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.errors.EntityNotFoundException;
import com.hitech.dms.app.security.util.MenuUtil;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.model.user.AppMenuModel;
import com.hitech.dms.web.model.user.UserMenu;
import com.hitech.dms.web.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dinesh.jakhar
 *
 */
@RestController
@RequestMapping("/api/menus")
@Slf4j
public class MenuController {
	private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private Mapper mapper;

	@Autowired
	private UserService userService;

	@GetMapping("/appMenuList")
//    @PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
	public ResponseEntity<?> findByUsername(OAuth2Authentication authentication) {
		String userCode = null;
		UserEntity user = null;
		if (authentication != null) {
			user = (UserEntity) authentication.getUserAuthentication().getPrincipal();
			userCode = user.getUsername();
		}
		List<AppMenuModel> userMenuList = userService.getUserMenu(userCode);
		if (userMenuList == null) {
			throw new EntityNotFoundException(UserMenu.class, "UserMenu", userCode);
		}
		List<AppMenuModel> finalMenuList = MenuUtil.getHierarchicalList(userMenuList);
		return ResponseEntity.ok(finalMenuList);
	}

	
}
