package taskfive;

import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface UserRepository {
    User add(User user);
    List<User> findAllUsers();
}
