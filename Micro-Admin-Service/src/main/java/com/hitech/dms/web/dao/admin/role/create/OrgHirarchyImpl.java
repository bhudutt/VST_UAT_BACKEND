package com.hitech.dms.web.dao.admin.role.create;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.dms.constants.WebConstants;
import com.hitech.dms.web.entity.admin.org.BusinessValidationException;
import com.hitech.dms.web.entity.admin.org.DuplicateResourceException;
import com.hitech.dms.web.entity.admin.org.OrgHierarchy;
import com.hitech.dms.web.entity.admin.org.OrgLevel;
import com.hitech.dms.web.entity.admin.org.ResourceNotFoundException;
import com.hitech.dms.web.entity.user.RoleEntity;
import com.hitech.dms.web.entity.user.RoleMenuEntity;
import com.hitech.dms.web.model.admin.role.create.response.AdminRoleCreateResponseModel;
import com.hitech.dms.web.model.admin.role.create.resquest.AdminRoleFunctionRequestModel;
import com.hitech.dms.web.model.admin.role.create.resquest.RoleMenuRequestModel;
import com.hitech.dms.web.repo.admin.org.add.OrgHierarchyRepository;
import com.hitech.dms.web.repo.admin.org.add.OrgLevelRepository;

@Repository
@Transactional
public class OrgHirarchyImpl implements OrgHirarchy {
	
