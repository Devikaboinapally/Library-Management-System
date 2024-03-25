package com.library.service;

import java.util.List;

import com.library.model.Author;
import com.library.model.Book;

public interface BookAuthorService {
	
	  public void removeAuthorFromBook(int book_id, int author_id); 
	  public List<Book> getBooksForAuthor(int author_id);
	  public List<Author> getAuthorsForBook(int book_id);
	  public void addAuthorsToBook(int book_id, int author_id);


}
