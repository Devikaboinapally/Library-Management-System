package com.library.service;

import java.time.LocalDate;
import java.util.List;

import com.library.model.*;

public interface TransactionsService {

	public boolean deleteTransactions(int id);

	public Transactions getTransactionsById(int id);

	public List<Transactions> getAllTransactions();

	void addTransactions(int id, int book_id, int member_id, LocalDate issue_date, LocalDate due_date,
			LocalDate return_date);

	boolean updateTransactions(int id, int book_id, int member_id, LocalDate issue_date, LocalDate newDueDate,
			LocalDate newReturnDate);

}
