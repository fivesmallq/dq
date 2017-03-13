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

import im.nll.data.dq.utils.TypeUtils;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultColumnMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectColumnMapper<T> implements ResultColumnMapper<T> {

    private final Class<T> clazz;

    public ObjectColumnMapper(Class clazz) {
        this.clazz = clazz;
    }

    @Override
    public T mapColumn(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
        Object value = r.getObject(columnNumber);
        return r.wasNull() ? null : TypeUtils.cast(value, clazz);
    }

    @Override
    public T mapColumn(ResultSet r, String columnLabel, StatementContext ctx) throws SQLException {
        Object value = r.getObject(columnLabel);
        return r.wasNull() ? null : TypeUtils.cast(value, clazz);
    }
}
