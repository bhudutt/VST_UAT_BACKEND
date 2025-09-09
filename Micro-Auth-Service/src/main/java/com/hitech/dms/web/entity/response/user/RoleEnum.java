/**
 * 
 */
package com.hitech.dms.web.entity.response.user;

/**
 * @author dinesh.jakhar
 *
 */
public enum RoleEnum {
	DYNAMIC_ENUM_ROLE {

        @Override
        public String setGetValue(String yourValue) {
            return yourValue;
        }
    };
    public abstract String setGetValue(String value);
}
