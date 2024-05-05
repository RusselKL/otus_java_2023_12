package ru.otus.jdbc.mapper;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData
    ) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(
                connection,
                entitySQLMetaData.getSelectByIdSql(),
                List.of(id),
                rs -> {
                    try {
                        if (rs.next()) {
                            var values = new ArrayList<>();
                            var fields = entityClassMetaData.getAllFields();
                            for (int idx = 1; idx <= fields.size(); idx++) {
                                try {
                                    values.add(rs.getObject(idx));
                                } catch (SQLException e) {
                                    values.add(null);
                                }
                            }
                            return entityClassMetaData.getConstructor().newInstance(values.toArray());
                        }
                        return null;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                }
        );
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(
                        connection,
                        entitySQLMetaData.getSelectAllSql(),
                        Collections.emptyList(),
                        rs -> {
                            var entityList = new ArrayList<T>();
                            var fields = entityClassMetaData.getAllFields();
                            try {
                                while (rs.next()) {
                                    var values = new ArrayList<>();
                                    for (int idx = 1; idx <= fields.size(); idx++) {
                                        try {
                                            values.add(rs.getObject(idx));
                                        } catch (SQLException e) {
                                            values.add(null);
                                        }
                                    }
                                    entityList.add(entityClassMetaData.getConstructor().newInstance(values.toArray()));
                                }
                                return entityList;
                            } catch (Exception e) {
                                throw new DataTemplateException(e);
                            }
                        }
                )
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            var values = new ArrayList<>();
            var fields = entityClassMetaData.getFieldsWithoutId();
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(client));
            }
            return dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getInsertSql(),
                    values
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            var values = new ArrayList<>();
            var fields = entityClassMetaData.getFieldsWithoutId();
            for (Field field : fields) {
                field.setAccessible(true);
                values.add(field.get(client));
            }
            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);
            values.add(idField.get(client));
            dbExecutor.executeStatement(
                    connection,
                    entitySQLMetaData.getUpdateSql(),
                    values
            );
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}
