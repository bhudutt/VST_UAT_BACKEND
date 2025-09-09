package com.hitech.dms.web.repo.admin.org.add;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hitech.dms.web.entity.admin.org.OrgLevel;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

@Repository
public interface OrgLevelRepository extends JpaRepository<OrgLevel, Long> {
    
    // Find all active levels ordered by sequence
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE isActive = :isActive " +
           "ORDER BY seq_no", 
           nativeQuery = true)
    List<OrgLevel> findByIsActiveOrderBySeqNo(@Param("isActive") String isActive);
    
    // Find by PC ID and Department ID
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE pc_id = :pcId AND department_id = :departmentId AND isActive = :isActive " +
           "ORDER BY seq_no", 
           nativeQuery = true)
    List<OrgLevel> findByPcIdAndDepartmentIdAndIsActiveOrderBySeqNo(
        @Param("pcId") Long pcId, 
        @Param("departmentId") Long departmentId, 
        @Param("isActive") String isActive);
    
    // Find by level code
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE level_code = :levelCode AND isActive = :isActive LIMIT 1", 
           nativeQuery = true)
    Optional<OrgLevel> findByLevelCodeAndIsActive(
        @Param("levelCode") String levelCode, 
        @Param("isActive") String isActive);
    
    // Check if level code exists (for validation)
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
           "FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE level_code = :levelCode AND level_id != :levelId", 
           nativeQuery = true)
    int existsByLevelCodeAndLevelIdNotNative(
        @Param("levelCode") String levelCode, 
        @Param("levelId") Long levelId);
    
    default boolean existsByLevelCodeAndLevelIdNot(String levelCode, Long levelId) {
        return existsByLevelCodeAndLevelIdNotNative(levelCode, levelId) > 0;
    }
    
    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END " +
           "FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE level_code = :levelCode", 
           nativeQuery = true)
    int existsByLevelCodeNative(@Param("levelCode") String levelCode);
    
    default boolean existsByLevelCode(String levelCode) {
        return existsByLevelCodeNative(levelCode) > 0;
    }
    
    // Find by level description
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE LOWER(level_desc) LIKE LOWER(CONCAT('%', :levelDesc, '%')) " +
           "AND isActive = :isActive " +
           "ORDER BY seq_no", 
           nativeQuery = true)
    List<OrgLevel> findByLevelDescContainingIgnoreCaseAndIsActive(
        @Param("levelDesc") String levelDesc, 
        @Param("isActive") String isActive);
    
    // Custom query to get levels with hierarchy count
    @Query(value = "SELECT ol.level_id, ol.pc_id, ol.department_id, ol.level_code, " +
           "       ol.level_desc, ol.seq_no, ol.isActive, ol.CreatedDate, " +
           "       ol.CreatedBy, ol.ModifiedDate, ol.ModifiedBy, " +
           "       COALESCE(COUNT(oh.org_hierarchy_id), 0) as hierarchyCount " +
           "FROM ADM_HO_MST_ORG_LEVEL ol " +
           "LEFT JOIN ADM_HO_MST_ORG_LEVEL_HIER oh ON ol.level_id = oh.level_id AND oh.isActive = 'Y' " +
           "WHERE ol.isActive = :isActive " +
           "GROUP BY ol.level_id, ol.pc_id, ol.department_id, ol.level_code, " +
           "         ol.level_desc, ol.seq_no, ol.isActive, ol.CreatedDate, " +
           "         ol.CreatedBy, ol.ModifiedDate, ol.ModifiedBy " +
           "ORDER BY ol.seq_no", 
           nativeQuery = true)
    List<Object[]> findLevelsWithHierarchyCount(@Param("isActive") String isActive);
    
    // Find levels by PC and Department with custom query
    @Query(value = "SELECT * FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE pc_id = :pcId AND department_id = :departmentId AND isActive = 'Y' " +
           "ORDER BY seq_no", 
           nativeQuery = true)
    List<OrgLevel> findActiveLevelsByPcAndDepartment(
        @Param("pcId") Long pcId, 
        @Param("departmentId") Long departmentId);
    
    // Get next sequence number
    @Query(value = "SELECT COALESCE(MAX(seq_no), 0) + 1 " +
           "FROM ADM_HO_MST_ORG_LEVEL " +
           "WHERE pc_id = :pcId AND department_id = :departmentId", 
           nativeQuery = true)
    Integer getNextSequenceNumber(
        @Param("pcId") Long pcId, 
        @Param("departmentId") Long departmentId);
}
