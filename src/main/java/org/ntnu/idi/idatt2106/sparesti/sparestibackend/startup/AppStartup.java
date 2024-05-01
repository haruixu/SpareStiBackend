package org.ntnu.idi.idatt2106.sparesti.sparestibackend.startup;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.account.AccountDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.challenge.ChallengeCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.config.ChallengeTypeConfigDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.goal.GoalCreateDTO;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.dto.user.RegisterRequest;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.User;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.AccountType;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Experience;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.model.enums.Motivation;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.repository.UserRepository;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AccountService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.AuthenticationService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.ChallengeService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.GoalService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserConfigService;
import org.ntnu.idi.idatt2106.sparesti.sparestibackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Class that populates the database with test data
 */
@Component
@RequiredArgsConstructor
@Profile("!test")
public class AppStartup implements CommandLineRunner {

    private final UserService userService;
    private final GoalService goalService;
    private final ChallengeService challengeService;
    private final AuthenticationService authenticationService;

    private final UserConfigService userConfigService;

    private final UserRepository userRepository;

    private final AccountService accountService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Runs on application startup.
     *
     * @param args Command line arguments
     */
    @Override
    public void run(String... args) {
        if (userService.userExistByEmail("testMail@testMail.no")) {
            return;
        }
        User user = prepareUser();
        logger.info("Preparing goals");
        prepareGoals(user);
        logger.info("Preparing challenges");
        prepareChallenges(user);
        logger.info("Finished setup");
    }

    private User prepareUser() {
        // Creating user
        RegisterRequest registerRequest =
                new RegisterRequest(
                        "test", "test", "testBrukernavn", "Test123!", "testMail@testMail.no");
        authenticationService.register(registerRequest);

        User user = userRepository.findByUsername("testBrukernavn").get();

        // Creating config
        ChallengeConfigDTO challengeConfigDTO =
                new ChallengeConfigDTO(
                        Experience.VERY_HIGH,
                        Motivation.VERY_HIGH,
                        Set.of(
                                new ChallengeTypeConfigDTO(
                                        "Kaffe", BigDecimal.valueOf(210), BigDecimal.valueOf(30)),
                                new ChallengeTypeConfigDTO(
                                        "Snus", BigDecimal.valueOf(120), BigDecimal.valueOf(40))));
        userConfigService.createChallengeConfig(user.getUsername(), challengeConfigDTO);

        // Creating accounts
        AccountDTO spendingAccount =
                new AccountDTO(AccountType.SPENDING, 11111111112L, BigDecimal.ZERO);
        AccountDTO savingAccount =
                new AccountDTO(AccountType.SAVING, 11111111111L, BigDecimal.ZERO);

        accountService.saveAccount(spendingAccount, user);
        accountService.saveAccount(savingAccount, user);
        return user;
    }

    private void prepareGoals(User user) {
        GoalCreateDTO hellas =
                new GoalCreateDTO(
                        "Hellas",
                        BigDecimal.valueOf(5000),
                        BigDecimal.valueOf(5000),
                        "Tur til hellas",
                        ZonedDateTime.now().plusDays(60));
        GoalCreateDTO gamingPC =
                new GoalCreateDTO(
                        "Gaming PC",
                        BigDecimal.ZERO,
                        BigDecimal.valueOf(12500),
                        "Insane specks",
                        ZonedDateTime.now().plusDays(90));
        GoalCreateDTO moped =
                new GoalCreateDTO(
                        "Moped",
                        BigDecimal.ZERO,
                        BigDecimal.valueOf(69000),
                        "Moped for 18-års dagen",
                        ZonedDateTime.now().plusDays(130));

        goalService.save(hellas, user);
        goalService.save(gamingPC, user);
        goalService.save(moped, user);
    }

    private void prepareChallenges(User user) {
        ChallengeCreateDTO kaffe =
                new ChallengeCreateDTO(
                        "Kaffestans",
                        BigDecimal.valueOf(250),
                        BigDecimal.valueOf(500),
                        BigDecimal.valueOf(30),
                        "Drikk mindre kaffe",
                        ZonedDateTime.now().plusDays(14),
                        "Kaffe");
        ChallengeCreateDTO kaffe2 =
                new ChallengeCreateDTO(
                        "Mindre kaffe",
                        BigDecimal.valueOf(630),
                        BigDecimal.valueOf(6000),
                        BigDecimal.valueOf(30),
                        "Drikk mindre kaffe",
                        ZonedDateTime.now().plusDays(340),
                        "Kaffe");
        ChallengeCreateDTO bensin =
                new ChallengeCreateDTO(
                        "Bensin",
                        BigDecimal.valueOf(840),
                        BigDecimal.valueOf(1600),
                        BigDecimal.valueOf(20),
                        "Bruk mindre bensin",
                        ZonedDateTime.now().plusDays(15),
                        "Bensin");
        ChallengeCreateDTO monster =
                new ChallengeCreateDTO(
                        "Mindre monster",
                        BigDecimal.ZERO,
                        BigDecimal.valueOf(115),
                        BigDecimal.valueOf(23),
                        "Drikk mindre monster",
                        ZonedDateTime.now().plusDays(21),
                        "Energidrikke");
        ChallengeCreateDTO kiosk =
                new ChallengeCreateDTO(
                        "Stans kiosken",
                        BigDecimal.valueOf(10),
                        BigDecimal.valueOf(250),
                        BigDecimal.valueOf(50),
                        "Ikke kjøp kiosk",
                        ZonedDateTime.now().plusDays(28),
                        "Kiosken");
        ChallengeCreateDTO godteri =
                new ChallengeCreateDTO(
                        "Mindre godteri",
                        BigDecimal.valueOf(300),
                        BigDecimal.valueOf(300),
                        BigDecimal.valueOf(60),
                        "Spis mindre godteri",
                        ZonedDateTime.now().plusDays(28),
                        "godteri");

        challengeService.save(kaffe, user);
        challengeService.save(monster, user);
        challengeService.save(kiosk, user);
        challengeService.save(kaffe2, user);
        challengeService.save(bensin, user);
        challengeService.save(godteri, user);
    }
}
