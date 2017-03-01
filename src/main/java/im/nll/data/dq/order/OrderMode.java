/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package im.nll.data.dq.order;


/**
 * 排序方式.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-18下午5:23:16
 */
public enum OrderMode {

    /**
     * 自定义.
     */
    EXACT {
        @Override
        public String toMatchString(String order) {
            return order;
        }
    },

    /**
     * ASC-升序
     */
    ASC {
        @Override
        public String toMatchString(String pattern) {
            return "asc";
        }
    },

    /**
     * DESC-降序
     */
    DESC {
        @Override
        public String toMatchString(String pattern) {
            return "desc";
        }
    };

    /**
     * convert the pattern, by appending/prepending "order by ?"
     */
    public abstract String toMatchString(String pattern);

    /**
     * 自定义.
     *
     * @param mode
     * @return
     */
    public static OrderMode custom(String mode) {
        if ("desc".equals(mode.trim().toLowerCase())) {
            return OrderMode.DESC;
        } else {
            return OrderMode.ASC;
        }
    }

}
