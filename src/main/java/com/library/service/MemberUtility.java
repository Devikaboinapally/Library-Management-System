package com.library.service;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.library.model.Member;

public class MemberUtility implements MemberService {
	private final SessionFactory sessionFactory;

	public MemberUtility(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public boolean isMemberExists(int id) {
		try (Session session = sessionFactory.openSession()) {
			Member existingMember = session.get(Member.class, id);
			return existingMember != null;
		} catch (Exception e) {
			System.out.println("Error checking member existence: " + e.getMessage());
			return true; // Assuming error handling as per your requirement
		}
	}

	@Override
	public boolean addMember(int id, String name, LocalDate joined_date, String status) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Check if the member already exists
			if (isMemberExists(id)) {
				System.out.println("Error adding member: Member with ID " + id + " already exists.");
				return false;
			}

			// Create a Member object and set its properties
			Member member = new Member();
			member.setId(id);
			member.setName(name);
			member.setJoined_date(joined_date);
			member.setStatus(status);
			// Save the member object to the database
			session.save(member);
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
	public boolean updateMember(int id, String name, LocalDate joined_date, String status) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Check if the member exists
			Member member = session.get(Member.class, id);
			if (member == null) {
				return false;
			}

			// Update member attributes
			member.setName(name);
			member.setJoined_date(joined_date);
			member.setStatus(status);

			session.update(member);
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
	public boolean deleteMember(int id) {
		Transaction transaction = null;
		try (Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();

			// Check if the member exists
			Member member = session.get(Member.class, id);
			if (member == null) {
				return false;
			}

			// Delete the member from the database
			session.delete(member);
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
	public Member getMemberById(int id) {
		try (Session session = sessionFactory.openSession()) {
			return session.get(Member.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<Member> getAllMembers() {
		try (Session session = sessionFactory.openSession()) {
			Query<Member> query = session.createQuery("from Member", Member.class);
			return query.list();
		} catch (Exception e) {
			return null;
		}
	}

}
