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

import com.hitech.dms.web.entity.admin.org.ApiResponse;
import com.hitech.dms.web.entity.admin.org.OrgLevel;
import com.hitech.dms.web.service.admin.org.add.OrgHierarchyService;
import com.hitech.dms.web.service.admin.org.add.OrgLevelService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/admin/level")
@SecurityRequirement(name = "hitechApis")
public class OrgLevelController {
	
	 @Autowired
	    private OrgLevelService orgLevelService;
	    
	    @GetMapping
	    public ResponseEntity<List<OrgLevel>> getAllLevels() {
	        List<OrgLevel> levels = orgLevelService.getAllActiveLevels();
	        return ResponseEntity.ok(levels);
	    }
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<OrgLevel> getLevelById(@PathVariable Long id) {
	        Optional<OrgLevel> level = orgLevelService.getLevelById(id);
	        return level.map(ResponseEntity::ok)
	                   .orElse(ResponseEntity.notFound().build());
	    }
	    
	    @GetMapping("/by-code/{code}")
	    public ResponseEntity<OrgLevel> getLevelByCode(@PathVariable String code) {
	        Optional<OrgLevel> level = orgLevelService.getLevelByCode(code);
	        return level.map(ResponseEntity::ok)
	                   .orElse(ResponseEntity.notFound().build());
	    }
	    
	    @GetMapping("/by-pc-dept")
	    public ResponseEntity<List<OrgLevel>> getLevelsByPcAndDepartment(
	            @RequestParam Long pcId, 
	            @RequestParam Long departmentId) {
	        List<OrgLevel> levels = orgLevelService.getLevelsByPcAndDepartment(pcId, departmentId);
	        return ResponseEntity.ok(levels);
	    }
	    
	    @GetMapping("/search")
	    public ResponseEntity<List<OrgLevel>> searchLevelsByDescription(
	            @RequestParam @NotBlank String description) {
	        List<OrgLevel> levels = orgLevelService.searchLevelsByDescription(description);
	        return ResponseEntity.ok(levels);
	    }
	    
	    @GetMapping("/with-hierarchy-count")
	    public ResponseEntity<List<Object[]>> getLevelsWithHierarchyCount() {
	        List<Object[]> levelsWithCount = orgLevelService.getLevelsWithHierarchyCount();
	        return ResponseEntity.ok(levelsWithCount);
	    }
	    
	    @PostMapping
	    public ResponseEntity<ApiResponse<OrgLevel>> createLevel(@Valid @RequestBody OrgLevel orgLevel) {
	        try {
	            OrgLevel created = orgLevelService.createLevel(orgLevel);
	            ApiResponse<OrgLevel> response = new ApiResponse<>(
	                true, "Organization level created successfully", created);
	            return ResponseEntity.status(HttpStatus.CREATED).body(response);
	        } catch (Exception e) {
	            ApiResponse<OrgLevel> response = new ApiResponse<>(
	                false, "Failed to create organization level: " + e.getMessage(), null);
	            return ResponseEntity.badRequest().body(response);
	        }
	    }
	    
	    @PutMapping("/{id}")
	    public ResponseEntity<ApiResponse<OrgLevel>> updateLevel(
	            @PathVariable Long id, 
	            @Valid @RequestBody OrgLevel orgLevel) {
	        try {
	            OrgLevel updated = orgLevelService.updateLevel(id, orgLevel);
	            ApiResponse<OrgLevel> response = new ApiResponse<>(
	                true, "Organization level updated successfully", updated);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            ApiResponse<OrgLevel> response = new ApiResponse<>(
	                false, "Failed to update organization level: " + e.getMessage(), null);
	            return ResponseEntity.badRequest().body(response);
	        }
	    }
	    
	    @DeleteMapping("/{id}")
	    public ResponseEntity<ApiResponse<Void>> deleteLevel(
	            @PathVariable Long id, 
	            @RequestParam(defaultValue = "system") String modifiedBy) {
	        try {
	            orgLevelService.deleteLevel(id, modifiedBy);
	            ApiResponse<Void> response = new ApiResponse<>(
	                true, "Organization level deleted successfully", null);
	            return ResponseEntity.ok(response);
	        } catch (Exception e) {
	            ApiResponse<Void> response = new ApiResponse<>(
	                false, "Failed to delete organization level: " + e.getMessage(), null);
	            return ResponseEntity.badRequest().body(response);
	        }
	    }
	    
	    @GetMapping("/exists/{code}")
	    public ResponseEntity<Boolean> checkLevelCodeExists(@PathVariable String code) {
	        boolean exists = orgLevelService.existsByLevelCode(code);
	        return ResponseEntity.ok(exists);
	    }

}
