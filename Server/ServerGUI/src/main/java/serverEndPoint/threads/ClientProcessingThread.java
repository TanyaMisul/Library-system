package serverEndPoint.threads;

import Entites.*;
import Entites.Response;
import dbLayer.managers.DataAccessManager;
import Entites.Sex;
import Entites.UserStatus;
import Entites.UserType;
import serverEndPoint.ConnectedClientInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

//В этом потоке происходит взаимодействие с клиентом
public class ClientProcessingThread extends Thread {

    private final DataAccessManager dataAccessManager;

    private final ConnectedClientInfo clientInfo;

    private final ObjectOutputStream objectOutputStream;

    private final ObjectInputStream objectInputStream;

    public ClientProcessingThread(ConnectedClientInfo clientInfo, Connection dbConnection) throws IOException {
        this.clientInfo = clientInfo;
        var socket = clientInfo.getConnectionSocket();
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        dataAccessManager = new DataAccessManager(dbConnection);
    }

    private void sendObject(Serializable object) throws IOException {

        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
    }

    private <T> T receiveObject() throws IOException, ClassNotFoundException {

        return (T) objectInputStream.readObject();
    }

    @Override
    public void run() {

        while (true) {
            try {
                switch (clientLobby()) {
                    case ADMIN, USER -> {
                        processing();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void interrupt() {
        try {
            //Заканчиваем работу
            clientInfo.getConnectionSocket().close();
        } catch (IOException e) { //Аналогично
            throw new RuntimeException(e);
        }
        super.interrupt();
    }

    public ConnectedClientInfo getClientInfo() {
        return clientInfo;
    }

    private UserType clientLobby() throws Exception {

        while (true) {

            AuthorizationCommand command = receiveObject();
            switch (command) {
                case AUTHORIZE -> {

                    String login = receiveObject();
                    String password = receiveObject();
                    var user = dataAccessManager.usersRepository.get(login, password);
                    if (user.getId() != 0 && user.getStatus() == UserStatus.NOT_BANNED) {
                        sendObject(UserType.USER);
                        clientInfo.setIdInDB(user.getId());
                        return UserType.USER;
                    }
                    var admin = dataAccessManager.adminsRepository.get(login, password);
                    if (admin.getId() != 0) {
                        sendObject(UserType.ADMIN);
                        clientInfo.setIdInDB(admin.getId());
                        return UserType.ADMIN;
                    }
                    clientInfo.setIdInDB(0);
                    sendObject(UserType.UNDEFINED);
                }
                case CHECK_IF_LOGIN_EXISTS -> {

                    String login = receiveObject();
                    var user = dataAccessManager.usersRepository.get(login);
                    var admin = dataAccessManager.adminsRepository.get(login);
                    if (user.getId() == 0 && admin.getId() == 0) {
                        sendObject(Response.NOT_FOUND);
                    } else {
                        sendObject(Response.SUCCESSFULLY);
                    }
                }
                case REGISTER -> {

                    String login = receiveObject();
                    String password = receiveObject();
                    String fullName = receiveObject();
                    int age = receiveObject();
                    Sex sex = receiveObject();
                    try {
                        int id = dataAccessManager.usersRepository.create(
                                new User(0, login, password, UserStatus.NOT_BANNED, new UserProfile(0, fullName, age, sex)));
                        clientInfo.setIdInDB(id);
                        sendObject(Response.SUCCESSFULLY);
                        return UserType.USER;
                    } catch (Exception e) {
                        sendObject(Response.ERROR);
                    }
                }
                default -> {
                    sendObject(Response.UNKNOWN_COMMAND);
                }
            }
        }
    }

    private void processing() throws IOException, ClassNotFoundException {

        while (true) {

            Command command = receiveObject();
            switch (command) {
                case EXIT -> {
                    return;
                }
                case GET_ALL_BOOKS -> {
                    try{
                        var list = dataAccessManager.booksRepository.getAll();
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case CREATE_BOOK -> {
                    Book book = receiveObject();
                    try{
                        dataAccessManager.booksRepository.create(book);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case EDIT_BOOK -> {
                    Book book = receiveObject();
                    try{
                        dataAccessManager.booksRepository.update(book);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case DELETE_BOOK -> {
                    int bookId = receiveObject();
                    try{
                        dataAccessManager.booksRepository.delete(bookId);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case ADD_BOOK_TO_USER -> {
                    int userId = receiveObject();
                    int bookId = receiveObject();
                    Date date = new Date();
                    date.setTime(date.getTime() + 10*24*3600*1000);
                    try{
                        dataAccessManager.booksRepository.addBookToUser(userId, bookId, 1, date);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case DELETE_BOOK_FROM_USER -> {
                    int userId = receiveObject();
                    int bookId = receiveObject();
                    try{
                        dataAccessManager.booksRepository.deleteBookFromUser(userId, bookId);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case GET_BORROWED_BOOKS_CURRENT_PROFILE -> {
                    try{
                        var list = dataAccessManager.booksRepository.getAllUserBooks(clientInfo.getIdInDB());
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_BORROWED_BOOKS -> {
                    int userId = receiveObject();
                    try{
                        var list = dataAccessManager.booksRepository.getAllUserBooks(userId);
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case ADD_ADMIN -> {
                    String username = receiveObject();
                    String password = receiveObject();
                    Admin adm = new Admin(0, username, password);
                    try{
                        dataAccessManager.adminsRepository.create(adm);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case GET_ALL_USERS -> {
                    try{
                        var list = dataAccessManager.usersRepository.getAll();
                        sendObject(new ArrayList<>(list));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_ALL_BORROWED_BOOKS -> {
                    try{
                        var allBooks = new ArrayList<Rec>();
                        var allUsers = dataAccessManager.usersRepository.getAll();
                        for(var user: allUsers){
                            var list = dataAccessManager.booksRepository.getAllUserBooks(user.getId());
                            allBooks.addAll(list);
                        }
                        sendObject(new ArrayList<>(allBooks));
                    } catch (SQLException e) {
                        sendObject(new ArrayList<>());
                    }
                }
                case GET_PROFILE -> {
                    try{
                        var obj = dataAccessManager.usersRepository.getById(clientInfo.getIdInDB());
                        sendObject(obj);
                    } catch (SQLException e) {
                        sendObject(new User());
                    }
                }
                case EDIT_CURRENT_PROFILE -> {
                    UserProfile userProfile = receiveObject();
                    try{
                        var user = dataAccessManager.usersRepository.getById(clientInfo.getIdInDB());
                        user.setProfile(userProfile);
                        dataAccessManager.usersRepository.update(user);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
                case EDIT_PROFILE -> {
                    int userId = receiveObject();
                    User user = receiveObject();
                    try{
                        dataAccessManager.usersRepository.update(user);
                        sendObject(Response.SUCCESSFULLY);
                    } catch (SQLException e) {
                        sendObject(Response.ERROR);
                    }
                }
            }
        }
    }
}