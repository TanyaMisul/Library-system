package dbLayer.repositories;

import Entites.Sex;
import Entites.User;
import Entites.UserStatus;
import Entites.UserProfile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository {

    private final Connection dbConnection;

    public UsersRepository(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    protected static User convertResultSetToSingleObj(ResultSet resultSet) throws SQLException {

        resultSet.beforeFirst();
        if (!resultSet.next()) return new User();
        return convertResultSetToObj(resultSet);
    }

    private static User convertResultSetToObj(ResultSet resultSet) throws SQLException {

        var obj = new User();
        obj.setId(resultSet.getInt("users.id"));
        obj.setLogin(resultSet.getString("login"));
        obj.setPassword(resultSet.getString("password"));
        switch (resultSet.getInt("status")) {
            case 0 -> obj.setStatus(UserStatus.NOT_BANNED);
            case 1 -> obj.setStatus(UserStatus.BANNED);
            default -> obj.setStatus(UserStatus.BANNED);
        }

        var profile = new UserProfile();
        profile.setId(resultSet.getInt("p.id"));
        profile.setAge(resultSet.getInt("age"));
        profile.setFullName(resultSet.getString("fullName"));
        switch (resultSet.getInt("sex")){
            case 0 -> profile.setSex(Sex.MAN);
            case 1 -> profile.setSex(Sex.WOMAN);
        }
        obj.setProfile(profile);
        return obj;
    }

    protected static List<User> convertResultSetToList(ResultSet resultSet) throws SQLException {

        var list = new ArrayList<User>();
        resultSet.beforeFirst();
        while (resultSet.next()) {

            list.add(convertResultSetToObj(resultSet));
        }
        return list;
    }

    private int getMaxId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from users;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    private int getMaxProfilesId() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT MAX(id) from profiles;");
        var resultSet = statement.executeQuery();
        resultSet.next();
        return resultSet.getInt(1);
    }

    public int create(User obj) throws SQLException {

        var insertProfileStatement = dbConnection.prepareStatement(
                "INSERT INTO profiles (fullName, age, sex) " +
                        "values (?, ?, ?)");

        insertProfileStatement.setString(1, obj.getProfile().getFullName());
        insertProfileStatement.setInt(2, obj.getProfile().getAge());
        insertProfileStatement.setInt(3, obj.getProfile().getSex().ordinal());
        insertProfileStatement.executeUpdate();
        var profileId = getMaxProfilesId();

        var insertStatement = dbConnection.prepareStatement(
                "INSERT INTO users (login, password, status, profileId) " +
                        "values (?, ?, ?, ?)");

        insertStatement.setString(1, obj.getLogin());
        insertStatement.setString(2, obj.getPassword());
        insertStatement.setInt(3, obj.getStatus().ordinal());
        insertStatement.setInt(4, profileId);
        insertStatement.executeUpdate();
        return getMaxId();
    }

    public void update(User obj) throws SQLException {

        var insertProfileStatement = dbConnection.prepareStatement(
                "UPDATE profiles SET fullName=?, age=?, sex=? where id = ?");

        insertProfileStatement.setString(1, obj.getProfile().getFullName());
        insertProfileStatement.setInt(2, obj.getProfile().getAge());
        insertProfileStatement.setInt(3, obj.getProfile().getSex().ordinal());
        insertProfileStatement.setInt(4, obj.getProfile().getId());
        insertProfileStatement.executeUpdate();

        var updateStatement = dbConnection.prepareStatement(
                "UPDATE users SET login=?, password=?, status=?, profileId=? where id = ?");
        updateStatement.setString(1, obj.getLogin());
        updateStatement.setString(2, obj.getPassword());
        updateStatement.setInt(3, obj.getStatus().ordinal());
        updateStatement.setInt(4, obj.getProfile().getId());
        updateStatement.setInt(5, obj.getId());
        updateStatement.executeUpdate();
    }

    public void delete(int id) throws SQLException {

        var deleteProfileStatement = dbConnection.prepareStatement(
                "DELETE from profiles where id=?");
        deleteProfileStatement.setInt(1, getById(id).getProfile().getId());
        deleteProfileStatement.executeUpdate();

        var deleteStatement = dbConnection.prepareStatement(
                "DELETE from users where id=?");
        deleteStatement.setInt(1, id);
        deleteStatement.executeUpdate();
    }

    public User getById(int id) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM users join profiles p on p.id = users.profileId where users.id = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setInt(1, id);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public User get(String login, String password) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM users join profiles p on p.id = users.profileId where login = ? AND password = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, login);
        statement.setString(2, password);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public User get(String login) throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM users join profiles p on p.id = users.profileId where login = ?;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, login);
        statement.executeQuery();
        return convertResultSetToSingleObj(statement.getResultSet());
    }

    public List<User> getAll() throws SQLException {

        var statement = dbConnection.prepareStatement(
                "SELECT * FROM users join profiles p on p.id = users.profileId;",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.executeQuery();
        return convertResultSetToList(statement.getResultSet());
    }
}
