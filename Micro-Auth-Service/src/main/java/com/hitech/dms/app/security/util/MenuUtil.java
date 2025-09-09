/**
 * 
 */
package com.hitech.dms.app.security.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.hitech.dms.web.model.user.AppMenuModel;

/**
 * @author dinesh.jakhar
 *
 */
public class MenuUtil {
	public static List<AppMenuModel> getHierarchicalList(final List<AppMenuModel> originalList) {
		final List<AppMenuModel> copyList = new ArrayList<>(originalList);

		copyList.forEach(element -> {
			originalList.stream().filter(parent -> parent.getId().compareTo(element.getParentId()) == 0).findAny()
					.ifPresent(parent -> {
						if (parent.getChildren() == null) {
							parent.setChildren(new ArrayList<>());
						}
						parent.getChildren().add(element);
//						originalList.remove(element);
					});
		});
		Predicate<AppMenuModel> menuPredicate = m -> (m.getParentId() != 0);
		originalList.subList(1, originalList.size()).removeIf(menuPredicate);
		List<AppMenuModel> sortedList = originalList.stream().sorted(Comparator.comparingInt(AppMenuModel::getMenuOrder))
				.collect(Collectors.toList());
//		sortedList.forEach(element -> {
//			if(element.getChildren() == null) {
//				element.setChildren(new ArrayList<>());
//			}
//		});
		return sortedList;
	}
}
