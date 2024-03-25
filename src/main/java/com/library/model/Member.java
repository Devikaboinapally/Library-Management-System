package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Members")
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private LocalDate joined_date;
	private String status;

	@OneToMany(mappedBy = "member")
	private List<Transactions> transactions;

	@OneToMany(mappedBy = "member")
	private List<Fine> fines = new ArrayList<>();

	public Member(int id, String name, LocalDate joined_date, String status, List<Transactions> transactions,
			List<Fine> fines) {
		super();
		this.id = id;
		this.name = name;
		this.joined_date = joined_date;
		this.status = status;
		this.transactions = transactions;
		this.fines = fines;
	}

	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getJoined_date() {
		return joined_date;
	}

	public void setJoined_date(LocalDate joined_date) {
		this.joined_date = joined_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Transactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transactions> transactions) {
		this.transactions = transactions;
	}

	public List<Fine> getFines() {
		return fines;
	}

	public void setFines(List<Fine> fines) {
		this.fines = fines;
	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", name=" + name + ", joined_date=" + joined_date + ", status=" + status + "]";
	}
}
