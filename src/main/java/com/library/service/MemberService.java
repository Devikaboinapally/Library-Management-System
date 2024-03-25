package com.library.service;

import java.time.LocalDate;
import java.util.List;

import com.library.model.*;

public interface MemberService {

	public Member getMemberById(int id);

	public List<Member> getAllMembers();

	boolean addMember(int id, String name, LocalDate joined_date, String status);

	boolean deleteMember(int id);

	boolean updateMember(int id, String name, LocalDate joined_date, String status);

}
