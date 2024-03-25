package com.library.service;

import java.util.List;

import com.library.model.*;

public interface AuthorService {

	public boolean isAuthorExists(int id);

	public boolean addAuthor(int id, String first_name, String last_name, String nationality);

	public boolean updateAuthor(int id, String newfirst_name, String newlast_name, String newnationality);

	public Author getAuthorById(int id);

	public List<Author> getAllAuthors();

	public boolean deleteAuthor(int id);
}
