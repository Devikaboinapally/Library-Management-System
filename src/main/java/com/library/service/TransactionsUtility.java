package com.library.service;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.library.model.*;

public class TransactionsUtility  implements TransactionsService {
	private final SessionFactory sessionFactory;

	public TransactionsUtility(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;

	}

	public boolean isBookAvailableForBorrowing(int book_id) {
		try (Session session = sessionFactory.openSession()) {
			// Query to check if the book is available for borrowing
			Query<Long> query = session.createQuery(
					"select count(*) from Transactions where book_id = :bookId and return_date is null", Long.class);
			query.setParameter("bookId", book_id);
			Long count = query.uniqueResult();

			// If count > 0, it means the book is already borrowed
			return count == 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false; // Return false in case of any exception
		}
	}

	public boolean isValidTransactionDates(LocalDate issueDate, LocalDate dueDate, LocalDate returnDate) {
		// Check if the issue date is before the due date
		if (issueDate.isAfter(dueDate)) {
			return false;
		}

		// Check if the return date, if provided, is after the issue date
		if (returnDate != null && returnDate.isBefore(issueDate)) {
			return false;
		}

		// All checks passed
		return true;
	}

	public boolean isMemberEligibleForMember(int member_id) {
		// You need to implement this method based on your application's logic.
		// For example, you could check if the member's account is active or if they
		// have any outstanding fines.
		// This might involve querying the database or checking the member's status.
		// For simplicity, let's assume all members are eligible.
		return true;
	}

