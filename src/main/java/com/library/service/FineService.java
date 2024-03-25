package com.library.service;

import java.time.LocalDate;
import java.util.List;

import com.library.model.*;

public interface FineService {

	boolean deleteFine(int id);

	public Fine getFineById(int id);

	public List<Fine> getAllFines();

	public List<Fine> getFineByTransactionId(int transaction_id);

	public List<Fine> getFineByMemberId(int member_id);

	boolean addFine(int id, int transaction_id, int member_id, LocalDate fine_date, int payment_amount, int fine_amount,
			LocalDate payment_date);

	boolean updateFine(int id, int transaction_id, int member_id, LocalDate newFineDate, int newFineAmount,
			LocalDate newPaymentDate, int newPaymentAmount);

}
