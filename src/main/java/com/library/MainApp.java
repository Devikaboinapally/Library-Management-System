package com.library;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import org.hibernate.SessionFactory;

import com.library.model.Author;
import com.library.model.Book;
import com.library.model.Fine;
import com.library.model.Member;
import com.library.model.Transactions;
import com.library.service.AuthorUtility;
import com.library.service.BookAuthorUtility;
import com.library.service.BookUtility;
import com.library.service.FineUtility;
import com.library.service.MemberUtility;
import com.library.service.TransactionsUtility;
import com.library.util.HibernateUtil;

public class MainApp {

	public static Scanner scanner = new Scanner(System.in);

	private static final BookUtility bookimp = new BookUtility(HibernateUtil.getSessionFactory());
	private static final AuthorUtility authorimp = new AuthorUtility(HibernateUtil.getSessionFactory());
	private static final BookAuthorUtility bookauthorimp = new BookAuthorUtility(HibernateUtil.getSessionFactory());
	private static final MemberUtility memberimp = new MemberUtility(HibernateUtil.getSessionFactory());
	private static final TransactionsUtility transactionsimp = new TransactionsUtility(
			HibernateUtil.getSessionFactory());

	// Initialize sessionFactory with HibernateUtil.getSessionFactory()
	private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	// Initialize fineimp after sessionFactory is assigned
	private static final FineUtility fineimp = new FineUtility(sessionFactory, new MemberUtility(sessionFactory));

	public static void main(String[] args) {

		displayMenu();
	}

	private static void displayMenu() {
		int choice = 0;

		do {
			System.out.println("Welcome to the Library Management System");
			System.out.println("1. Manage Books");
			System.out.println("2. Manage Authors");
			System.out.println("3. Manage BookAuthor");
			System.out.println("4. Manage Members");
			System.out.println("5. Manage Transactions");
			System.out.println("6. Manage Fines");
			System.out.println("7. Exit");

			System.out.println("Enter your choice: ");
			choice = scanner.nextInt();

			switch (choice) {
			case 1:
				displayBookMenu();
				break;
			case 2:
				displayAuthorMenu();
				break;
			case 3:
				displayBookAuthorMenu();
				break;
			case 4:
				displayMemberMenu();
				break;
			case 5:
				displayTransactionMenu();
				break;
			case 6:
				displayFineMenu();
				break;
			case 7:
				System.out.println("Exiting the Library Management System");
				return;
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 6.");
			}
		} while (choice != 7);
	}

	private static void displayBookMenu() {
		int choice = 0;

		do {
			System.out.println("\nBook Management Menu:");
			System.out.println("1. Add a new book");
			System.out.println("2. Update an existing book");
			System.out.println("3. Delete a book");
			System.out.println("4. Search for a book by ISBN");
			System.out.println("5. Search for a book by id");
			System.out.println("6. List all books");
			System.out.println("7. Go back to the main menu");

			System.out.println("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addBook();
				break;
			case 2:
				updateBook();
				break;
			case 3:
				deleteBook();
				break;
			case 4:
				getBookByISBN();
				break;
			case 5:
				getBookById();
				break;
			case 6:
				getAllBooks();
				break;
			case 7:
				return; // Return to the main menu
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 7");
			}
		} while (choice != 7);
	}

