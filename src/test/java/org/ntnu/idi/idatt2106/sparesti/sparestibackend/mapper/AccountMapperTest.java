package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;

public class AccountMapperTest {

    private final AccountMapper accountMapper = AccountMapper.INSTANCE;

    @Test
    public void testToDTO() {
        Account account = new Account();
        account.setAccNumber("123456789");
        account.setBalance(BigDecimal.ZERO);

        AccountDTO accountDTO = accountMapper.toDTO(account);

        assertEquals(123456789L, accountDTO.accNumber());
        assertEquals(BigDecimal.ZERO, accountDTO.balance());
    }

    @Test
    public void testToEntityWithNull() {
        assertEquals(null, accountMapper.toEntity(null));
    }

    @Test
    public void testUpdateEntityWithNull() {
        assertEquals(null, accountMapper.updateEntity(null, null));
    }
}
