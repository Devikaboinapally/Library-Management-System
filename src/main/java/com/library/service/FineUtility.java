package com.library.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.library.model.Fine;
import com.library.model.Member;
import com.library.model.Transactions;

public class FineUtility implements FineService {
	private MemberUtility memberimp;
	private SessionFactory sessionFactory;

	public FineUtility(SessionFactory sessionFactory, MemberUtility memberimp) {
		this.sessionFactory = sessionFactory;
		this.memberimp = memberimp; // Initialize memberUtility via constructor
	}

	@Override
	public boolean addFine(int id, int transaction_id, int member_id, LocalDate fine_date, int payment_amount,
			int fine_amount, LocalDate payment_date) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			Member member = session.get(Member.class, member_id);
			if (member == null) {
				return false;
			}

			Transactions transactions = session.get(Transactions.class, transaction_id);
			if (transactions == null) {
				return false;
			}

			LocalDate due_date = transactions.getDue_date();
			LocalDate return_date = transactions.getReturn_date();
			long lateDays = ChronoUnit.DAYS.between(due_date, return_date);

			if (lateDays <= 0) {
				System.out.println("No fine applicable: Return was not late.");
				return false;
			}

			int fineAmount = calculateFineAmount(lateDays, transactions);

			Fine fine = new Fine();
			fine.setId(id);
			fine.setTransactions(transactions);
			fine.setMember(member);
			fine.setFine_amount(fineAmount);
			fine.setFine_date(fine_date);
			fine.setPayment_date(payment_date);
			fine.setPayment_amount(payment_amount);
			session.save(fine);

			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace(); // Log the exception
			return false;
		}
	}

	private int calculateFineAmount(long lateDays, Transactions transactions) {
		// Implement your fine calculation logic here
		// You may consider factors like the type of item borrowed, fine rate per
		// day/hour, etc.
		// For example, you could have different fine rates for different types of items
		int fineRatePerDay = getFineRatePerDay(transactions); // Method to get fine rate per day based on item type
		return (int) (fineRatePerDay * lateDays);
	}

	private int getFineRatePerDay(Transactions transactions) {
		// Implement logic to retrieve fine rate per day based on the type of item
		// borrowed
		// You might have a lookup table or some other mechanism to determine the fine
		// rate for each type of item
		// This method should return the appropriate fine rate per day
		// For simplicity, let's assume a fixed fine rate per day for all types of items
		return 1; // $1 per day fine rate
	}

	@Override
	public boolean updateFine(int id, int transaction_id, int member_id, LocalDate newFineDate, int newFineAmount,
			LocalDate newPaymentDate, int newPaymentAmount) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Retrieve the Fine object by its ID
			Fine fine = session.get(Fine.class, id);
			if (fine == null) {
				return false;
			}

			// Update fine attributes
			if (newFineAmount > 0) {
				fine.setFine_amount(newFineAmount);
			}
			if (newPaymentDate != null) {
				fine.setPayment_date(newPaymentDate);
			}
			if (newPaymentAmount > 0) {
				fine.setPayment_amount(newPaymentAmount);
			}

			// Save the updated Fine object
			session.update(fine);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace(); // Log the exception
			return false;
		}
	}

	@Override
	public boolean deleteFine(int id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Retrieve the Fine object by its ID
			Fine fine = session.get(Fine.class, id);
			if (fine == null) {
				return false;
			}

			// Delete the Fine object
			session.delete(fine);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace(); // Log the exception
			return false;
		}
	}

	@Override
	public Fine getFineById(int id) {
		try (Session session = sessionFactory.openSession()) {
			// Retrieve the Fine object by its ID with eager fetching of Transactions
			Query<Fine> query = session.createQuery("select f from Fine f join fetch f.transactions where f.id = :id",
					Fine.class);
			query.setParameter("id", id);
			Fine fine = query.uniqueResult();
			return fine;
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception
			return null;
		}
	}

	@Override
	public List<Fine> getAllFines() {

		try (Session session = sessionFactory.openSession()) {
			// Fetch all Fine objects with eager fetching of Transactions
			Query<Fine> query = session.createQuery("select f from Fine f join fetch f.transactions", Fine.class);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception
			return null;
		}
	}

	public List<Fine> getFineByTransactionId(int transaction_id) {
		try (Session session = sessionFactory.openSession()) {
			// Check if the transaction with the given ID exists
			Transactions transaction = session.get(Transactions.class, transaction_id);
			if (transaction == null) {
				// Transaction does not exist, return an empty list or null
				return Collections.emptyList(); // or return null;
			}

			// Query fines associated with the given transaction ID
			Query<Fine> query = session.createQuery(
					"select f from Fine f join fetch f.transactions t where t.id = :transactionId", Fine.class);
			query.setParameter("transactionId", transaction_id);
			return query.list();
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception
			return null;
		}
	}

	public List<Fine> getFineByMemberId(int member_id) {
		 try (Session session = sessionFactory.openSession()) {
		        // Check if the member ID exists in the database
		        Member member = session.get(Member.class, member_id);
		        if (member == null) {
		            // Member does not exist, return an empty list or null
		            return Collections.emptyList(); // or return null;
		        }
		        
		        // Query fines associated with the given member ID, with eager fetching of Transactions
		        Query<Fine> query = session.createQuery(
		            "select f from Fine f join fetch f.transactions t where f.member.id = :memberId", Fine.class
		        );
		        query.setParameter("memberId", member_id);
		        return query.list();
		    } catch (Exception e) {
		        e.printStackTrace(); // Log the exception
		        return null;
		    }
	}

}
