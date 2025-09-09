/**
 * @author vinay.gautam
 *
 */
package com.hitech.dms.web.dao.admin.role.create;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author vinay.gautam
 *
 */
public class AdminRoleHierarchy <T extends AdminRoleElement<R>, R>{
	

    private final Collection<T> all;

    public AdminRoleHierarchy(Collection<T> all) {
        this.all = all;
    }

    public Collection<T> getRoots() {
        return this.all.stream()
                .filter(this::isRoot)
                .collect(Collectors.toSet());
    }

    public AdminRoleTreeNode<T> getTree(T element) {
        final Collection<T> children = this.getChildren(element);
        return children.isEmpty() ? new AdminRoleTreeNode<>(element) : new AdminRoleTreeNode<>(element, children.stream()
                .map(this::getTree)
                .collect(Collectors.toSet()));
    }

    public Collection<T> getChildren(T parent) {
        return this.all.stream()
                .filter(element -> parent.checkId(element.getParentId()))
                .collect(Collectors.toSet());
    }
    
    public boolean isRoot(T element) {
        return !Optional.ofNullable(element.getParentId()).isPresent();
    }

}
