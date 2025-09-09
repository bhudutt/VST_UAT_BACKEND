package com.hitech.dms.web.dao.admin.role.create;

/**
 * @author vinay.gautam
 *
 */
public interface AdminRoleElement<R> {
    R getId();

    R getParentId();

    default boolean checkId(R id) {
        return this.getId().equals(id);
    }

}
