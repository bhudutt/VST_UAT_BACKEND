package com.hitech.dms.web.controller.admin.org.add;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hitech.dms.web.dao.admin.role.create.AdminRoleCreateDaoImpl;
import com.hitech.dms.web.dao.admin.role.create.OrgHirarchyImpl;
import com.hitech.dms.web.entity.admin.org.ApiResponse;
import com.hitech.dms.web.entity.admin.org.BusinessValidationException;
import com.hitech.dms.web.entity.admin.org.DuplicateResourceException;
import com.hitech.dms.web.entity.admin.org.OrgHierarchy;
import com.hitech.dms.web.entity.admin.org.OrgHierarchyDto;
import com.hitech.dms.web.entity.admin.org.ResourceNotFoundException;
import com.hitech.dms.web.service.admin.org.add.OrgHierarchyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/admin/hierarchy")
//@SecurityRequirement(name = "hitechApis")
public class OrgHierarchyController {
	
	@Autowired
    private OrgHierarchyService orgHierarchyService;
	
	@Autowired
	private OrgHirarchyImpl orgHirarchyImpl;
    
    @GetMapping
    @Operation(summary = "Get all active hierarchies", description = "Retrieve all active organization hierarchies")
    public ResponseEntity<List<OrgHierarchy>> getAllHierarchies() {
        List<OrgHierarchy> hierarchies = orgHierarchyService.getAllActiveHierarchies();
        return ResponseEntity.ok(hierarchies);
    }
    
    @GetMapping("/roots")
    @Operation(summary = "Get root hierarchies", description = "Retrieve all root level hierarchies")
    public ResponseEntity<List<OrgHierarchy>> getRootHierarchies() {
        List<OrgHierarchy> roots = orgHierarchyService.getRootHierarchies();
        return ResponseEntity.ok(roots);
    }
    
    @GetMapping("/tree")
    @Operation(summary = "Get complete hierarchy tree", description = "Retrieve complete organization hierarchy tree with children")
    public ResponseEntity<List<OrgHierarchyDto>> getCompleteTree() {
        List<OrgHierarchyDto> tree = orgHierarchyService.getCompleteHierarchyTreeDto();
        return ResponseEntity.ok(tree);
    }
    
