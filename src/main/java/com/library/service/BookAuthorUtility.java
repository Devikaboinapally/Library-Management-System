package com.library.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.library.model.Author;
import com.library.model.Book;

public class BookAuthorUtility implements BookAuthorService {

	private final SessionFactory sessionFactory;

	public BookAuthorUtility(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void addAuthorsToBook(int book_id, int author_id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			// Check if the book with the given ID exists
			Book book = session.get(Book.class, book_id);
			if (book == null) {
				System.out.println("Book with ID " + book_id + " is not found in the database.");
				return; // Exit the method if the book doesn't exist
			}

			// Now, the book exists, proceed with adding the author
			Author author = session.get(Author.class, author_id);
			if (author == null) {
				System.out.println("Author with ID " + author_id + " is not found in the database.");
				return;
			}

			transaction = session.beginTransaction();

			// Check if the association already exists
			if (book.getAuthors().contains(author)) {
				System.out.println("Author is already associated with the book.");
				return;
			}

			// Add the author to the book's authors collection
			book.getAuthors().add(author);
			author.getBooks().add(book);

			session.update(book);
			transaction.commit();
			System.out.println("Author added to the book successfully.");

		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void removeAuthorFromBook(int book_id, int author_id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			Book book = session.get(Book.class, book_id);
			Author author = session.get(Author.class, author_id);

			if (book == null) {
				System.out.println("Book with ID " + book_id + " is not found in the database.");
				return;
			}

			if (author == null) {
				System.out.println("Author with ID " + author_id + " is not found in the database.");
				return;
			}

			// Check if the association exists
			if (!book.getAuthors().contains(author)) {
				System.out.println("Author is not associated with the book.");
				return;
			}

			book.getAuthors().remove(author);

			session.update(book);
			transaction.commit();
			System.out.println("Author removed from the book successfully.");
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}

	@Override
	public List<Book> getBooksForAuthor(int author_id) {

		try (Session session = sessionFactory.openSession()) {
			Author author = session.get(Author.class, author_id);
			if (author == null) {
				System.out.println("Author with ID " + author_id + " does not exist.");
				return Collections.emptyList(); // Return an empty list if the author doesn't exist
			}

			List<Book> books = author.getBooks();
			if (books.isEmpty()) {
				System.out.println("No books found for the author with ID " + author_id + ".");
				return Collections.emptyList(); // Return an empty list if no books are associated with the author
			}

			System.out.println("Books of the author with ID " + author_id + ":");

			return new ArrayList<>(books);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList(); // Return an empty list in case of an exception
		}
	}

	@Override
	public List<Author> getAuthorsForBook(int book_id) {
		try (Session session = sessionFactory.openSession()) {
			Book book = session.get(Book.class, book_id);
			if (book == null) {
				System.out.println("Book with ID " + book_id + " does not exist.");
				return Collections.emptyList(); // Return an empty list if the book doesn't exist
			}

			List<Author> authors = book.getAuthors();
			if (authors.isEmpty()) {
				System.out.println("No authors found for the book with ID " + book_id + ".");
				return Collections.emptyList(); // Return an empty list if no authors are associated with the book
			}
			System.out.println("Authors of the book with ID " + book_id + ":");
			return new ArrayList<>(authors);

		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList(); // Return an empty list in case of an exception
		}
	}

}
