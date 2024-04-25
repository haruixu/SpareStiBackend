package org.ntnu.idi.idatt2106.sparesti.sparestibackend.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountUpdateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.yml")
class AccountIntegrationTest {

    @Autowired private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired private ObjectMapper objectMapper;

    String savingAccountDTOJsonRequest;

    String spendingAccountDTOJsonRequest;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        RegisterRequest registerRequest =
                new RegisterRequest(
                        "testFirstName",
                        "testLastName",
                        "user",
                        "testPassword123!",
                        "testEmail@test.com");
        String jsonRequest = objectMapper.writeValueAsString(registerRequest);

        mvc.perform(
                        MockMvcRequestBuilders.post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(jsonRequest))
                .andReturn();

        AccountDTO savingAccountDTO =
                new AccountDTO(AccountType.SAVING, 50L, new BigDecimal("200.55"));
        savingAccountDTOJsonRequest = objectMapper.writeValueAsString(savingAccountDTO);

        AccountDTO spendingAccountDTO =
                new AccountDTO(AccountType.SPENDING, 900L, new BigDecimal("6000.455"));
        spendingAccountDTOJsonRequest = objectMapper.writeValueAsString(spendingAccountDTO);
    }

    @Test
    @WithMockUser
    void postValidSavingAccount() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(savingAccountDTOJsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void postValidSpendingAccount() throws Exception {
        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(spendingAccountDTOJsonRequest))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void postAccountWithInvalidAccountType() throws Exception {
        // Replace the valid account type with an invalid one in the JSON
        String invalidAccountDTOJsonRequest =
                savingAccountDTOJsonRequest.replace(
                        "\"accountType\":\"SAVING\"", "\"accountType\":\"INVALID_TYPE\"");

        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(invalidAccountDTOJsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void postAccountWithMissingAccountNumber() throws Exception {
        String modifiedJson = savingAccountDTOJsonRequest.replace(",\"accNumber\":50", "");

        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void postAccountWithMissingBalance() throws Exception {
        String modifiedJson = savingAccountDTOJsonRequest.replace(",\"balance\":200.55", "");

        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void postValidSavingAccountWhenSavingAccountAlreadyExists() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        mvc.perform(
                        MockMvcRequestBuilders.post("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(savingAccountDTOJsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void getValidAccounts() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(spendingAccountDTOJsonRequest));

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/users/me/accounts")).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        String savingAccNumber = responseJson.get("savingAccount").get("accNumber").asText();
        String savingBalance = responseJson.get("savingAccount").get("balance").asText();
        String spendingAccNumber = responseJson.get("spendingAccount").get("accNumber").asText();
        String spendingBalance = responseJson.get("spendingAccount").get("balance").asText();

        Assertions.assertEquals("50", savingAccNumber);
        Assertions.assertEquals("200.55", savingBalance);
        Assertions.assertEquals("900", spendingAccNumber);
        Assertions.assertEquals("6000.46", spendingBalance);
    }

    @Test
    @WithMockUser
    void putValidSavingAccountBalance() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson = updateAccountDTOJsonRequest.replace(",\"accNumber\":191", "");

        mvc.perform(
                MockMvcRequestBuilders.put("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(modifiedJson));

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/users/me/accounts")).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        String savingAccNumber = responseJson.get("savingAccount").get("accNumber").asText();
        String savingBalance = responseJson.get("savingAccount").get("balance").asText();

        Assertions.assertEquals("50", savingAccNumber);
        Assertions.assertEquals("660.21", savingBalance);
    }

    @Test
    @WithMockUser
    void putValidSpendingAccountBalanceWithoutAccNumber() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(spendingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SPENDING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson = updateAccountDTOJsonRequest.replace(",\"accNumber\":191", "");

        mvc.perform(
                MockMvcRequestBuilders.put("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(modifiedJson));

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/users/me/accounts")).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        String savingAccNumber = responseJson.get("spendingAccount").get("accNumber").asText();
        String savingBalance = responseJson.get("spendingAccount").get("balance").asText();

        Assertions.assertEquals("900", savingAccNumber);
        Assertions.assertEquals("660.21", savingBalance);
    }

    @Test
    @WithMockUser
    void putValidSpendingAccNumberWithoutBalance() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(spendingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SPENDING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson = updateAccountDTOJsonRequest.replace(",\"balance\":234", "");

        mvc.perform(
                MockMvcRequestBuilders.put("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(modifiedJson));

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/users/me/accounts")).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        String savingAccNumber = responseJson.get("spendingAccount").get("accNumber").asText();
        String savingBalance = responseJson.get("spendingAccount").get("balance").asText();

        Assertions.assertEquals("191", savingAccNumber);
        Assertions.assertEquals("660.21", savingBalance);
    }

    @Test
    @WithMockUser
    void putValidAccountBalanceWithAccNumber() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);

        mvc.perform(
                MockMvcRequestBuilders.put("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(updateAccountDTOJsonRequest));

        MvcResult result =
                mvc.perform(MockMvcRequestBuilders.get("/users/me/accounts")).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);

        String savingAccNumber = responseJson.get("savingAccount").get("accNumber").asText();
        String savingBalance = responseJson.get("savingAccount").get("balance").asText();

        Assertions.assertEquals("191", savingAccNumber);
        Assertions.assertEquals("660.21", savingBalance);
    }

    @Test
    @WithMockUser
    void putValidAccountWhenAccountDoesNotExist() throws Exception {
        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(updateAccountDTOJsonRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void putAccountWithInvalidAccountType() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson =
                updateAccountDTOJsonRequest.replace(
                        "\"accountType\":\"SAVING\"", "\"accountType\":\"INVALID_TYPE\"");

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void putAccountWithMissingBalance() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson = updateAccountDTOJsonRequest.replace(",\"balance\":660.21", "");

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void putAccountWithMissingAccNumber() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson = updateAccountDTOJsonRequest.replace(",\"accNumber\":191", "");

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void putAccountWithMissingAccountType() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/users/me/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(savingAccountDTOJsonRequest));

        AccountUpdateDTO accountUpdateDTO =
                new AccountUpdateDTO(AccountType.SAVING, 191L, new BigDecimal("660.21"));
        String updateAccountDTOJsonRequest = objectMapper.writeValueAsString(accountUpdateDTO);
        String modifiedJson =
                updateAccountDTOJsonRequest.replace("\"accountType\":\"SAVING\",", "");

        mvc.perform(
                        MockMvcRequestBuilders.put("/users/me/accounts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(modifiedJson))
                .andExpect(status().isBadRequest());
    }
}