    @GetMapping("/roots-with-children")
    @Operation(summary = "Get root hierarchies with children", description = "Retrieve root hierarchies with immediate children")
    public ResponseEntity<List<OrgHierarchy>> getRootHierarchiesWithChildren() {
        List<OrgHierarchy> rootsWithChildren = orgHierarchyService.getRootHierarchiesWithChildren();
        return ResponseEntity.ok(rootsWithChildren);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get hierarchy by ID", description = "Retrieve a specific hierarchy by its ID")
    public ResponseEntity<OrgHierarchy> getHierarchyById(@PathVariable Long id) {
        Optional<OrgHierarchy> hierarchy = orgHierarchyService.getHierarchyById(id);
        return hierarchy.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/{id}/children")
    @Operation(summary = "Get children of hierarchy", description = "Retrieve all direct children of a hierarchy")
    public ResponseEntity<List<OrgHierarchy>> getChildren(@PathVariable Long id) {
        List<OrgHierarchy> children = orgHierarchyService.getChildrenByParentId(id);
        return ResponseEntity.ok(children);
    }
    
    @GetMapping("/{id}/children-count")
    @Operation(summary = "Get children count", description = "Get count of direct children for a hierarchy")
    public ResponseEntity<Long> getChildrenCount(@PathVariable Long id) {
        Long count = orgHierarchyService.getChildrenCount(id);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/by-level/{levelId}")
    @Operation(summary = "Get hierarchies by level", description = "Retrieve all hierarchies for a specific level")
    public ResponseEntity<List<OrgHierarchy>> getHierarchiesByLevel(@PathVariable Long levelId) {
        List<OrgHierarchy> hierarchies = orgHierarchyService.getHierarchiesByLevel(levelId);
        return ResponseEntity.ok(hierarchies);
    }
    
    @GetMapping("/by-code/{code}")
    @Operation(summary = "Get hierarchy by code", description = "Retrieve hierarchy by its unique code")
    public ResponseEntity<OrgHierarchy> getHierarchyByCode(@PathVariable String code) {
        Optional<OrgHierarchy> hierarchy = orgHierarchyService.getHierarchyByCode(code);
        return hierarchy.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search hierarchies", description = "Search hierarchies by description or code")
    public ResponseEntity<List<OrgHierarchy>> searchHierarchies(
            @RequestParam @NotBlank String term) {
        List<OrgHierarchy> hierarchies = orgHierarchyService.searchHierarchies(term);
        return ResponseEntity.ok(hierarchies);
    }
    
    @GetMapping("/{id}/path")
    @Operation(summary = "Get hierarchy path", description = "Get path from root to specific hierarchy")
    public ResponseEntity<List<Object[]>> getHierarchyPath(@PathVariable Long id) {
        List<Object[]> path = orgHierarchyService.getHierarchyPath(id);
        return ResponseEntity.ok(path);
    }
    
    @GetMapping("/{id}/descendants")
    @Operation(summary = "Get hierarchy descendants", description = "Get all descendants of a hierarchy")
    public ResponseEntity<List<Object[]>> getHierarchyDescendants(@PathVariable Long id) {
        List<Object[]> descendants = orgHierarchyService.getHierarchyDescendants(id);
        return ResponseEntity.ok(descendants);
    }
    
    @PostMapping("/create")
    @Operation(summary = "Create hierarchy", description = "Create a new organization hierarchy")
    public ResponseEntity<ApiResponse<OrgHierarchy>> createHierarchy(
            @Valid @RequestBody OrgHierarchy hierarchy) {
        try {
            OrgHierarchy created = orgHirarchyImpl.createHierarchy(hierarchy);
            ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                true, "Hierarchy created successfully", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (DuplicateResourceException e) {
            ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                    false, e.getMessage(), null);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                
            } catch (BusinessValidationException e) {
                ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                    false, e.getMessage(), null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                
            } catch (ResourceNotFoundException e) {
                ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                    false, e.getMessage(), null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                
            } catch (Exception e) {
                ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                    false, "Failed to create hierarchy: " + e.getMessage(), null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update hierarchy", description = "Update an existing organization hierarchy")
    public ResponseEntity<ApiResponse<OrgHierarchy>> updateHierarchy(
            @PathVariable Long id, 
            @Valid @RequestBody OrgHierarchy hierarchy) {
        try {
            OrgHierarchy updated = orgHierarchyService.updateHierarchy(id, hierarchy);
            ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                true, "Hierarchy updated successfully", updated);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<OrgHierarchy> response = new ApiResponse<>(
                false, "Failed to update hierarchy: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
   
    @GetMapping("/deleteOrgHirarchy")
    @Operation(summary = "Delete hierarchy", description = "Soft delete a hierarchy (without children)")
    public ResponseEntity<ApiResponse<Void>> deleteHierarchy(
    		@RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "system") String modifiedBy) {
        try {
        	orgHirarchyImpl.deleteHierarchy(id, modifiedBy);
            ApiResponse<Void> response = new ApiResponse<>(
                true, "Hierarchy deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                false, "Failed to delete hierarchy: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/deleteOrgHirarchy/with-children")
    @Operation(summary = "Delete hierarchy with children", description = "Soft delete a hierarchy and all its children")
    public ResponseEntity<ApiResponse<Void>> deleteHierarchyWithChildren(
    		@RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "system") String modifiedBy) {
        try {
        	orgHirarchyImpl.deleteHierarchyWithChildren(id, modifiedBy);
            ApiResponse<Void> response = new ApiResponse<>(
                true, "Hierarchy and children deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = new ApiResponse<>(
                false, "Failed to delete hierarchy with children: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/exists/{code}")
    @Operation(summary = "Check code exists", description = "Check if hierarchy code already exists")
    public ResponseEntity<Boolean> checkHierarchyCodeExists(@PathVariable String code) {
        boolean exists = orgHierarchyService.existsByHierarchyCode(code);
        return ResponseEntity.ok(exists);
    }
}
