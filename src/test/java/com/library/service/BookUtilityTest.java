package com.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.library.model.Book;

public class BookUtilityTest {

	private SessionFactory sessionFactory;
	private Session session;

	@BeforeEach
	public void setUp() {
		sessionFactory = mock(SessionFactory.class);
		session = mock(Session.class);
		when(sessionFactory.openSession()).thenReturn(session);
	}

	@Test
	public void testGetBookById_WhenBookFound() {
		// Mocking a book with ID 1
		Book expectedBook = new Book();

		// Mocking session.get() method to return expectedBook when called with ID 1
		when(session.get(Book.class, 1)).thenReturn(expectedBook);

		// Creating a BookService instance
		BookUtility bookimp = new BookUtility(sessionFactory);

		// Calling getBookById method with ID 1
		Book actualBook = bookimp.getBookById(1);

		// Asserting that the returned book is the same as the expected book
		assertEquals(expectedBook, actualBook);
	}

	@Test
	public void testGetBookById_WhenBookNotFound() {
		// Mocking session.get() method to return null when called with any ID
		when(session.get(eq(Book.class), anyInt())).thenReturn(null); // Use eq() matcher for class type

		// Creating a BookService instance
		BookUtility bookimp = new BookUtility(sessionFactory);

		// Calling getBookById method with any ID
		Book actualBook = bookimp.getBookById(123); // ID doesn't matter in this case

		// Asserting that the returned book is null
		assertNull(actualBook);
	}
}
