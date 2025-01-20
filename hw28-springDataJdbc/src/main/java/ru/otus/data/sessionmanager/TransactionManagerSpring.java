package ru.otus.data.sessionmanager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.domain.sessionmanager.TransactionAction;
import ru.otus.domain.sessionmanager.TransactionManager;

@Component
public class TransactionManagerSpring implements TransactionManager {

    @Transactional
    @Override
    public <T> T doInTransaction(TransactionAction<T> action) {
        return action.get();
    }

    @Transactional(readOnly = true)
    @Override
    public <T> T doInReadOnlyTransaction(TransactionAction<T> action) {
        return action.get();
    }

}
