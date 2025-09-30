package com.example.bankcards;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.RoleRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.HashSet;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CardRepository cardRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           CardRepository cardRepository,
                           TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(String... args) {
        // Создание ролей
        Role adminRole = createRole("ADMIN");
        Role userRole = createRole("USER");

        // Создание пользователей
        User admin = createUser("admin", "admin123", List.of(adminRole));
        User user = createUser("user", "user123", List.of(userRole));

        // Создание карт
        createCard(admin, "4111111111111111", LocalDate.now().plusYears(2));
        createCard(user, "4222222222222222", LocalDate.now().plusYears(3));

        // Создание тестовых транзакций
        if (cardRepository.count() >= 2) {
            List<Card> cards = cardRepository.findAll();
            Card fromCard = cards.get(0);
            Card toCard = cards.get(1);

            fromCard.setBalance(new BigDecimal("1000.00"));
            toCard.setBalance(new BigDecimal("500.00"));

            cardRepository.save(fromCard);
            cardRepository.save(toCard);

            transactionRepository.save(
                    new Transaction(
                            fromCard,
                            toCard,
                            new BigDecimal("100.00")
                    )
            );
        }
    }

    private Role createRole(String roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName(roleName);
                    return roleRepository.save(newRole);
                });
        return role;
    }

    private User createUser(String username, String password, List<Role> roles) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.setRoles(new HashSet<>(roles));
                    return userRepository.save(newUser);
                });
    }

    private Card createCard(User owner, String cardNumber, LocalDate validityPeriod) {
        return cardRepository.findByCardNumber(cardNumber)
                .orElseGet(() -> {
                    Card newCard = new Card();
                    newCard.setOwner(owner);
                    newCard.setCardNumber(cardNumber);
                    newCard.setValidityPeriod(validityPeriod);
                    newCard.setStatus("ACTIVE");
                    return cardRepository.save(newCard);
                });
    }
}

