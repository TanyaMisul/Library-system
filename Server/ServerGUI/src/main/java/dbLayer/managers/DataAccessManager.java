package dbLayer.managers;

import dbLayer.repositories.*;

import java.sql.Connection;

public class DataAccessManager {

    public final UsersRepository usersRepository;

    public final AdminsRepository adminsRepository;

    public final BooksRepository booksRepository;


    public DataAccessManager(Connection connection) {

        usersRepository = new UsersRepository(connection);
        adminsRepository = new AdminsRepository(connection);
        booksRepository = new BooksRepository(connection);
    }
}
