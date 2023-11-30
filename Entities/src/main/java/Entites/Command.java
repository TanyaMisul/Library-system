package Entites;

public enum Command {

    GET_ALL_BOOKS,

    ADD_ADMIN,

    EXIT,

    //Entites.Admin commands
    CREATE_BOOK,
    EDIT_BOOK,
    DELETE_BOOK,
    ADD_BOOK_TO_USER,
    DELETE_BOOK_FROM_USER,
    GET_ALL_BORROWED_BOOKS,
    GET_BORROWED_BOOKS,

    GET_ALL_USERS,
    CHANGE_ACCESS,

    //Entites.User commands
    GET_BORROWED_BOOKS_CURRENT_PROFILE,
    GET_PROFILE,
    EDIT_CURRENT_PROFILE, EDIT_PROFILE,

}
