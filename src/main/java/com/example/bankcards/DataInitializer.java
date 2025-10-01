package com.example.bankcards;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.UserRole;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.Role;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardNumberEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardNumberEncryptionService cardNumberEncryptionService;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CardRepository cardRepository,
                           TransactionRepository transactionRepository,
                           PasswordEncoder passwordEncoder,
                           CardNumberEncryptionService cardNumberEncryptionService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardNumberEncryptionService = cardNumberEncryptionService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создание ролей
        UserRole adminUserRole = createRole("ROLE_ADMIN");
        UserRole userRole = createRole("ROLE_USER");

        // Создание пользователей
//        User admin = createUser("admin", passwordEncoder.encode("admin123"), adminUserRole);
//        User user = createUser("user", passwordEncoder.encode("user123"), userRole);
//
//        // Создание карт
//        createCard(admin, cardNumberEncryptionService.encrypt("4111111111111111"), LocalDate.now().plusYears(2));
//        createCard(user, cardNumberEncryptionService.encrypt("4222222222222222"), LocalDate.now().plusYears(3));
//        createCard(user, cardNumberEncryptionService.encrypt("4222222222222223"), LocalDate.now().plusYears(3));
//
//        // Создание тестовых транзакций
//        if (cardRepository.count() >= 2) {
//            List<Card> cards = cardRepository.findAll();
//            Card fromCard = cards.get(0);
//            Card toCard = cards.get(1);
//
//            fromCard.setBalance(new BigDecimal("1000.00"));
//            toCard.setBalance(new BigDecimal("500.00"));
//
//            cardRepository.save(fromCard);
//            cardRepository.save(toCard);
//
//            transactionRepository.save(
//                    new Transaction(
//                            fromCard,
//                            toCard,
//                            new BigDecimal("100.00")
//                    )
//            );
//        }
    }

    private UserRole createRole(String roleName) {
        return roleRepository.findUserRoleByRoleName(Role.valueOf(roleName))
                .orElseGet(() -> {
                    UserRole newUserRole = new UserRole();
                    newUserRole.setRoleName(Role.valueOf(roleName));
                    return roleRepository.save(newUserRole);
                });
    }

    private User createUser(String username, String password, UserRole userRoles) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setRole(userRoles);
                    return userRepository.save(newUser);
                });
    }

    private Card createCard(User owner, String cardNumber, LocalDate validityPeriod) {
        return cardRepository.findByCardNumber(cardNumber)
                .orElseGet(() -> {
                    Card newCard = new Card();
                    newCard.setOwner(owner);
                    newCard.setCardNumber(cardNumber);
                    newCard.setLast4(cardNumber.substring(12));
                    newCard.setValidityPeriod(validityPeriod);
                    newCard.setStatus("ACTIVE");
                    return cardRepository.save(newCard);
                });
    }
}

