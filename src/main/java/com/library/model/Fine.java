package com.library.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Fines")
public class Fine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private LocalDate fine_date;
	private LocalDate payment_date;
	private int fine_amount;
	private int payment_amount;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "transaction_id")
	private Transactions transactions;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	public Fine(int id, LocalDate fine_date, LocalDate payment_date, int fine_amount, int payment_amount,
			Transactions transactions, Member member) {
		super();
		this.id = id;
		this.fine_date = fine_date;
		this.payment_date = payment_date;
		this.fine_amount = fine_amount;
		this.payment_amount = payment_amount;
		this.transactions = transactions;
		this.member = member;
	}

	public Fine() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getFine_date() {
		return fine_date;
	}

	public void setFine_date(LocalDate fine_date) {
		this.fine_date = fine_date;
	}

	public LocalDate getPayment_date() {
		return payment_date;
	}

	public void setPayment_date(LocalDate payment_date) {
		this.payment_date = payment_date;
	}

	public int getFine_amount() {
		return fine_amount;
	}

	public void setFine_amount(int i) {
		this.fine_amount = i;
	}

	public int getPayment_amount() {
		return payment_amount;
	}

	public void setPayment_amount(int payment_amount) {
		this.payment_amount = payment_amount;
	}

	public Transactions getTransactions() {
		return transactions;
	}

	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	@Override
	public String toString() {
		return "Fine [id=" + id + ", fine_date=" + fine_date + ", payment_date=" + payment_date + ", fine_amount="
				+ fine_amount + ", payment_amount=" + payment_amount + ", transactions=" + transactions + ", member="
				+ member + "]";
	}

}
