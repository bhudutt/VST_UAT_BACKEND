package com.hitech.dms.web.model.user;

import java.util.ArrayList;

public class MenuDetailListAnySize<E> extends ArrayList<E> {

	@Override
	public void add(int index, E element) {
		if (index >= 0 && index <= size())
			super.add(index, element);
		int insertNulls = index - size();
		for (int i = 0; i < insertNulls; i++) {
			super.add(null);
		}
		if(!super.contains(element))
			super.add(index, element);
	}
}
