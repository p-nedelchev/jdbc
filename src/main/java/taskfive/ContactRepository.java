package taskfive;

import java.util.List;

/**
 * @author Petar Nedelchev <peter.krasimirov@gmail.com>
 */
public interface ContactRepository {
    Contact add(Contact contact);
    List<Contact> findAllContacts();
}
