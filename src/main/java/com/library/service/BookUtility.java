package com.library.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.library.model.*;

public class BookUtility implements BookService {

	private final SessionFactory sessionFactory;

	public BookUtility(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	public boolean isBookExists(String ISBN) {
		try (Session session = sessionFactory.openSession()) {
			Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Book WHERE ISBN = :ISBN", Long.class);
			query.setParameter("ISBN", ISBN);
			Long count = query.uniqueResult();
			return count != null && count > 0;
		}
	}

	public boolean isValidISBN(String ISBN) {
		// Define the pattern for ISBN with hyphens at specific positions
		String pattern = "^\\d{3}-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d{1}$";

		// Check if the ISBN matches the pattern
		return ISBN.matches(pattern);
	}

	public boolean isValidTitle(String title) {
		return !title.matches("-?\\d+(\\.\\d+)?"); // Returns true if title is not numeric
	}

	public boolean isValidCategory(String category) {
		return !category.matches("-?\\d+(\\.\\d+)?"); // Returns true if category is not numeric
	}

	public boolean addBook(int id, String title, int publicationYear, String category, int copies, String ISBN) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Check if the book already exists
			if (isBookExists(ISBN)) {
				System.out.println("Error adding book: Book with ISBN " + ISBN + " already exists.");
				return false;
			}

			// Validate ISBN
			if (!isValidISBN(ISBN)) {
				System.out.println("Invalid ISBN format. ISBN should be in the format: XXX-X-XX-XXXXXX-X");
				return false;
			}

			// Input validation checks...
			if (!isValidTitle(title)) {
				System.out.println("Invalid input. Title must not be numeric.");
				return false;
			}

			if (!isValidCategory(category)) {
				System.out.println("Invalid input. Category must not be numeric.");
				return false;
			}

			Book book = new Book();
			book.setId(id);
			book.setTitle(title);
			book.setPublication_year(publicationYear);
			book.setCategory(category);
			book.setCopies(copies);
			book.setISBN(ISBN);

			session.save(book);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}

	}

	public boolean updateBook(int id, String title, int publicationYear, String category, int copies, String ISBN) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Check if the book with the given ISBN exists
			if (isBookExists(ISBN)) {
				System.out.println("Error updating book: Book with ISBN " + ISBN + " already exists.");
				return false;
			}

			// Validate ISBN
			if (!isValidISBN(ISBN)) {
				System.out.println("Invalid ISBN format. ISBN should be in the format: XXX-X-XX-XXXXXX-X");
				return false;
			}

			// Input validation checks...
			if (!isValidTitle(title)) {
				System.out.println("Invalid input. Title must not be numeric.");
				return false;
			}

			if (!isValidCategory(category)) {
				System.out.println("Invalid input. Category must not be numeric.");
				return false;
			}

			// Retrieve the existing book from the database
			Book book = session.get(Book.class, id);

			// Update book attributes

			book.setTitle(title);
			book.setPublication_year(publicationYear);
			book.setCategory(category);
			book.setCopies(copies);

			session.update(book);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean deleteBook(int id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Retrieve the book to delete
			Book book = session.get(Book.class, id);

			if (book == null) {
				System.out.println("Error deleting book: Book with ID " + id + " does not exist.");
				return false;
			}

			session.delete(book);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	@Override
	public Book getBookByISBN(String ISBN) {
		try (Session session = sessionFactory.openSession()) {
			Book book = session.createQuery("FROM Book WHERE ISBN = :ISBN", Book.class).setParameter("ISBN", ISBN)
					.uniqueResult();
			if (book != null) {

			} else {
				// Book not found, handle this case accordingly
				// Handle the case where the book is not found...
			}
			return book;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Book getBookById(int id) {
		try (Session session = sessionFactory.openSession()) {
			Book book = session.get(Book.class, id);
			if (book != null) {
				// Book found, perform operations with the book object
				// Perform other operations with the book...
			} else {
				// Book not found, handle this case accordingly
				// Handle the case where the book is not found...
			}
			return book;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Book> getAllBooks() {

		try (Session session = sessionFactory.openSession()) {
			Query<Book> query = session.createQuery("from Book", Book.class);
			return query.list();

		} catch (Exception e) {
			return null;
		}

	}
}