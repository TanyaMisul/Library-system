package com.example.clientgui;

import Entites.Rec;
import Entites.Sex;
import connectionModule.ConnectionModule;
import Entites.Book;
import javafx.beans.value.ObservableValueBase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BorrowedManagementController {

    String searchName;
    String searchAuthor;
    String searchGenre;
    private void updatePage(){

        ObservableList<Rec> books = FXCollections.observableArrayList();

        try {
            List<Rec> list = ConnectionModule.getAllBorrowedBooks(Client.user.getId());
            for (var item: list) {
                boolean isNeedToShow = true;

                if(!searchName.isEmpty())
                    isNeedToShow &= item.getBook().getName().contains(searchName);
                if(!searchAuthor.isEmpty())
                    isNeedToShow &= item.getBook().getAuthor().contains(searchAuthor);
                if(!searchGenre.isEmpty())
                    isNeedToShow &= item.getBook().getGenre().contains(searchGenre);

                if(isNeedToShow){
                    item.getBook().setAmount(1);
                    books.add(item);
                }
            }

            projectsTable.setItems(books);

        } catch (Exception e) {
            AlertManager.getInstance().ErrorLog("Ошибка", "Ошбика соединения");
        }
    }

    @FXML
    public void initialize(){

        searchName = "";
        searchAuthor = "";
        searchGenre = "";

        colName.setCellValueFactory(financedProjectStringCellDataFeatures -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return financedProjectStringCellDataFeatures.getValue().getBook().getName();
            }
        });
        colGenre.setCellValueFactory(financedProjectStringCellDataFeatures -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return financedProjectStringCellDataFeatures.getValue().getBook().getGenre();
            }
        });
        colAuthor.setCellValueFactory(financedProjectStringCellDataFeatures -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return financedProjectStringCellDataFeatures.getValue().getBook().getAuthor();
            }
        });
        colAmount.setCellValueFactory(financedProjectStringCellDataFeatures -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return String.valueOf(financedProjectStringCellDataFeatures.getValue().getBook().getAmount());
            }
        });
        colDate.setCellValueFactory(financedProjectStringCellDataFeatures -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
                return fmt.format(financedProjectStringCellDataFeatures.getValue().getDate());
            }
        });

        updatePage();
    }

    @FXML
    private TextField nameSearchInput;
    @FXML
    private TextField authorSearchInput;
    @FXML
    private TextField genreSearchInput;

    @FXML
    private TableView<Rec> projectsTable;

    @FXML
    private TableColumn<Rec, String> colName;

    @FXML
    private TableColumn<Rec, String> colAuthor;

    @FXML
    private TableColumn<Rec, String> colGenre;

    @FXML
    private TableColumn<Rec, String> colAmount;
    @FXML
    private TableColumn<Rec, String> colDate;

    @FXML
    void onApply(ActionEvent event) {
        searchName = nameSearchInput.getText();
        searchAuthor = authorSearchInput.getText();
        searchGenre = genreSearchInput.getText();

        updatePage();
    }

    @FXML
    void onAdd(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.borrowEdit, Client.changingWindowUtility.borrowEditW, Client.changingWindowUtility.borrowEditH, "Выдача книги");
    }

    @FXML
    void onDel(ActionEvent event) {
        Rec book = projectsTable.getSelectionModel().getSelectedItem();
        if(book == null){
            AlertManager.getInstance().WarningLog("Для оформления возврата сначала выберите книгу в таблице.", "");
            return;
        }

        try {
            ConnectionModule.deleteBookFromUser(Client.user.getId(), book.getBook().getId());
            projectsTable.getItems().remove(book);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onGoBack(ActionEvent event) {
        Client.changingWindowUtility.showWindow(Client.changingWindowUtility.userView, Client.changingWindowUtility.userW, Client.changingWindowUtility.userH, "Пользователи");
    }

    @FXML
    void onReport(ActionEvent event) {
        List<Book> list = null;
        try {
            list = ConnectionModule.getAllBooks();
            var listAmounts = new ArrayList<Integer>();
            for(int i=0; i<list.size(); ++i)
                listAmounts.add(0);

            var records = ConnectionModule.getAllBorrowedBooksForAllUsers();
            for(var rec: records){
                for(int i=0; i<list.size(); ++i){
                    if(list.get(i).getId() == rec.getBook().getId()){
                        listAmounts.set(i, listAmounts.get(i)+1);
                    }
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(int i=0; i<list.size();++i) {
                dataset.addValue(listAmounts.get(i), "Количество", list.get(i).getName());
            }


            ChartViewer chartViewer = new ChartViewer(dataset);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream("Отчет.png");
                // Сохранить как файл PNG
                ChartUtilities.writeChartAsPNG(out, chartViewer.createChart(), 900, 600);
                // Сохранить как файл JPEG
                //ChartUtilities.writeChartAsJPEG(out, chart, 500, 400);
                out.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        //do nothing
                    }
                }
            }
            AlertManager.getInstance().InformationLog("Успешно!", "Данные сохранены в папке с программой");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void onReportText(ActionEvent event) {
        List<Rec> list = null;
        try {
            list = ConnectionModule.getAllBorrowedBooks(Client.user.getId());

            String filename = Client.user.getLogin() + "_report.txt";
            FileOutputStream out = new FileOutputStream(filename);

            String text = "Пользователь: " + Client.user.getLogin() + "\n";
            text += "ФИО: " + Client.user.getProfile().getFullName() + "\n";
            text += "Возраст: " + Client.user.getProfile().getAge() + "\n";
            text += "Пол: " + (Client.user.getProfile().getSex() == Sex.MAN? "Муж." : "Жен.") + "\n\n\n";
            text += "Взятые книги:\n\n\n";
            out.write(text.getBytes());

            for (var item: list) {
                boolean isNeedToShow = true;

                if (!searchName.isEmpty())
                    isNeedToShow &= item.getBook().getName().contains(searchName);
                if (!searchAuthor.isEmpty())
                    isNeedToShow &= item.getBook().getAuthor().contains(searchAuthor);
                if (!searchGenre.isEmpty())
                    isNeedToShow &= item.getBook().getGenre().contains(searchGenre);

                if (isNeedToShow) {
                    text = "";
                    text += "Название:" + item.getBook().getName() + "\n";
                    text += "Автор:" + item.getBook().getAuthor() + "\n";
                    text += "Жанр:" + item.getBook().getGenre() + "\n\n";
                    out.write(text.getBytes());
                }
            }
            out.close();

            AlertManager.getInstance().InformationLog("Успешно!", "Данные сохранены в файл " + filename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

        @FXML
    void onSendEmail(ActionEvent event) {
        Rec book = projectsTable.getSelectionModel().getSelectedItem();
        if(book == null){
            AlertManager.getInstance().WarningLog("Для отправки уведомления сначала выберите книгу в таблице.", "");
            return;
        }

        try {
            String text = "Доброго времени суток!\n";
            text +="Дорогой читатель, " + Client.user.getProfile().getFullName() +", Вы просрочили срок сдачи книги '" + book.getBook().getName() +"' (" + book.getBook().getAuthor() + ").\n";
            text +="Во избежание последствий (наложение штрафа или исключение из нашего круга читателей), просим Вас незамедлительно вернуть книгу.";

            MailSender sender = new MailSender();
            sender.send("Просрочен возврат книги", text, Client.user.getLogin(), null);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        }
}
