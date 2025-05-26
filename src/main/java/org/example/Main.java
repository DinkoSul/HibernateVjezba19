package org.example;
import org.example.model.Author;
import org.example.model.Book;
import org.example.model.Publisher;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create and save entities
        createAndSaveEntities();

        // HQL SELECT query
        selectAuthorsWithBooks();

        // HQL UPDATE query
        updateBookTitle(1L, "Advanced Hibernate");

        // HQL SELECT query to verify update
        selectAuthorsWithBooks();

        // HQL DELETE query
        deleteBook(2L);

        // HQL SELECT query to verify delete
        selectAuthorsWithBooks();

        HibernateUtil.shutdown();
    }

    public static void createAndSaveEntities() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            // Create authors
            Author author1 = new Author("Author One");
            Author author2 = new Author("Author Two");

            // Create books
            Book book1 = new Book("Hibernate Basics");
            Book book2 = new Book("Java Basics");

            // Create publishers
            Publisher publisher1 = new Publisher("Publisher One");
            Publisher publisher2 = new Publisher("Publisher Two");

            // Set relationships
            author1.addBook(book1);
            author2.addBook(book2);

            book1.addPublisher(publisher1);
            book1.addPublisher(publisher2);
            book2.addPublisher(publisher1);

            // Save entities
            session.save(publisher1);
            session.save(publisher2);

            session.save(book1);
            session.save(book2);

            session.save(author1);
            session.save(author2);

            transaction.commit();
        }
    }

    public static void selectAuthorsWithBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Author> authors = session.createQuery("FROM Author", Author.class).list();
            System.out.println("Authors and their books:");
            for (Author author : authors) {
                System.out.println("Author: " + author.getName());
                for (Book book : author.getBooks()) {
                    System.out.println("  Book: " + book.getTitle() + ", Publishers: " + book.getPublishers().size());
                }
            }
        }
    }

    public static void updateBookTitle(Long bookId, String newTitle) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "UPDATE Book SET title = :newTitle WHERE id = :bookId";
            session.createQuery(hql)
                    .setParameter("newTitle", newTitle)
                    .setParameter("bookId", bookId)
                    .executeUpdate();
            transaction.commit();
        }
    }

    public static void deleteBook(Long bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "DELETE FROM Book WHERE id = :bookId";
            session.createQuery(hql)
                    .setParameter("bookId", bookId)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
