package com.library.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.library.model.Author;

public class AuthorUtility implements AuthorService {

	private final SessionFactory sessionFactory;

	public AuthorUtility(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isAuthorExists(int id) {
		try (Session session = sessionFactory.openSession()) {
			Author existingAuthor = session.get(Author.class, id);
			return existingAuthor != null;
		} catch (Exception e) {
			System.out.println("Error checking author existence: " + e.getMessage());
			return true; // Assuming error handling as per your requirement
		}
	}

	public boolean addAuthor(int id, String first_name, String last_name, String nationality) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Check if the author already exists
			if (isAuthorExists(id)) {
				return false;
			}

			// Create an Author object and set its properties
			Author author = new Author();
			author.setId(id);
			author.setFirst_name(first_name);
			author.setLast_name(last_name);
			author.setNationality(nationality);

			// Save the author object to the database
			session.save(author);
			transaction.commit();

			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			return false;
		}
	}

	public boolean updateAuthor(int id, String newfirst_name, String newlast_name, String newnationality) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Retrieve the existing author from the database
			Author existingAuthor = session.get(Author.class, id);

			// Check if the author exists
			if (existingAuthor == null) {
				return false;
			}

			// Update the author's properties
			if (newfirst_name != null && !newfirst_name.isEmpty()) {
				existingAuthor.setFirst_name(newfirst_name);
				;
			}

			if (newlast_name != null && !newlast_name.isEmpty()) {
				existingAuthor.setLast_name(newlast_name);
				;
			}

			if (newnationality != null && !newnationality.isEmpty()) {
				existingAuthor.setNationality(newnationality);
			}

			// Save the updated author object to the database
			session.update(existingAuthor);
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
	public boolean deleteAuthor(int id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Retrieve the existing author from the database
			Author existingAuthor = session.get(Author.class, id);

			// Check if the author exists
			if (existingAuthor == null) {
				return false;
			}

			// Delete the author
			session.delete(existingAuthor);
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
	public Author getAuthorById(int id) {
		try (Session session = sessionFactory.openSession()) {
			return session.get(Author.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Author> getAllAuthors() {
		try (Session session = sessionFactory.openSession()) {
			Query<Author> query = session.createQuery("from Author", Author.class);
			return query.list();
		} catch (Exception e) {
			return null;
		}
	}
}