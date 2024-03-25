package com.library.service;

import java.util.List;

import com.library.model.*;

public interface BookService {

	public boolean isBookExists(String ISBN);

	public boolean addBook(int id, String title, int publicationYear, String category, int copies, String ISBN);

	public boolean updateBook(int id, String title, int publicationYear, String category, int copies, String ISBN);

	public boolean deleteBook(int id);

	public List<Book> getAllBooks();

	Book getBookById(int id);

	Book getBookByISBN(String ISBN);

	// public List<Transactions> getTransactionsForBook(Book b);

}
