import com.challenge.library.controllers.BookController;
import com.challenge.library.entities.Book;
import com.challenge.library.entities.BookStatusEnum;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        //TODO: Implementar lógica do Scanner

        //TODO: Implementar lógica do Menu

        //TODO: Implementar iteração do Menu com reset



        BookController bookController = new BookController();

        //Listar Livros
        listarLivrosConsole(bookController);

        //Salvar livro
        Book newBook = new Book();
        newBook.setTitle("AVATAR");
        newBook.setAuthor("Fulano");
        newBook.setStatus(BookStatusEnum.AVAILABLE);

        bookController.save(newBook);
        listarLivrosConsole(bookController);

    }

    public static void listarLivrosConsole(BookController bookController) {
        List<Book> livrosDisponiveis = bookController.listByStatus(BookStatusEnum.AVAILABLE);
        for (Book book : livrosDisponiveis) {
            System.out.println(book);
        }

    }

}