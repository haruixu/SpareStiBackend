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

/**
 * Provides account-related operations bound to the user's session context within the application.
 * The service manages creation, retrieval, and updating of saving and spending accounts for users.
 *
 * @author L.M.L Nilsen and H.L Xu
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final UserService userService;

    private final ObjectValidator<AccountDTO> accountValidator;
    private final ObjectValidator<AccountUpdateDTO> accountUpdateValidator;

    /**
     * Saves a new account based on the provided DTO and associates it with the given user.
     * If the user already has an account of the same type, this method will throw an exception.
     *
     * @param accountDTO the data transfer object containing account details
     * @param user the user with whom the account will be associated
     * @return the saved account DTO
     * @throws ObjectNotValidException if the account DTO fails validation checks
     * @throws AccountAlreadyExistsException if an account of the specified type already exists for the user
     */
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

    /**
     * Retrieves the saving and spending accounts associated with the specified user.
     *
     * @param user the user whose accounts are to be retrieved
     * @return a DTO containing the user's savings and spending accounts
     */
    public AccountResponseDTO findUserAccounts(User user) {
        return new AccountResponseDTO(user.getSavingAccount(), user.getSpendingAccount());
    }

    /**
     * Updates an existing account of the specified user based on the provided update DTO.
     * This method checks if the specified account type exists before updating.
     *
     * @param accountUpdateDTO the DTO containing the updated account details
     * @param user the user whose account is to be updated
     * @return the updated account information encapsulated in a DTO
     * @throws ObjectNotValidException if the update DTO fails validation checks
     * @throws AccountNotFoundException if no account of the specified type exists for the user
     */
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
