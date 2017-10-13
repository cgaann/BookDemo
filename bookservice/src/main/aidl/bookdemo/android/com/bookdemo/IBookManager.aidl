// IBookManager.aidl
package bookdemo.android.com.bookdemo;
import bookdemo.android.com.bookdemo.Book;
import bookdemo.android.com.bookdemo.IOnNewBookArrivedListener;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
