package connectionModule;

import Entites.*;
import Entites.Response;
import Entites.Rec;
import Entites.UserProfile;
import Entites.Sex;
import Entites.UserType;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

public class ConnectionModule {

    private static Socket connectionSocket;
    private static final String serverIp;
    private static final int serverPort;
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;

    private static Properties getPropertiesFromConfig() throws IOException {

        var properties = new Properties();
        String propFileName = "Client/ConnectionModule/src/main/resources/config.properties";
        var inputStream = new FileInputStream(propFileName);
        if (inputStream == null)
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        properties.load(inputStream);
        return properties;
    }

    static {
        try {
            var properties = getPropertiesFromConfig();
            serverIp = properties.getProperty("serverIp");
            serverPort = Integer.parseInt(properties.getProperty("serverPort"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean connectToServer() throws IOException {

        connectionSocket = new Socket(serverIp, serverPort);
        if (!connectionSocket.isConnected()) return false;
        objectOutputStream = new ObjectOutputStream(connectionSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(connectionSocket.getInputStream());
        return true;
    }

    private static void sendObject(Serializable object) throws IOException {

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    private static  <T> T receiveObject() throws Exception {

        return (T) objectInputStream.readObject();
    }

    public static UserType singUp(String login, String password) throws Exception {

        sendObject(AuthorizationCommand.AUTHORIZE);
        sendObject(login);
        sendObject(password);
        return receiveObject();
    }

    public static Response registration(String login, String password, String fullName, int age, Sex sex) throws Exception {

        sendObject(AuthorizationCommand.REGISTER);
        sendObject(login);
        sendObject(password);
        sendObject(fullName);
        sendObject(age);
        sendObject(sex);
        return receiveObject();
    }

    //ТОЛЬКО ПРИ РЕГИСТРАЦИИ
    public static boolean checkIfLoginExists(String login) throws Exception {

        sendObject(AuthorizationCommand.CHECK_IF_LOGIN_EXISTS);
        sendObject(login);
        Response response = receiveObject();
        return response == Response.SUCCESSFULLY;
    }

    public static void exit() throws IOException {
        sendObject(Command.EXIT);
    }

    public static List<Book> getAllBooks() throws Exception {
        sendObject(Command.GET_ALL_BOOKS);
        return receiveObject();
    }

    public static List<Rec> getAllBorrowedBooksForAllUsers() throws Exception {
        sendObject(Command.GET_ALL_BORROWED_BOOKS);
        return receiveObject();
    }

    public static Response createBook(Book book) throws Exception {
        sendObject(Command.CREATE_BOOK);
        sendObject(book);
        return receiveObject();
    }

    public static Response editBook(Book book) throws Exception {
        sendObject(Command.EDIT_BOOK);
        sendObject(book);
        return receiveObject();
    }

    public static Response deleteBook(int bookId) throws Exception {
        sendObject(Command.DELETE_BOOK);
        sendObject(bookId);
        return receiveObject();
    }

    public static Response addBookToUser(int userId, int bookId) throws Exception {
        sendObject(Command.ADD_BOOK_TO_USER);
        sendObject(userId);
        sendObject(bookId);
        return receiveObject();
    }

    public static Response deleteBookFromUser(int userId, int bookId) throws Exception {
        sendObject(Command.DELETE_BOOK_FROM_USER);
        sendObject(userId);
        sendObject(bookId);
        return receiveObject();
    }

    public static List<User> getAllUsers() throws Exception {
        sendObject(Command.GET_ALL_USERS);
        return receiveObject();
    }

    public static List<Rec> getAllBorrowedBooks(int id) throws Exception {
        sendObject(Command.GET_BORROWED_BOOKS);
        sendObject(id);
        return receiveObject();
    }

    //USER
    public static List<Rec> getAllBorrowedBooks() throws Exception {
        sendObject(Command.GET_BORROWED_BOOKS_CURRENT_PROFILE);
        return receiveObject();
    }

    public static User getProfile() throws Exception {
        sendObject(Command.GET_PROFILE);
        return receiveObject();
    }

    public static Response updateCurrentProfile(UserProfile profile) throws Exception {
        sendObject(Command.EDIT_CURRENT_PROFILE);
        sendObject(profile);
        return receiveObject();
    }

    public static Response updateProfile(int userId, User profile) throws Exception {
        sendObject(Command.EDIT_PROFILE);
        sendObject(userId);
        sendObject(profile);
        return receiveObject();
    }

    public static Response addAdmin(String username, String password) throws Exception {
        sendObject(Command.ADD_ADMIN);
        sendObject(username);
        sendObject(password);
        return receiveObject();
    }
}