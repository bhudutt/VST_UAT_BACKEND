package com.hitech.dms.web.dao.admin.role.create;

import java.util.Collection;
import java.util.Objects;

/**
 * @author vinay.gautam
 *
 */
public class AdminRoleTreeNode <T extends AdminRoleElement<?>> {
	
    private T element;

    private Collection<AdminRoleTreeNode<T>> children;

    public AdminRoleTreeNode(T element) {
        this.element = element;
    }

    public AdminRoleTreeNode(T element, Collection<AdminRoleTreeNode<T>> children) {
        this.element = element;
        this.children = children;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public Collection<AdminRoleTreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(Collection<AdminRoleTreeNode<T>> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdminRoleTreeNode<?> treeNode = (AdminRoleTreeNode<?>) o;
        return Objects.equals(element, treeNode.element) &&
                Objects.equals(children, treeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, children);
    }

}
