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
package im.nll.data.dq.criterion;


import im.nll.data.dq.utils.StringUtils;

/**
 * like 规则.
 *
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.1
 * @date 2013-1-19下午12:37:15
 */
public enum MatchMode {

    /**
     * Match the entire string to the pattern<br>
     * 自定义方式.
     */
    EXACT {
        @Override
        public String toMatchString(String pattern) {
            if (MatchMode.isNull(pattern)) {
                return null;
            }
            return pattern;
        }
    },

    /**
     * Match the start of the string to the pattern.<br>
     * 字符串在前面,'%' 在后面.<br>
     * field%
     */
    START {
        @Override
        public String toMatchString(String pattern) {
            if (MatchMode.isNull(pattern)) {
                return null;
            }
            return pattern + '%';
        }
    },

    /**
     * Match the end of the string to the pattern.<br>
     * 字符串在后面,'%' 在前面.<br>
     * %field
     */
    END {
        @Override
        public String toMatchString(String pattern) {
            if (MatchMode.isNull(pattern)) {
                return null;
            }
            return '%' + pattern;
        }
    },

    /**
     * Match the pattern anywhere in the string .<br>
     * 字符串在中间,'%' 在两边.<br>
     * %field%
     */
    ANYWHERE {
        @Override
        public String toMatchString(String pattern) {
            if (MatchMode.isNull(pattern)) {
                return null;
            }
            return '%' + pattern + '%';
        }
    };

    /**
     * convert the pattern, by appending/prepending "%"
     */
    public abstract String toMatchString(String pattern);

    private static boolean isNull(String pattern) {
        return StringUtils.isNullOrEmpty(pattern);
    }
}
