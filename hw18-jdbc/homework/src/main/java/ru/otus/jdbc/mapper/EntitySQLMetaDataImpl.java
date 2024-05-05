package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final String entityName;
    private final List<Field> fields;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> data) {
        this.entityName = data.getName();
        this.fields = data.getFieldsWithoutId();
    }

    @Override
    public String getSelectAllSql() {
        return String.format("SELECT * FROM %s", entityName);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("SELECT * FROM %s WHERE id = ?", entityName);
    }

    @Override
    public String getInsertSql() {
        var fieldNames = fields.stream().map(Field::getName).toList();
        String columns = String.join(",", fieldNames);
        String values = String.join(",", Collections.nCopies(fieldNames.size(), "?"));
        return String.format("INSERT INTO %s(%s) values (%s)", entityName, columns, values);
    }

    @Override
    public String getUpdateSql() {
        var fieldNames = fields.stream().map(Field::getName).map(name -> name + " = ?").toList();
        String values = String.join(", ", fieldNames);
        return String.format("UPDATE %s SET %s WHERE id = ?", entityName, values);
    }
}
