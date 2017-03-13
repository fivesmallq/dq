/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package im.nll.data.dq.mapper;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultColumnMapper;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanMapper<T> implements ResultSetMapper<T> {

    private final Class<T> type;
    private final Map<String, Field> properties = new HashMap<String, Field>();

    public BeanMapper(Class<T> type) {
        this.type = type;
        cacheAllFieldsIncludingSuperClass(type);
    }

    private void cacheAllFieldsIncludingSuperClass(Class<T> type) {
        Class aClass = type;
        while (aClass != null) {
            for (Field field : aClass.getDeclaredFields()) {
                properties.put(underscoreName(field.getName()), field);
            }
            aClass = aClass.getSuperclass();
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public T map(int row, ResultSet rs, StatementContext ctx)
            throws SQLException {
        T bean;
        try {
            bean = type.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("A bean, %s, was mapped " +
                    "which was not instantiable", type.getName()), e);
        }

        ResultSetMetaData metadata = rs.getMetaData();

        for (int i = 1; i <= metadata.getColumnCount(); ++i) {
            String name = metadata.getColumnLabel(i).toLowerCase();

            Field field = properties.get(name);

            if (field != null) {
                Class type = field.getType();

                Object value;
                ResultColumnMapper mapper = ctx.columnMapperFor(type);
                if (mapper != null) {
                    value = mapper.mapColumn(rs, i, ctx);
                } else {
                    value = rs.getObject(i);
                }

                try {
                    field.setAccessible(true);
                    field.set(bean, value);
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(String.format("Unable to access " +
                            "property, %s", name), e);
                }
            }
        }

        return bean;
    }

    /**
     * <p>
     * Uncapitalizes a String changing the first letter to title case as per
     * {@link Character#toLowerCase(char)}. No other letters are changed.
     * </p>
     * <p>
     * <p>
     * <code>null</code> input String returns <code>null</code>.
     * </p>
     * <p>
     * <pre>
     * StringUtils.uncapitalize(null)  = null
     * StringUtils.uncapitalize("")    = ""
     * StringUtils.uncapitalize("Cat") = "cat"
     * StringUtils.uncapitalize("CAT") = "cAT"
     * </pre>
     *
     * @param str the String to uncapitalize, may be null
     * @return the uncapitalized String, <code>null</code> if null String input
     * @since 2.0
     */
    public static String uncapitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuffer(strLen)
                .append(Character.toLowerCase(str.charAt(0)))
                .append(str.substring(1)).toString();
    }

    /**
     * Convert a name in camelCase to an underscored name in upper case. Any
     * upper case letters are converted to lower case with a preceding
     * underscore.
     * <p>
     * 主要的作用是把java的字段转化为数据库字段.数字会被当成一个大写字母,在前面添加下划线,<br>
     * 如果连续两个字符都是数字,会当成一个整体.处理完毕后会返回小写格式
     * <p>
     * <pre>
     * StringUtils.underscoreNameProcessNum(&quot;userNameLast10&quot;) = &quot;update_name_last_10&quot;;
     * StringUtils.underscoreNameProcessNum(&quot;xx2&quot;) = &quot;xx_2&quot;;
     * </pre>
     *
     * @param name the string containing original name
     * @return the converted name toLowerCase
     */
    public static String underscoreName(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                String b = name.substring(i - 1, i);
                // 如果这个字符是大写,或者前一个字符不是数字,那么添加一个下划线
                if (s.equals(s.toUpperCase()) && !isNum(b.charAt(0))) {
                    result.append('_');
                    result.append(s);
                } else {
                    result.append(s);
                }
            }
        }
        return result.toString().toLowerCase();
    }

    /**
     * 判断单个字符是否是数字
     *
     * @param ch
     * @return
     */
    private static boolean isNum(char ch) {
        return Character.isDigit(ch);
    }

    /************************* 后添加的方法 ***********************************/

    private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
        private static final long serialVersionUID = 1L;

        {
            add(Long.class);
            add(Integer.class);
            add(String.class);
            add(java.util.Date.class);
            add(java.sql.Date.class);
            add(java.sql.Timestamp.class);
        }
    };

    /**
     * 判断一个类是否是原生类型
     *
     * @param cls
     * @return
     */
    public final static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || PrimitiveClasses.contains(cls);
    }
}
