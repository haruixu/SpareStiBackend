package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;

/**
 * Mapper interface for converting back and forth from the Account entity to its DTO's in addition to cascading updates
 * from a DTO to the entity.
 *
 * @author Lars N.
 * @version 1.0
 * @since 24.4.24
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountMapper {

    /**
     * Implementation instance of the mapper
     */
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    /**
     * Converts from dto to entity for account
     * @param account Account dto
     * @return Converted entity
     */
    Account toEntity(AccountDTO account);

    /**
     * Converts to dto from entity for account
     * @param account Account entity
     * @return Converted dto
     */
    @Mapping(target = "accountType", ignore = true)
    AccountDTO toDTO(Account account);

    /**
     * Updates account entity using a dto. Ignores fields that are null
     * @param account Account entity that is updated
     * @param accountDTO Account dto with the new changes
     * @return Updated account entity
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Account updateEntity(@MappingTarget Account account, AccountUpdateDTO accountDTO);
}
