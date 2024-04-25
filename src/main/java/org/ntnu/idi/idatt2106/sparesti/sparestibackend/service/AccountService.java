package org.ntnu.idi.idatt2106.sparesti.sparestibackend.service;

import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountResponseDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.AccountAlreadyExistsException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.AccountNotFoundException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.exception.validation.ObjectNotValidException;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper.AccountMapper;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.validation.ObjectValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;

    private final ObjectValidator<AccountDTO> accountValidator;
    private final ObjectValidator<AccountUpdateDTO> accountUpdateValidator;

    public AccountDTO saveAccount(AccountDTO accountDTO, User user) throws ObjectNotValidException {
        accountValidator.validate(accountDTO);
        if (accountDTO.accountType() == AccountType.SAVING && user.getSavingAccount() == null) {
            user.setSavingAccount(AccountMapper.INSTANCE.toEntity(accountDTO));
        } else if (accountDTO.accountType() == AccountType.SPENDING
                && user.getSpendingAccount() == null) {
            user.setSpendingAccount(AccountMapper.INSTANCE.toEntity(accountDTO));
        } else {
            throw new AccountAlreadyExistsException(
                    "Account already exists. Use put to update an account.");
        }
        userService.save(user);
        return accountDTO;
    }

    public AccountResponseDTO findUserAccounts(User user) {
        return new AccountResponseDTO(user.getSavingAccount(), user.getSpendingAccount());
    }

    public AccountResponseDTO updateAccount(AccountUpdateDTO accountUpdateDTO, User user)
            throws ObjectNotValidException {
        accountUpdateValidator.validate(accountUpdateDTO);
        if (accountUpdateDTO.accountType() == AccountType.SAVING
                && user.getSavingAccount() != null) {
            Account updatedAccount =
                    AccountMapper.INSTANCE.updateEntity(user.getSavingAccount(), accountUpdateDTO);
            user.setSavingAccount(updatedAccount);
        } else if (accountUpdateDTO.accountType() == AccountType.SPENDING
                && user.getSpendingAccount() != null) {
            Account updatedAccount =
                    AccountMapper.INSTANCE.updateEntity(
                            user.getSpendingAccount(), accountUpdateDTO);
            user.setSpendingAccount(updatedAccount);
        } else {
            throw new AccountNotFoundException(
                    "Account not found. You need to create an account before putting to it");
        }
        userService.save(user);

        return findUserAccounts(user);
    }
}
