package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger logger = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    private final Class<T> entityClass;
    private final List<Field> entityClassFields;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        entityClass = clazz;
        entityClassFields = Arrays.stream(entityClass.getDeclaredFields()).toList();
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        Constructor<T> constructor = null;
        try {
            Constructor<?>[] constructors = entityClass.getDeclaredConstructors();
            var fieldTypes = entityClassFields.stream().map(Field::getType).toList();
            for (Constructor<?> c : constructors) {
                List<Class<?>> parameterTypes = Arrays.stream(c.getParameterTypes()).toList();
                if (fieldTypes.equals(parameterTypes)) {
                    constructor = (Constructor<T>) c;
                }
            }
        } catch (Exception e) {
            logger.info("GetConstructor method failed: {}", e.getMessage());
        }
        return constructor;
    }

    @Override
    public Field getIdField() {
        Field idField = null;
        try {
            var fieldsAnnotatedById = entityClassFields.stream()
                    .filter(field -> field.getAnnotation(Id.class) != null).toList();
            if (fieldsAnnotatedById.size() > 1) {
                throw new IllegalStateException(String.format("Class %1$s has more than one ID annotation", entityClass.getName()));
            } else {
                idField = fieldsAnnotatedById.getFirst();
            }
        } catch (Exception e) {
            logger.info("GetIdField method failed: {}", e.getMessage());
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return entityClassFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return entityClassFields.stream()
                .filter(field -> field.getAnnotation(Id.class) == null)
                .toList();
    }
}
