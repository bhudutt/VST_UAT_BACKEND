package com.hitech.dms.web.model.user;

import java.util.HashSet;
import java.util.Set;

/*@Entity
@Table(name = "roles")
@NamedQuery(name = "Role.findByRole",
        query = "select r from Role r where r.role = :role"
)*/
public class Role {

   /* @Id
    @GeneratedValue
    @Column(name = "role_id", unique = true, nullable = false)*/
    private Integer roleId;

//    @Column(name = "role", nullable = false, length = 100)
    private String role;
    private Integer buId;
    private String roleCode;
    private String roleName;
    private Boolean isActive;
    private Integer isFor;
	private Integer parentId;
	private String parentName;
	private int[] menuIDs;
	private boolean selectedRole;

//  todo: need to change FetchType to LAZY
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userRole")
    private Set<User> userRecords = new HashSet<User>();

    public Set<User> getUserRecords() {
        return this.userRecords;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserRecords(Set<User> userRecords) {
        this.userRecords = userRecords;
    }

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public int[] getMenuIDs() {
		return menuIDs;
	}

	public void setMenuIDs(int[] menuIDs) {
		this.menuIDs = menuIDs;
	}

	public boolean isSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(boolean selectedRole) {
		this.selectedRole = selectedRole;
	}

	public Integer getBuId() {
		return buId;
	}

	public void setBuId(Integer buId) {
		this.buId = buId;
	}

	public Integer getIsFor() {
		return isFor;
	}

	public void setIsFor(Integer isFor) {
		this.isFor = isFor;
	}
}