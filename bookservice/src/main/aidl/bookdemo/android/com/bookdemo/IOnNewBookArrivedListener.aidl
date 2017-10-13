// IOnNewBookArrivedListener.aidl
package bookdemo.android.com.bookdemo;
import bookdemo.android.com.bookdemo.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void OnNewBookArrivedListener(in Book book);
}