	@Override
	public boolean deleteTransactions(int id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Retrieve the transaction from the database
			Transactions existingTransaction = session.get(Transactions.class, id);
			if (existingTransaction == null) {
				System.out.println("Transaction with ID " + id + " not found.");
				return false;
			}

			// Delete the transaction
			session.delete(existingTransaction);
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
	public Transactions getTransactionsById(int id) {
		try (Session session = sessionFactory.openSession()) {
			// Check if the transaction with the specified ID exists
			Transactions transactions = session.get(Transactions.class, id);
			if (transactions == null) {
				return null;
			}
			return transactions;
		} catch (Exception e) {

			// Handle any exceptions, such as database connection errors

			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<Transactions> getAllTransactions() {

		try (Session session = sessionFactory.openSession()) {
			Query<Transactions> query = session.createQuery("from Transactions", Transactions.class);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Transactions> getTransactionsByBookId(int book_id) {
		try (Session session = sessionFactory.openSession()) {
			// Check if the member ID exists in the database
			Book book = session.get(Book.class, book_id);
			if (book == null) {
				return null; // Return null as there are no books to retrieve
			}
			Query<Transactions> query = session.createQuery("from Transactions where book_id = :bookId",
					Transactions.class);
			query.setParameter("bookId", book_id);
			List<Transactions> transactions = query.list();
			if (transactions.isEmpty()) {
			}
			return transactions;
		} catch (Exception e) {
			// Handle exceptions or log errors
			e.printStackTrace();
			return null; // Return null instead of an empty list
		}
	}

	public List<Transactions> getTransactionsByMemberId(int member_id) {
		try (Session session = sessionFactory.openSession()) {
			// Check if the member ID exists in the database
			Member member = session.get(Member.class, member_id);
			if (member == null) {
				return null; // Return null as there are no members to retrieve
			}
			Query<Transactions> query = session.createQuery("from Transactions where member_id = :memberId",
					Transactions.class);
			query.setParameter("memberId", member_id);
			List<Transactions> transactions = query.list();
			if (transactions.isEmpty()) {
			}
			return transactions;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void addTransactions(int id, int book_id, int member_id, LocalDate issue_date, LocalDate due_date,
			LocalDate return_date) {
		try (Session session = sessionFactory.openSession()) {
			// Check if the book with the provided ID exists in the database
			Book book = session.get(Book.class, book_id);
			if (book == null) {
				System.out.println("Book with ID " + book_id + " does not exist in the database.");
				return; // Exit the method
			}

			// Check if the member with the provided ID exists in the database
			Member member = session.get(Member.class, member_id);
			if (member == null) {
				System.out.println("Member with ID " + member_id + " does not exist in the database.");
				return; // Exit the method
			}

			// Check if a transaction with the same book and member already exists
			Query<Transactions> query = session.createQuery(
					"SELECT t FROM Transactions t WHERE t.book.id = :bookId AND t.member.id = :memberId",
					Transactions.class);
			query.setParameter("bookId", book_id);
			query.setParameter("memberId", member_id);
			List<Transactions> existingTransactions = query.list();

			if (!existingTransactions.isEmpty()) {
				System.out.println("A transaction for the same book and member already exists.");
				return; // Exit the method
			}

			// Check if the book is available for borrowing
			if (!isBookAvailableForBorrowing(book_id)) {
				System.out.println("Error adding transaction: The book is not available for borrowing.");
				return ;
			}

			// Validate dates
			if (!isValidTransactionDates(issue_date, due_date, return_date)) {
				System.out.println("Invalid transaction dates.");
				return ;
			}

			// Check if the member is eligible for borrowing
			if (!isMemberEligibleForMember(member_id)) {
				System.out.println("Error adding transaction: The member is not eligible for borrowing.");
				return ;
			}
			
			// Proceed with adding the new transaction
			Transactions transactions = new Transactions();

			transactions.setId(id);
			transactions.setBook(book);
			transactions.setMember(member);
			transactions.setIssue_date(issue_date);
			transactions.setDue_date(due_date);
			transactions.setReturn_date(return_date);

			// Begin transaction and save the new transaction
			session.beginTransaction();
			session.save(transactions);
			session.getTransaction().commit();

			System.out.println("Transaction added successfully.");
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception
		}
	}

	@Override
	public boolean updateTransactions(int id, int book_id, int member_id, LocalDate issue_date, LocalDate newDueDate,
	        LocalDate newReturnDate) {
	    Transaction transaction = null;
	    try (Session session = sessionFactory.openSession()) {
	        transaction = session.beginTransaction();

	        // Retrieve the transaction from the database
	        Transactions existingTransaction = session.get(Transactions.class, id);
	        if (existingTransaction == null) {
	            System.out.println("Transaction with ID " + id + " not found.");
	            return false;
	        }

	        // Check if the provided newDueDate is valid
	        if (newDueDate != null && newDueDate.isBefore(existingTransaction.getIssue_date())) {
	            System.out.println("Invalid due date. Due date cannot be before the issue date.");
	            return false;
	        }

	        // Check if the provided newReturnDate is valid
	        if (newReturnDate != null && newReturnDate.isBefore(existingTransaction.getIssue_date())) {
	            System.out.println("Invalid return date. Return date cannot be before the issue date.");
	            return false;
	        }

	        // Update the transaction with the new dates if provided
	        if (newDueDate != null) {
	            existingTransaction.setDue_date(newDueDate);
	        }
	        if (newReturnDate != null) {
	            existingTransaction.setReturn_date(newReturnDate);
	        }

	        // Check if book_id and member_id are provided and update if necessary
	        if (book_id > 0) {
	            Book book = session.get(Book.class, book_id);
	            if (book != null) {
	                existingTransaction.setBook(book);
	            } else {
	                System.out.println("Book with ID " + book_id + " not found.");
	                return false;
	            }
	        }

	        if (member_id > 0) {
	            Member member = session.get(Member.class, member_id);
	            if (member != null) {
	                existingTransaction.setMember(member);
	            } else {
	                System.out.println("Member with ID " + member_id + " not found.");
	                return false;
	            }
	        }

	        session.update(existingTransaction);
	        transaction.commit();
	        return true; // Return true to indicate successful update
	    } catch (Exception e) {
	        if (transaction != null) {
	            transaction.rollback();
	        }
	        e.printStackTrace(); // Log any exceptions
	        return false; // Return false in case of error
	    }
	}
}
