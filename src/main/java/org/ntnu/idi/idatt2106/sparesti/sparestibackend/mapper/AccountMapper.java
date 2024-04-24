package org.ntnu.idi.idatt2106.sparesti.sparestibackend.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.Account;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toEntity(AccountDTO account);

    @Mapping(target = "accountType", ignore = true)
    AccountDTO toDTO(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "accNumber", ignore = true)
    Account updateEntity(@MappingTarget Account account, AccountUpdateDTO accountDTO);
}
