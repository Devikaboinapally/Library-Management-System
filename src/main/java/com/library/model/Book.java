package com.library.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title;
	private int publication_year;
	private String category;
	private int copies;
	private String ISBN;

	@ManyToMany
	@JoinTable(name = "bookauthor", joinColumns = @JoinColumn(name = "bookid"), inverseJoinColumns = @JoinColumn(name = "authorid"))
	private List<Author> authors = new ArrayList<>();

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	private List<Transactions> transactions = new ArrayList<>();

	public Book(int id, String title, int publication_year, String category, int copies, String iSBN,
			List<Author> authors, List<Transactions> transactions) {
		super();
		this.id = id;
		this.title = title;
		this.publication_year = publication_year;
		this.category = category;
		this.copies = copies;
		ISBN = iSBN;
		this.authors = authors;
		this.transactions = transactions;
	}

	public Book() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPublication_year() {
		return publication_year;
	}

	public void setPublication_year(int publication_year) {
		this.publication_year = publication_year;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Transactions> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transactions> transactions) {
		this.transactions = transactions;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", title=" + title + ", publication_year=" + publication_year + ", category="
				+ category + ", copies=" + copies + ", ISBN=" + ISBN + "]";
	}

}