	private static final Logger logger = LoggerFactory.getLogger(OrgHirarchyImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
    private OrgHierarchyRepository orgHierarchyRepository;
	
	@Autowired
    private OrgLevelRepository orgLevelRepository;
	
	public void deleteHierarchyWithChildren(Long id,String modifiedBy) {
		Session session = null;
		Transaction transaction = null;
		try {
		    session = sessionFactory.openSession();
		    transaction = session.beginTransaction();
		    
		    // SQL Server syntax for recursive CTE
		    String sql = "WITH HierarchyToDelete AS ( " +
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
		                 "WHERE org_hierarchy_id IN (SELECT org_hierarchy_id FROM HierarchyToDelete)";
		    
		    Query query = session.createSQLQuery(sql);
		    query.setParameter("hierarchyId", id);
		    query.setParameter("modifiedBy", modifiedBy);
		    int updatedRowCount = query.executeUpdate();
		    transaction.commit();
		    
		} catch (Exception ex) {
		    if (transaction != null) {
		        transaction.rollback();
		    }
		    logger.error(this.getClass().getName(), ex);
		} finally {
		    if (session != null) {
		        session.close();
		    }
		}
	}
	
	public void deleteHierarchy(Long id,String modifiedBy) {
		Session session = null;
		Transaction transaction = null;
		try {
			
			
			OrgHierarchy hierarchy = getHierarchyByIdOrThrow(id);
			Long childrenCount = orgHierarchyRepository.countDirectChildren(id);
			if (childrenCount > 0) {
	            throw new BusinessValidationException("Cannot delete hierarchy with active children. Found " + childrenCount + " active children.");
	        }else {
	        	session = sessionFactory.openSession();
				transaction = session.beginTransaction();
				hierarchy.setIsActive("N");
		        hierarchy.setModifiedDate(LocalDateTime.now());
		        hierarchy.setModifiedBy(modifiedBy);
		        session.merge(hierarchy);
		        transaction.commit();
	        }
			
			
		}catch (SQLGrammarException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		} catch (HibernateException ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		}catch (Exception ex) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(this.getClass().getName(), ex);
		}finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	
	@Override
	public OrgHierarchy createHierarchy(OrgHierarchy hierarchy) {
		if (logger.isDebugEnabled()) {
			logger.debug("org create or update invoked..");
		}
		
		Session session = null;
		Transaction transaction = null;
		Map<String, Object> mapData = null;
		OrgHierarchy responseModel = hierarchy;
		boolean isSuccess = true;
		boolean forUpdate = false;
		Date currDate = new Date();
		
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			// Enhanced validation with profit center duplicate checking
			validateHierarchy(hierarchy, false);
			
			// Additional profit center duplicate validation
	       // validateNoDuplicateInSameProfitCenter(hierarchy, false);
	        if (!"N".equals(hierarchy.getIsActive())) {
	            validateNoDuplicateInSameProfitCenter(hierarchy, false);
	        }
			
			
			//OrgLevel orgLevel = getLevelByIdOrThrow(hierarchy.getLevelId());
	        
	        if (hierarchy.getParentOrgHierarchyId() != null) {
	            OrgHierarchy parentHierarchy = getHierarchyByIdOrThrow(hierarchy.getParentOrgHierarchyId());
	            if (!"Y".equals(parentHierarchy.getIsActive())) {
	                throw new BusinessValidationException("Parent hierarchy is not active");
	            }
	        }
	        
	        hierarchy.setCreatedDate(LocalDateTime.now());
	        if (hierarchy.getIsActive() == null) {
	            hierarchy.setIsActive("Y");
	        }
			
			  if (hierarchy.getOrgHierarchyId() == null) 
			  {
//				  if (orgHierarchyRepository.existsByHierarchyCode(hierarchy.getHierarchyCode())) {
//			            throw new DuplicateResourceException("Hierarchy code already exists: " + hierarchy.getHierarchyCode());
//			        }
				  session.save(hierarchy);
			  }
			  else 
			  {
//				  if (orgHierarchyRepository.existsByHierarchyCode(hierarchy.getHierarchyCode())) {
//			            throw new DuplicateResourceException("Hierarchy code already exists: " + hierarchy.getHierarchyCode());
//			        }
				  session.merge(hierarchy);
			  }
			 
			  transaction.commit();
		      responseModel = hierarchy;
		} catch (BusinessValidationException | DuplicateResourceException | ResourceNotFoundException ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        logger.error("Validation error in createHierarchy: " + ex.getMessage(), ex);
	        throw ex; // Re-throw business exceptions
	    } catch (SQLGrammarException ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        logger.error("SQL error in " + this.getClass().getName(), ex);
	        throw new BusinessValidationException("Database error occurred while creating hierarchy");
	    } catch (HibernateException ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        logger.error("Hibernate error in " + this.getClass().getName(), ex);
	        throw new BusinessValidationException("Database error occurred while creating hierarchy");
	    } catch (Exception ex) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        isSuccess = false;
	        logger.error("Unexpected error in " + this.getClass().getName(), ex);
	        throw new BusinessValidationException("Unexpected error occurred while creating hierarchy: " + ex.getMessage());
	    } finally {
	        if (session != null) {
	            session.close();
	        }
	    }
		return responseModel;
	}
	
	/**
	 * NEW METHOD: Validate no duplicate hierarchy name within same profit center and level
	 */
	private void validateNoDuplicateInSameProfitCenter(OrgHierarchy hierarchy, boolean isUpdate) {
	    // Get the org level to access profit center ID
	    OrgLevel orgLevel = getLevelByIdOrThrow(hierarchy.getLevelId());
	    
	    // Trim and normalize the hierarchy description
	    String normalizedDesc = hierarchy.getHierarchyDesc().trim();
	    hierarchy.setHierarchyDesc(normalizedDesc);
	    
	    // Check for duplicate hierarchy description within same level and profit center
	    Long excludeId = isUpdate ? hierarchy.getOrgHierarchyId() : null;
	    boolean isDuplicate = orgHierarchyRepository.existsByHierarchyDescAndLevelAndPcId(
	        normalizedDesc, hierarchy.getLevelId(), orgLevel.getPcId(), excludeId);
	    
	    if (isDuplicate) {
	        String errorMessage = String.format(
	            "Duplicate hierarchy name '%s' already exists at %s level within the same profit center (PC ID: %d). " +
	            "Each %s name must be unique within the profit center. Please choose a different name.",
	            normalizedDesc, orgLevel.getLevelDesc(), orgLevel.getPcId(), 
	            orgLevel.getLevelDesc().toLowerCase()
	        );
	        throw new DuplicateResourceException(errorMessage);
	    }
	}
	
	
	private void validateHierarchy(OrgHierarchy hierarchy, boolean isUpdate) {
        if (hierarchy == null) {
            throw new BusinessValidationException("Hierarchy cannot be null");
        }
        
        if (hierarchy.getLevelId() == null) {
            throw new BusinessValidationException("Level ID is required");
        }
        
        if (hierarchy.getHierarchyCode() == null || hierarchy.getHierarchyCode().trim().isEmpty()) {
            throw new BusinessValidationException("Hierarchy code is required");
        }
        
        if (hierarchy.getHierarchyDesc() == null || hierarchy.getHierarchyDesc().trim().isEmpty()) {
            throw new BusinessValidationException("Hierarchy description is required");
        }
        
        if (!isUpdate && (hierarchy.getCreatedBy() == null || hierarchy.getCreatedBy().trim().isEmpty())) {
            throw new BusinessValidationException("Created by is required");
        }
    }
	
	private OrgLevel getLevelByIdOrThrow(Long levelId) {
        return orgLevelRepository.findById(levelId)
            .orElseThrow(() -> new ResourceNotFoundException("OrgLevel not found with id: " + levelId));
    }
	
	 public OrgHierarchy getHierarchyByIdOrThrow(Long hierarchyId) {
	        return orgHierarchyRepository.findById(hierarchyId)
	            .orElseThrow(() -> new ResourceNotFoundException("OrgHierarchy not found with id: " + hierarchyId));
	    }

}