	private static void getAllBooks() {
		try {
			List<Book> Books = bookimp.getAllBooks();

			// Check if there are books in the list
			if (!Books.isEmpty()) {
				System.out.println("All Books:");
				// Iterate over the list and print each book
				for (Book book : Books) {
					System.out.println(book); // Assuming you have overridden toString() method in your Book class
				}
			} else {
				System.out.println("No books found.");
			}

		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void getBookById() {
		try {
			System.out.println("Enter the ID of the book you want to search for:");

			int id = Integer.parseInt(scanner.nextLine());

			Book book = bookimp.getBookById(id);
			if (book != null) {
				System.out.println("Book found:");
				System.out.println(book); // Assuming you have overridden toString() method in your Book class
			} else {
				System.out.println("Book with the specified ID was not found.");
			}

		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void getBookByISBN() {
		try {
			System.out.println("Enter the ISBN of the book you want to search for:");
			String ISBN = scanner.nextLine();

			Book book = bookimp.getBookByISBN(ISBN);
			if (book != null) {
				System.out.println("Book found:");
				System.out.println(book); // Assuming you have overridden toString() method in your Book class

			} else {
				System.out.println("Book with the specified ISBN was not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void deleteBook() {

		System.out.println("Enter the ID of the book you want to delete:");
		int id;

		try {
			id = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer for the book ID.");
			return;
		}

		// Call the corresponding service method to delete the book
		boolean success = bookimp.deleteBook(id);

		// Display success or error message
		if (success) {
			System.out.println("Book deleted successfully!");
		} else {
			System.out.println("Book with the specified ID does not exist.");
		}
	}

	private static void updateBook() {
		try {
			System.out.println("Enter the ID of the book to update: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Check if the book with the given ID exists
			Book book = bookimp.getBookById(id);
			if (book != null) {
				System.out.println("Enter new Title: ");
				String title = scanner.nextLine();
				System.out.println("Enter publication_year: ");
				int publication_year = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter category: ");
				String category = scanner.nextLine();
				System.out.println("Enter new copies: ");
				int copies = Integer.parseInt(scanner.nextLine());
				System.out.println("Enter  ISBN: ");
				String ISBN = scanner.nextLine();

				// Call the updateBook method from your BookService class
				boolean success = bookimp.updateBook(id, title, publication_year, category, copies, ISBN);
				if (success) {
					System.out.println("Book updated successfully!");
				} else {
					System.out.println("Error updating book.");
				}
			} else {
				System.out.println("Book with the specified ID was not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void addBook() {
		try {
			// Read book details from the console
			System.out.println("Enter ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter Title: ");
			String title = scanner.nextLine();

			System.out.println("Enter Publication Year: ");
			int publicationYear = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter Category: ");
			String category = scanner.nextLine();

			System.out.println("Enter Copies: ");
			int copies = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter ISBN: ");
			String ISBN = scanner.nextLine();

			// Call the addBook method from the service class
			boolean success = bookimp.addBook(id, title, publicationYear, category, copies, ISBN);

			if (success) {
				System.out.println("Book added successfully!");
			} else {
				System.out.println("Error adding book. Please try again");
			}
		} catch (Exception e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void displayAuthorMenu() {
		int choice = 0;

		do {
			System.out.println("\nAuthor Management Menu:");
			System.out.println("1. Add a new author");
			System.out.println("2. Update an existing author");
			System.out.println("3. Delete an author");
			System.out.println("4. Search for an author by ID");
			System.out.println("5. List all authors");
			System.out.println("6. Go back to the main menu");

			System.out.println("Enter your choice: ");

			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addAuthor();
				break;
			case 2:
				updateAuthor();
				break;
			case 3:
				deleteAuthor();
				break;
			case 4:
				getAuthorById();
				break;
			case 5:
				getAllAuthors();
				break;
			case 6:
				return; // Return to the main menu
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 6.");
			}

		} while (choice != 6);

	}

	private static void addAuthor() {
		try {
			// Read author details from the console

			System.out.println("Enter ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter author's first name: ");
			String firstName = scanner.nextLine();

			System.out.println("Enter author's last name: ");
			String lastName = scanner.nextLine();

			System.out.println("Enter author's nationality: ");
			String nationality = scanner.nextLine();

			// Call the corresponding service method to add the author
			boolean success = authorimp.addAuthor(id, firstName, lastName, nationality);

			// Check if the author was added successfully
			if (success) {
				System.out.println("Author added successfully!");
			} else {
				System.out.println("Error adding author. Please try again.");
			}
		} catch (NumberFormatException e) {

			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void updateAuthor() {
		try {
			// Read author ID from the console

			System.out.println("Enter author ID to update: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Check if the author with the provided ID exists
			Author existingAuthor = authorimp.getAuthorById(id);
			if (existingAuthor == null) {
				System.out.println("Author with ID " + id + " not found.");
				return;
			}

			// Prompt the user to enter updated details
			System.out.println("Enter new first name (leave blank to keep current): ");
			String newFirstName = scanner.nextLine();

			System.out.println("Enter new last name (leave blank to keep current): ");
			String newLastName = scanner.nextLine();

			System.out.println("Enter new nationality (leave blank to keep current): ");
			String newNationality = scanner.nextLine();

			// Call the corresponding service method to update the author
			boolean success = authorimp.updateAuthor(id, newFirstName, newLastName, newNationality);

			// Display success or error message
			if (success) {
				System.out.println("Author updated successfully!");
			} else {
				System.out.println("Author with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {

			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void deleteAuthor() {
		// Read author ID from the console
		System.out.println("Enter author ID to delete: ");
		int id;
		try {
			id = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer for the author ID.");
			return;
		}

		// Call the corresponding service method to delete the author
		boolean success = authorimp.deleteAuthor(id);

		// Display success or error message
		if (success) {
			System.out.println("Author deleted successfully!");
		} else {
			System.out.println("Author with ID " + id + " not found.");
		}
	}

	private static void getAuthorById() {
		try {
			// Read author ID from the console

			System.out.println("Enter author ID to retrieve: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Call the corresponding service method to fetch the author by ID
			Author author = authorimp.getAuthorById(id);

			// Check if the author object is not null
			if (author != null) {
				// Display the author details
				System.out.println("Author Found:");
				System.out.println(author);
				System.out.println("Id: " + author.getId());
				System.out.println("First_name: " + author.getFirst_name());
				System.out.println("Last_name: " + author.getLast_name());
				System.out.println("Nationality: " + author.getNationality());

			} else {
				System.out.println("Author with the specified ID was not found.");
			}

		} catch (NumberFormatException e) {

			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void getAllAuthors() {
		try {
			// Call the corresponding service method to retrieve all authors

			List<Author> authors = authorimp.getAllAuthors();

			// Check if the list of authors is not empty
			if (!authors.isEmpty()) {
				System.out.println("List of all authors:");
				// Iterate over the list of authors and display their details
				for (Author author : authors) {
					System.out.println(author);
					System.out.println("ID: " + author.getId());
					System.out.println("First_name: " + author.getFirst_name());
					System.out.println("Last_name: " + author.getLast_name());
					System.out.println("Nationality: " + author.getNationality());
				}
			} else {
				// Display a message indicating that no authors were found
				System.out.println("No authors found.");
			}
		} catch (NumberFormatException e) {

			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void displayBookAuthorMenu() {
		int choice = 0;

		do {
			System.out.println("\nBookAuthor Management Menu:");
			System.out.println("1. Add Author to Book");
			System.out.println("2. Remove Author from Book");
			System.out.println("3. Get Books for Author");
			System.out.println("4. Get Authors for Book");
			System.out.println("5. Go back to the main menu");

			System.out.println("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addAuthorsToBook();
				break;
			case 2:
				removeAuthorFromBook();
				break;
			case 3:
				getBooksForAuthor();
				break;
			case 4:
				getAuthorsForBook();
			case 5:
				return; // Return to the main menu
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 5");
			}
		} while (choice != 5);

	}

	private static void addAuthorsToBook() {

		try {
			System.out.println("Enter the book ID:");
			int book_id = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter the author ID:");
			int author_id = Integer.parseInt(scanner.nextLine());

			bookauthorimp.addAuthorsToBook(book_id, author_id);

		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void removeAuthorFromBook() {
		try {
			// Prompt the user to enter the book ID and author ID
			System.out.println("Enter the book ID:");
			int book_id = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter the author ID:");
			int author_id = Integer.parseInt(scanner.nextLine());

			// Call the service method to remove the author from the book
			bookauthorimp.removeAuthorFromBook(book_id, author_id);

		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getAuthorsForBook() {
		try {
			// Prompt the user to enter the book ID
			System.out.println("Enter the book ID:");
			int book_id = Integer.parseInt(scanner.nextLine());

			// Call the service method to get the authors of the book
			List<Author> authors = bookauthorimp.getAuthorsForBook(book_id);

			// Check if authors are retrieved successfully
			if (authors != null) {

				for (Author author : authors) {
					System.out.println(author);
				}
			} else {
				System.out.println("Failed to retrieve authors for the book with ID " + book_id);
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getBooksForAuthor() {
		try {
			// Prompt the user to enter the author ID
			System.out.println("Enter the author ID:");
			int author_id = Integer.parseInt(scanner.nextLine());

			List<Book> books = bookauthorimp.getBooksForAuthor(author_id);

			if (books != null) {
				for (Book book : books) {
					System.out.println(book);
				}
			} else {
				System.out.println("Failed to retrieve authors for the book with ID " + author_id);
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void displayMemberMenu() {
		int choice = 0;

		do {
			System.out.println("\nMember Management Menu:");
			System.out.println("1. Add a new member");
			System.out.println("2. Update an existing member");
			System.out.println("3. Delete an member");
			System.out.println("4. Search for an member by ID");
			System.out.println("5. List all member");
			System.out.println("6. Go back to the main menu");

			System.out.println("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addMember();
				break;
			case 2:
				updateMember();
				break;
			case 3:
				deleteMember();

				break;
			case 4:
				getMemberById();
				break;
			case 5:
				getAllMembers();
				break;
			case 6:
				return; // Return to the main menu
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 6.");
			}

		} while (choice != 6);

	}

	private static void addMember() {
		try {

			// Read ID
			System.out.println("Enter ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			if (memberimp.isMemberExists(id)) {
				System.out.println("A member with the provided ID already exists. Please choose a different ID.");
				return; // Exit the method if member already exists
			}

			// Read First Name
			System.out.println("Enter  Name: ");
			String name = scanner.nextLine();

			// Read Joined Date
			LocalDate joined_date = null;
			boolean validDate = false;
			while (!validDate) {
				try {
					System.out.println("Enter Joined Date (YYYY-MM-DD): ");
					String joinedDateString = scanner.nextLine();
					joined_date = LocalDate.parse(joinedDateString);
					validDate = true;
				} catch (DateTimeParseException e) {
					System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
				}
			}

			// Read Status
			String status;
			while (true) {
				System.out.println("Enter Status (active/inactive): ");
				status = scanner.nextLine().toLowerCase(); // Convert to lowercase for case-insensitive comparison
				if (status.equals("active") || status.equals("inactive")) {
					break;
				} else {
					System.out.println("Status must be either 'active' or 'inactive'. Please try again.");
				}
			}

			// Call the service method to add the member
			boolean success = memberimp.addMember(id, name, joined_date, status);

			// Check if the member was added successfully
			if (success) {
				System.out.println("Member added successfully!");
			} else {
				System.out.println("Error adding member.");
			}

		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		}
	}

	private static void deleteMember() {
		// Read author ID from the console
		System.out.println("Enter member ID to delete: ");
		int id;
		try {
			id = Integer.parseInt(scanner.nextLine().trim());
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer for the member ID.");
			return;
		}
		// Call the corresponding service method to delete the member
		boolean success = memberimp.deleteMember(id);

		// Display success or error message
		if (success) {
			System.out.println("Member deleted successfully!");
		} else {
			System.out.println("Member with ID " + id + " not found.");
		}
	}

	private static void updateMember() {
		try {
			// Read member ID from the console

			System.out.println("Enter member ID to update: ");
			int id = Integer.parseInt(scanner.nextLine());
			// Check if the member with the provided ID exists
			Member existingMember = memberimp.getMemberById(id);
			if (existingMember == null) {
				System.out.println("Member with ID " + id + " not found.");
				return;
			}

			// Prompt the user to enter updated details
			// Read First Name
			System.out.println("Enter  Name (leave blank to keep current): ");
			String name = scanner.nextLine();

			// Read Joined Date
			LocalDate joined_date = null;
			boolean validDate = false;
			while (!validDate) {
				try {
					System.out.println("Enter Joined Date (YYYY-MM-DD) (leave blank to keep current): ");
					String joinedDateString = scanner.nextLine();
					joined_date = LocalDate.parse(joinedDateString);
					validDate = true;
				} catch (DateTimeParseException e) {
					System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
				}
			}

			// Read Status
			String status;
			while (true) {
				System.out.println("Enter Status (active/inactive): ");
				status = scanner.nextLine().toLowerCase(); // Convert to lowercase for case-insensitive comparison
				if (status.equals("active") || status.equals("inactive")) {
					break;
				} else {
					System.out.println("Status must be either 'active' or 'inactive'. Please try again.");
				}
			}

			// Call the corresponding service method to update the member
			boolean success = memberimp.updateMember(id, name, joined_date, status);

			// Display success or error message
			if (success) {
				System.out.println("Member updated successfully!");
			} else {
				System.out.println("Member with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getMemberById() {
		try {
			// Read member ID from the console
			System.out.println("Enter member ID to retrieve: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Call the corresponding service method to fetch the member by ID
			Member member = memberimp.getMemberById(id);

			// Check if the member object is not null
			if (member != null) {
				// Display the member details
				System.out.println(member);
				System.out.println("Id: " + member.getId());
				System.out.println(" Name: " + member.getName());
				System.out.println("Joined_date : " + member.getJoined_date());
				System.out.println("Status: " + member.getStatus());

			} else {
				System.out.println("Member with the specified ID was not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getAllMembers() {
		try {
			// Call the corresponding service method to retrieve all members
			List<Member> members = memberimp.getAllMembers();

			// Check if the list of members is not empty
			if (!members.isEmpty()) {
				System.out.println("List of all members:");
				// Iterate over the list of members and display their details
				for (Member member : members) {
					System.out.println(member);
					System.out.println("Id: " + member.getId());
					System.out.println(" Name: " + member.getName());
					System.out.println("Joined_date : " + member.getJoined_date());
					System.out.println("Status: " + member.getStatus());
				}
			} else {
				// Display a message indicating that there are no members in the database
				System.out.println("No members found in the database.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void displayTransactionMenu() {
		int choice = 0;

		do {
			System.out.println("\nTransaction Management Menu:");
			System.out.println("1. Add a new transactions");
			System.out.println("2. Update an existing transactions");
			System.out.println("3. Delete an transactions");
			System.out.println("4. Search for an transactions by ID");
			System.out.println("5. List all transactions");
			System.out.println("6. Search for transactions by Book ID");
			System.out.println("7. Search for transactions by Member ID");
			System.out.println("8. Go back to the main menu");

			System.out.println("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addTransactions();
				break;
			case 2:
				updateTransactions();
				break;
			case 3:
				deleteTransactions();
				break;
			case 4:
				getTransactionsById();
				break;
			case 5:
				getAllTransactions();
				break;
			case 6:
				getTransactionsByBookId();
				break;
			case 7:
				getTransactionsByMemberId();
				break;
			case 8:
				return; // Return to the main menu
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 6.");
			}
		} while (choice != 8);
	}

	private static void addTransactions() {
		try {
			System.out.println("Enter Transaction ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter Book ID: ");
			int book_id = Integer.parseInt(scanner.nextLine());

			// Check if the book ID exists in the database
			if (bookimp.getBookById(book_id) == null) {
				System.out.println("Book with ID " + book_id + " does not exist in the database.");
				return;
			}

			System.out.println("Enter Member ID: ");
			int member_id = Integer.parseInt(scanner.nextLine());

			// Check if the member ID exists in the database
			if (memberimp.getMemberById(member_id) == null) {
				System.out.println("Member with ID " + member_id + " does not exist in the database.");
				return;
			}

			System.out.println("Enter Issue Date (YYYY-MM-DD): ");
			LocalDate issue_date = LocalDate.parse(scanner.nextLine());

			System.out.println("Enter Due Date (YYYY-MM-DD): ");
			LocalDate due_date = LocalDate.parse(scanner.nextLine());

			System.out.println("Enter Return Date (YYYY-MM-DD): ");
			LocalDate return_date = LocalDate.parse(scanner.nextLine());

			// Call the service method to add the transaction
			transactionsimp.addTransactions(id, book_id, member_id, issue_date, due_date, return_date);

		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
		} catch (Exception e) {
			System.out.println("Error adding transaction: " + e.getMessage());
		}
	}

	private static void updateTransactions() {
		try {
			System.out.println("Enter Transaction ID to update: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Check if the transaction with the provided ID exists
			Transactions existingTransaction = transactionsimp.getTransactionsById(id);
			if (existingTransaction == null) {
				System.out.println("Transaction with ID " + id + " not found.");
				return;
			}

			System.out.println("Enter the new book ID (leave blank to keep current): ");
			String bookIdStr = scanner.nextLine().trim();
			int book_id = bookIdStr.isEmpty() ? existingTransaction.getBook().getId() : Integer.parseInt(bookIdStr);

			System.out.println("Enter the new member ID (leave blank to keep current): ");
			String memberIdStr = scanner.nextLine().trim();
			int member_id = memberIdStr.isEmpty() ? existingTransaction.getMember().getId()
					: Integer.parseInt(memberIdStr);

			System.out.println("Enter the new issue date (YYYY-MM-DD) (leave blank to keep current): ");
			String newIssueDateStr = scanner.nextLine().trim();
			LocalDate issue_date = newIssueDateStr.isEmpty() ? existingTransaction.getIssue_date()
					: LocalDate.parse(newIssueDateStr);

			System.out.println("Enter the new due date (YYYY-MM-DD) (leave blank to keep current): ");
			String newDueDateStr = scanner.nextLine().trim();
			LocalDate due_date = newDueDateStr.isEmpty() ? null : LocalDate.parse(newDueDateStr);

			System.out.println("Enter the new return date (YYYY-MM-DD) (leave blank to keep current): ");
			String newReturnDateStr = scanner.nextLine().trim();
			LocalDate return_date = newReturnDateStr.isEmpty() ? null : LocalDate.parse(newReturnDateStr);

			boolean success = transactionsimp.updateTransactions(id, book_id, member_id, issue_date, due_date,
					return_date);
			if (success) {
				System.out.println("Transaction updated successfully!");
			} else {
				System.out.println("Transaction with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
		}
	}

	private static void deleteTransactions() {
		try {
			// Read transaction ID from the console

			System.out.println("Enter transaction ID to delete: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Call the corresponding service method to delete the transaction
			boolean success = transactionsimp.deleteTransactions(id);

			// Display success or error message
			if (success) {
				System.out.println("Transaction deleted successfully!");
			} else {
				System.out.println("Transaction with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer for the transaction ID.");
			return;
		}
	}

	private static void getTransactionsById() {
		try {
			System.out.println("Enter transaction ID to retrieve: ");
			int id = Integer.parseInt(scanner.nextLine().trim());

			Transactions transactions = transactionsimp.getTransactionsById(id);
			if (transactions != null) {
				System.out.println("Transaction found:");
				// Print transaction details or perform any other actions
				System.out.println(transactions);

			} else {
				System.out.println("Transaction with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getAllTransactions() {
		try {
			List<Transactions> allTransactions = transactionsimp.getAllTransactions();
			if (!allTransactions.isEmpty()) {
				System.out.println("All Transactions:");
				for (Transactions transaction : allTransactions) {
					// Print transaction details or perform any other actions
					System.out.println(transaction);
				}
			} else {
				System.out.println("No transactions found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getTransactionsByBookId() {
		try {
			System.out.println("Enter the Book ID:");
			int book_id = Integer.parseInt(scanner.nextLine());
			List<Transactions> bookTransactions = transactionsimp.getTransactionsByBookId(book_id);

			if (bookTransactions != null) {
				if (bookTransactions.isEmpty()) {
					System.out.println("No transactions found for Book ID " + book_id);
				} else {

					System.out.println("Transactions for Book ID " + book_id + ":");
					for (Transactions transaction : bookTransactions) {
						// Print or process each transaction
						System.out.println(transaction);
					}
				}
			} else {
				System.out.println("Book ID " + book_id + " not found in the database.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getTransactionsByMemberId() {
		try {
			// Get member ID from console
			System.out.println("Enter the Member ID:");
			int member_id = Integer.parseInt(scanner.nextLine());

			// Retrieve transactions by member ID
			List<Transactions> memberTransactions = transactionsimp.getTransactionsByMemberId(member_id);

			if (memberTransactions != null) {
				if (memberTransactions.isEmpty()) {
					System.out.println("No transactions found for Member ID " + member_id);
				} else {

					System.out.println("Transactions for Member ID " + member_id + ":");
					for (Transactions transaction : memberTransactions) {
						// Print or process each transaction
						System.out.println(transaction);
					}
				}
			} else {
				System.out.println("Member ID " + member_id + " not found in the database.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void displayFineMenu() {
		int choice = 0;

		do {
			System.out.println("\nFine Management Menu:");
			System.out.println("1. Add a new fine");
			System.out.println("2. Update an existing fine");
			System.out.println("3. Delete a fine");
			System.out.println("4. Search for a fine by ID");
			System.out.println("5. List all fines");
			System.out.println("6. Search for fines by Transaction ID");
			System.out.println("7. Search for fines by Member ID");
			System.out.println("8. Go back to the main menu");

			System.out.println("Enter your choice: ");
			choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (choice) {
			case 1:
				addFine();
				break;
			case 2:
				updateFine();
				break;
			case 3:
				deleteFine();
				break;
			case 4:
				getFineById();
				break;
			case 5:
				getAllFines();
				break;
			case 6:
				getFineByTransactionId();
				break;
			case 7:
				getFineByMemberId();
				break;
			case 8:
				return; // Return to the main menu
			default:
				System.out.println("Invalid choice. Please enter a number between 1 and 8.");
			}
		} while (choice != 8);
	}

	private static void addFine() {
		try {
			System.out.println("Enter Fine ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter transaction ID: ");
			int transaction_id = Integer.parseInt(scanner.nextLine());

			// Check if the transaction exists
			Transactions transactions = transactionsimp.getTransactionsById(transaction_id);
			if (transactions == null) {
				System.out.println("Error: Transaction with ID " + transaction_id + " does not exist.");
				return;
			}

			System.out.println("Enter Member ID: ");
			int member_id = Integer.parseInt(scanner.nextLine());

			// Check if the member exists
			Member member = memberimp.getMemberById(member_id);
			if (member == null) {
				System.out.println("Error: Member with ID " + member_id + " does not exist.");
				return;
			}

			System.out.println("Enter Fine Date (YYYY-MM-DD): ");
			LocalDate fine_date = LocalDate.parse(scanner.nextLine());

			System.out.println("Enter Fine Amount: ");
			int fine_amount = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter Payment Amount: ");
			int payment_amount = Integer.parseInt(scanner.nextLine());

			System.out.println("Enter Payment Date (YYYY-MM-DD): ");
			LocalDate payment_date = LocalDate.parse(scanner.nextLine());

			// Validate that payment date is not before fine date
			if (payment_date.isBefore(fine_date)) {
				System.out.println("Error: Payment date cannot be before fine date.");
				return;
			}

			// Call the service method to add the transaction
			boolean success = fineimp.addFine(id, transaction_id, member_id, fine_date, fine_amount, payment_amount,
					payment_date);

			if (success) {
				System.out.println("Fine added successfully!");
			} else {
				System.out.println("Error adding fine.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
		}
	}

	private static void updateFine() {
		try {
			System.out.println("Enter Fine ID to update: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Check if the fine with the provided ID exists
			Fine existingFine = fineimp.getFineById(id);
			if (existingFine == null) {
				System.out.println("Fine with ID " + id + " not found.");
				return;
			}

			System.out.println("Enter new transaction ID (leave blank to keep current): ");
			String newTransactionIdStr = scanner.nextLine();
			Integer transaction_id = newTransactionIdStr.isEmpty() ? existingFine.getTransactions().getId()
					: Integer.parseInt(newTransactionIdStr);

			// Check if the provided transaction ID exists in the database
			if (!newTransactionIdStr.isEmpty() && transactionsimp.getTransactionsById(transaction_id) == null) {
				System.out.println("Error updating fine: Transaction with ID " + transaction_id + " not found.");
				return;
			}

			System.out.println("Enter new member ID (leave blank to keep current): ");
			String newMemberIdStr = scanner.nextLine();
			Integer member_id = newMemberIdStr.isEmpty() ? existingFine.getMember().getId()
					: Integer.parseInt(newMemberIdStr);

			// Check if the provided member ID exists in the database
			if (!newMemberIdStr.isEmpty() && memberimp.getMemberById(member_id) == null) {
				System.out.println("Error updating fine: Member with ID " + member_id + " not found.");
				return;
			}

			System.out.println("Enter New Fine Date (YYYY-MM-DD) (leave blank to keep current): ");
			String newFineDateStr = scanner.nextLine();
			LocalDate newFineDate = newFineDateStr.isEmpty() ? existingFine.getFine_date()
					: LocalDate.parse(newFineDateStr);

			System.out.println("Enter New Payment Date (YYYY-MM-DD) (leave blank to keep current): ");
			String newPaymentDateStr = scanner.nextLine();
			LocalDate newPaymentDate = newPaymentDateStr.isEmpty() ? existingFine.getPayment_date()
					: LocalDate.parse(newPaymentDateStr);

			System.out.println("Enter New Fine Amount (leave blank to keep current): ");
			String newFineAmountStr = scanner.nextLine();
			Integer newFineAmount = newFineAmountStr.isEmpty() ? existingFine.getFine_amount()
					: Integer.parseInt(newFineAmountStr);

			System.out.println("Enter New Payment Amount (leave blank to keep current): ");
			String newPaymentAmountStr = scanner.nextLine();
			Integer newPaymentAmount = newPaymentAmountStr.isEmpty() ? existingFine.getPayment_amount()
					: Integer.parseInt(newPaymentAmountStr);

			// Call the service method to update the fine
			boolean success = fineimp.updateFine(id, transaction_id, member_id, newFineDate, newFineAmount,
					newPaymentDate, newPaymentAmount);

			if (success) {
				System.out.println("Fine updated successfully!");
			} else {
				System.out.println("Error updating fine. Fine with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter valid numeric values.");
		} catch (DateTimeParseException e) {
			System.out.println("Invalid date format. Please enter the date in the format YYYY-MM-DD.");
		}
	}

	private static void deleteFine() {
		try {
			System.out.println("Enter Fine ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Call the service method to delete the fine
			boolean success = fineimp.deleteFine(id);

			if (success) {
				System.out.println("Fine deleted successfully!");
			} else {
				System.out.println("Error deleting fine. Fine with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter a valid numeric value for the Fine ID.");
		}
	}

	private static void getFineById() {
		try {
			System.out.println("Enter Fine ID: ");
			int id = Integer.parseInt(scanner.nextLine());

			// Call the service method to get the fine by ID
			Fine fine = fineimp.getFineById(id);

			if (fine != null) {
				System.out.println("Fine details for ID " + id + ":");
				System.out.println(fine);
			} else {
				System.out.println("Fine with ID " + id + " not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input format. Please enter a valid numeric value for the Fine ID.");
		}
	}

	private static void getAllFines() {
		try {
			List<Fine> fines = fineimp.getAllFines();

			if (!fines.isEmpty()) {
				System.out.println("All fines:");
				for (Fine fine : fines) {
					System.out.println(fine);
				}
			} else {
				System.out.println("No fines found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getFineByTransactionId() {
		try {
			System.out.println("Enter the Transaction ID:");
			int transaction_id = Integer.parseInt(scanner.nextLine());
			List<Fine> Finestransaction = fineimp.getFineByTransactionId(transaction_id);

			if (Finestransaction != null) {
				if (Finestransaction.isEmpty()) {
					System.out.println("Transaction with ID " + transaction_id + " does not exist.");
				} else {

					System.out.println("Fine details for Transaction ID " + transaction_id + ":");

					for (Fine fine : Finestransaction) {
						// Print or process each transaction
						System.out.println(fine);
					}
				}
			} else {
				System.out.println("Transaction ID " + transaction_id + " not found in the database.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}

	private static void getFineByMemberId() {
		try {
			System.out.println("Enter the Member ID:");
			int member_id = Integer.parseInt(scanner.nextLine());
			List<Fine> MemberFines = fineimp.getFineByMemberId(member_id);

			if (MemberFines != null) {
				if (MemberFines.isEmpty()) {
					System.out.println("Member with ID " + member_id + " does not exist.");
				} else {
					System.out.println("Fine details for Member ID " + member_id + ":");
					for (Fine fine : MemberFines) {
						// Print or process each transaction
						System.out.println(fine); // Assuming you have overridden toString() method in your Book class
					}
				}
			} else {
				System.out.println("Member ID " + member_id + " not found in the database.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a valid integer.");
		} catch (Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
		}
	}
}
