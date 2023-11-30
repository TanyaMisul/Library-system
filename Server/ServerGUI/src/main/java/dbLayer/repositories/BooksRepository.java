package dbLayer.repositories;

import Entites.Book;
import Entites.Rec;
import Entites.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BooksRepository {

    private final Connection dbConnection;

    public BooksRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    private Book convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new Book();
        return convertResultSetToObj(resultSet);
    }

    private Book convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new Book();
        obj.setId(resultSet.getInt("id"));
        obj.setName(resultSet.getString("name"));
        obj.setAuthor(resultSet.getString("author"));
        obj.setGenre(resultSet.getString("genre"));
        obj.setAmount(resultSet.getInt("amount"));
        return obj;
    }

    private Rec convertResultSetToObjRec(ResultSet resultSet, User user) throws SQLException {

        var obj = new Book();
        obj.setId(resultSet.getInt("id"));
        obj.setName(resultSet.getString("name"));
        obj.setAuthor(resultSet.getString("author"));
        obj.setGenre(resultSet.getString("genre"));
        obj.setAmount(resultSet.getInt("amount"));
        java.sql.Date date = resultSet.getDate("date");
        Date d = new Date();
        d.setTime(date.getTime());
        var r = new Rec(user, obj, d);
        return r;
    }

    private List<Book> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<Book>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private List<Rec> convertResultSetToListRec(ResultSet resultSet, User user) throws SQLException {

        var list = new ArrayList<Rec>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObjRec(resultSet, user));
        }
        return list;
    }

    private int getMaxId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from books;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public int create(Book obj) throws SQLException {

        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO books (name, author, genre, amount) " +
                        "values (?, ?, ?, ?)");

        insertStatement.setString(1, obj.getName());
        insertStatement.setString(2, obj.getAuthor());
        insertStatement.setString(3, obj.getGenre());
        insertStatement.setInt(4, obj.getAmount());
        insertStatement.executeUpdate();
        return getMaxId();
    }

    public void update(Book obj) throws SQLException {

        var updateStatement = dbConnection.prepareStatement(
                "UPDATE books SET name=?, author=?, genre=?, amount=?  where id = ?");
        updateStatement.setString(1, obj.getName());
        updateStatement.setString(2, obj.getAuthor());
        updateStatement.setString(3, obj.getGenre());
        updateStatement.setInt(4, obj.getAmount());
        updateStatement.setInt(5, obj.getId());
        updateStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from books where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }

    public Book getById(int id) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM books where id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public List<Book> getAll() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM books;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }

    public List<Rec> getAllUserBooks(int userId) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM user_books join books b on b.id = user_books.bookId where userId=?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, userId);
        statement.executeQuery();

        UsersRepository rep = new UsersRepository(dbConnection);

        return convertResultSetToListRec(statement.getResultSet(), rep.getById(userId));
    }

    public void addBookToUser(int userId, int bookId, int amount, Date date) throws SQLException {

        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO user_books (userId, bookId, date) " +
                        "values (?, ?, ?)");

        insertStatement.setInt(1, userId);
        insertStatement.setInt(2, bookId);
        insertStatement.setDate(3, new java.sql.Date(date.getTime()));
        insertStatement.executeUpdate();
    }

    public void deleteBookFromUser(int userId, int bookId) throws SQLException {

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from user_books where userId=? AND bookId=?");
        deleteStatement.setInt(1, userId);
        deleteStatement.setInt(2, bookId);
        deleteStatement.executeUpdate();
    }

}
