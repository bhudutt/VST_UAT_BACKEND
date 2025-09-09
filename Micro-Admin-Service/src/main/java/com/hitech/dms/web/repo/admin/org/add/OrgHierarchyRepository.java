package com.hitech.dms.web.repo.admin.org.add;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.web.entity.admin.org.OrgHierarchy;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrgHierarchyRepository extends JpaRepository<OrgHierarchy, Long> {
    
    // Find all active hierarchies
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE isActive = :isActive " +
           "ORDER BY org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> findByIsActiveOrderByOrgHierarchyId(@Param("isActive") String isActive);
    
    // Find root nodes (no parent)
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE parent_org_hierarchy_id IS NULL AND isActive = :isActive " +
           "ORDER BY org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> findByParentOrgHierarchyIdIsNullAndIsActiveOrderByOrgHierarchyId(@Param("isActive") String isActive);
    
    // Find children by parent ID
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE parent_org_hierarchy_id = :parentId AND isActive = :isActive " +
           "ORDER BY org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> findByParentOrgHierarchyIdAndIsActiveOrderByOrgHierarchyId(
        @Param("parentId") Long parentId, @Param("isActive") String isActive);
    
    // Find by level ID
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE level_id = :levelId AND isActive = :isActive " +
           "ORDER BY org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> findByLevelIdAndIsActiveOrderByOrgHierarchyId(
        @Param("levelId") Long levelId, @Param("isActive") String isActive);
    
    // Find by hierarchy code
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE hierarchy_code = :hierarchyCode AND isActive = :isActive LIMIT 1", 
           nativeQuery = true)
    Optional<OrgHierarchy> findByHierarchyCodeAndIsActive(
        @Param("hierarchyCode") String hierarchyCode, @Param("isActive") String isActive);
    
    // Check if hierarchy code exists (for validation)
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
           "FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE hierarchy_code = :hierarchyCode AND org_hierarchy_id != :orgHierarchyId", 
           nativeQuery = true)
    int existsByHierarchyCodeAndOrgHierarchyIdNotNative(
        @Param("hierarchyCode") String hierarchyCode, @Param("orgHierarchyId") Long orgHierarchyId);
    
    default boolean existsByHierarchyCodeAndOrgHierarchyIdNot(String hierarchyCode, Long orgHierarchyId) {
        return existsByHierarchyCodeAndOrgHierarchyIdNotNative(hierarchyCode, orgHierarchyId) > 0;
    }
    
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
           "FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE hierarchy_code = :hierarchyCode", 
           nativeQuery = true)
    int existsByHierarchyCodeNative(@Param("hierarchyCode") String hierarchyCode);
    
    default boolean existsByHierarchyCode(String hierarchyCode) {
        return existsByHierarchyCodeNative(hierarchyCode) > 0;
    }
    
    // Get complete hierarchy tree with all relationships
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE isActive = :isActive " +
           "ORDER BY level_id, org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> findAllWithRelationships(@Param("isActive") String isActive);
    
    // Get root hierarchies with their immediate children
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE parent_org_hierarchy_id IS NULL AND isActive = :isActive " +
           "ORDER BY org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> findRootHierarchiesWithChildren(@Param("isActive") String isActive);
    
    // Search hierarchies by description or code
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE (LOWER(hierarchy_desc) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(hierarchy_code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "AND isActive = 'Y' " +
           "ORDER BY level_id, org_hierarchy_id", 
           nativeQuery = true)
    List<OrgHierarchy> searchByDescriptionOrCode(@Param("searchTerm") String searchTerm);
    
    // Get hierarchy path from root to a specific node using recursive CTE
    @Query(value = "WITH RECURSIVE HierarchyPath AS ( " +
           "    SELECT org_hierarchy_id, hierarchy_code, hierarchy_desc, " +
           "           parent_org_hierarchy_id, level_id, 0 as depth " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "    WHERE org_hierarchy_id = :hierarchyId AND isActive = 'Y' " +
           "    UNION ALL " +
           "    SELECT h.org_hierarchy_id, h.hierarchy_code, h.hierarchy_desc, " +
           "           h.parent_org_hierarchy_id, h.level_id, hp.depth + 1 " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "    INNER JOIN HierarchyPath hp ON h.org_hierarchy_id = hp.parent_org_hierarchy_id " +
           "    WHERE h.isActive = 'Y' " +
           ") " +
           "SELECT org_hierarchy_id, hierarchy_code, hierarchy_desc, " +
           "       parent_org_hierarchy_id, level_id, depth " +
           "FROM HierarchyPath ORDER BY depth DESC", 
           nativeQuery = true)
    List<Object[]> getHierarchyPath(@Param("hierarchyId") Long hierarchyId);
    
    // Get all descendants of a node using recursive CTE
    @Query(value = "WITH RECURSIVE HierarchyTree AS ( " +
           "    SELECT org_hierarchy_id, hierarchy_code, hierarchy_desc, " +
           "           parent_org_hierarchy_id, level_id, 0 as depth " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "    WHERE org_hierarchy_id = :hierarchyId AND isActive = 'Y' " +
           "    UNION ALL " +
           "    SELECT h.org_hierarchy_id, h.hierarchy_code, h.hierarchy_desc, " +
           "           h.parent_org_hierarchy_id, h.level_id, ht.depth + 1 " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "    INNER JOIN HierarchyTree ht ON h.parent_org_hierarchy_id = ht.org_hierarchy_id " +
           "    WHERE h.isActive = 'Y' " +
           ") " +
           "SELECT org_hierarchy_id, hierarchy_code, hierarchy_desc, " +
           "       parent_org_hierarchy_id, level_id, depth " +
           "FROM HierarchyTree WHERE depth > 0 ORDER BY depth, org_hierarchy_id", 
           nativeQuery = true)
    List<Object[]> getHierarchyDescendants(@Param("hierarchyId") Long hierarchyId);
    
    // Soft delete hierarchy and all its children
    @Modifying
    @Transactional
    @Query(value = "WITH RECURSIVE HierarchyToDelete AS ( " +
           "    SELECT org_hierarchy_id " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "    WHERE org_hierarchy_id = :hierarchyId " +
           "    UNION ALL " +
           "    SELECT h.org_hierarchy_id " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "    INNER JOIN HierarchyToDelete htd ON h.parent_org_hierarchy_id = htd.org_hierarchy_id " +
           ") " +
           "UPDATE ADM_HO_MST_ORG_LEVEL_HIER " +
           "SET isActive = 'N', ModifiedDate = CURRENT_TIMESTAMP, ModifiedBy = :modifiedBy " +
           "WHERE org_hierarchy_id IN (SELECT org_hierarchy_id FROM HierarchyToDelete)", 
           nativeQuery = true)
    int softDeleteHierarchyAndChildren(@Param("hierarchyId") Long hierarchyId, @Param("modifiedBy") String modifiedBy);
    
    // Count direct children
    @Query(value = "SELECT COUNT(*) FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "WHERE parent_org_hierarchy_id = :parentId AND isActive = 'Y'", 
           nativeQuery = true)
    Long countDirectChildren(@Param("parentId") Long parentId);
    
    // Validate parent-child relationship to prevent circular references
    @Query(value = "WITH RECURSIVE HierarchyPath AS ( " +
           "    SELECT org_hierarchy_id, parent_org_hierarchy_id, 0 as depth " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER " +
           "    WHERE org_hierarchy_id = :childId " +
           "    UNION ALL " +
           "    SELECT h.org_hierarchy_id, h.parent_org_hierarchy_id, hp.depth + 1 " +
           "    FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "    INNER JOIN HierarchyPath hp ON h.org_hierarchy_id = hp.parent_org_hierarchy_id " +
           "    WHERE hp.depth < 50 " + // Prevent infinite loops
           ") " +
           "SELECT COUNT(*) FROM HierarchyPath WHERE org_hierarchy_id = :parentId", 
           nativeQuery = true)
    Long checkCircularReference(@Param("parentId") Long parentId, @Param("childId") Long childId);
    
    
    
 // Check for duplicate hierarchy description within same level and same profit center
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
           "FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "INNER JOIN ADM_HO_MST_ORG_LEVEL l ON h.level_id = l.level_id " +
           "WHERE LOWER(LTRIM(RTRIM(h.hierarchy_desc))) = LOWER(LTRIM(RTRIM(:hierarchyDesc))) " +
           "AND h.level_id = :levelId " +
           "AND l.pc_id = :pcId " +
           "AND h.isActive = 'Y' " +
           "AND (:excludeId IS NULL OR h.org_hierarchy_id != :excludeId)", 
           nativeQuery = true)
    int existsByHierarchyDescAndLevelAndPcIdNative(
        @Param("hierarchyDesc") String hierarchyDesc,
        @Param("levelId") Long levelId,
        @Param("pcId") Long pcId,
        @Param("excludeId") Long excludeId);

    default boolean existsByHierarchyDescAndLevelAndPcId(String hierarchyDesc, Long levelId, Long pcId, Long excludeId) {
        return existsByHierarchyDescAndLevelAndPcIdNative(hierarchyDesc, levelId, pcId, excludeId) > 0;
    }
    
 // Get profit center ID for a given hierarchy
    @Query(value = "SELECT l.pc_id " +
           "FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "INNER JOIN ADM_HO_MST_ORG_LEVEL l ON h.level_id = l.level_id " +
           "WHERE h.org_hierarchy_id = :hierarchyId " +
           "AND h.isActive = 'Y'", 
           nativeQuery = true)
    Long getProfitCenterIdByHierarchyId(@Param("hierarchyId") Long hierarchyId);
    
 // Get existing hierarchy names at same level within same profit center (for better error messages)
    @Query(value = "SELECT h.hierarchy_desc " +
           "FROM ADM_HO_MST_ORG_LEVEL_HIER h " +
           "INNER JOIN ADM_HO_MST_ORG_LEVEL l ON h.level_id = l.level_id " +
           "WHERE h.level_id = :levelId " +
           "AND l.pc_id = :pcId " +
           "AND h.isActive = 'Y' " +
           "AND LOWER(TRIM(h.hierarchy_desc)) = LOWER(TRIM(:hierarchyDesc)) " +
           "ORDER BY h.hierarchy_desc", 
           nativeQuery = true)
    List<String> findExistingHierarchyNames(
        @Param("levelId") Long levelId,
        @Param("pcId") Long pcId,
        @Param("hierarchyDesc") String hierarchyDesc);
}