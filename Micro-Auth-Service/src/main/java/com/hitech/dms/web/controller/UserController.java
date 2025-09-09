package com.hitech.dms.web.controller;

import java.util.HashSet;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.app.errors.EntityNotFoundException;
import com.hitech.dms.web.entity.user.UserCustomerEntity;
import com.hitech.dms.web.entity.user.UserEntity;
import com.hitech.dms.web.model.user.User;
import com.hitech.dms.web.repo.user.UserCustomerRepository;
import com.hitech.dms.web.repo.user.UserRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Validated
class UserController {

    private final UserRepository repository;
    private final UserCustomerRepository userProfileRepository;

    private final PasswordEncoder passwordEncoder;

    UserController(UserRepository repository, UserCustomerRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    Page<UserEntity> all(@PageableDefault(size = Integer.MAX_VALUE) Pageable pageable, OAuth2Authentication authentication) {
        String auth = (String) authentication.getUserAuthentication().getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
//        if (role.equals(User.Role.USER.name())) {
//            return repository.findAllByEmail(auth, pageable);
//        }
        return repository.findAll(pageable);
    }

    @GetMapping("/search")
    Page<UserCustomerEntity> search(@RequestParam String email, Pageable pageable, OAuth2Authentication authentication) {
        String auth = (String) authentication.getUserAuthentication().getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
//        if (role.equals(User.Role.USER.name())) {
//            return repository.findAllByEmailContainsAndEmail(email, auth, pageable);
//        }
        return userProfileRepository.findByEmailContains(email, pageable);
    }
    
//    @GetMapping("/findByUsername")
//    @PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
//    UserEntity findByUsername(@RequestParam String username, OAuth2Authentication authentication) {
//        return repository.findByUsercode(username).orElseThrow(() -> new EntityNotFoundException(UserEntity.class, "username", username));
//    }

    @GetMapping("/findByEmail")
    @PreAuthorize("!hasAuthority('USER') || (authentication.principal == #email)")
    UserCustomerEntity findByEmail(@RequestParam String email, OAuth2Authentication authentication) {
        return userProfileRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(User.class, "email", email));
    }

    @GetMapping("/{id}")
    @PostAuthorize("!hasAuthority('USER') || (returnObject != null && returnObject.email == authentication.principal)")
    UserEntity one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("!hasAuthority('USER') || (authentication.principal == @userRepository.findById(#id).orElse(new com.hitech.dms.web.entity.UserEntity()).email)")
    void update(@PathVariable Long id, @Valid @RequestBody UserEntity res) {
    	UserEntity u = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
        res.setPassword(u.getPassword());
        repository.save(res);
    }

    @PostMapping
    @PreAuthorize("!hasAuthority('USER')")
    UserEntity create(@Valid @RequestBody UserEntity res) {
        return repository.save(res);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("!hasAuthority('USER')")
    void delete(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new EntityNotFoundException(User.class, "id", id.toString());
        }
    }

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("!hasAuthority('USER') || (#oldPassword != null && !#oldPassword.isEmpty() && authentication.principal == @userRepository.findById(#id).orElse(new com.hitech.dms.model.user.User()).email)")
    void changePassword(@PathVariable Long id, @RequestParam(required = false) String oldPassword, @Valid @Size(min = 3) @RequestParam String newPassword) {
    	UserEntity user = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(User.class, "id", id.toString()));
        if (oldPassword == null || oldPassword.isEmpty() || passwordEncoder.matches(oldPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
        } else {
            throw new ConstraintViolationException("old password doesn't match", new HashSet<>());
        }
    }
}
