package bookdemo.android.com.bookdemo;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class bookService extends Service {

    private static final String TAG = "BookService";
    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
//            Log.d(TAG,"get book list");
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.d(TAG,"add book");
            mBookList.add(book);
            onNewBookArrived(book);
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.d(TAG,"register listen");
            mListeners.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.d(TAG,"unregister listen");
            mListeners.unregister(listener);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.d(TAG,"Service started");
        mBookList.add(new Book(1,"android"));
//        mBookList.add(new Book(2,"ios"));
//        new Thread(new serviceWork()).start();
    }

    private class serviceWork implements Runnable {
        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try { 
                    Thread.sleep(5000); 
                } catch (InterruptedException e){
                      e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book newBook = new Book(bookId,"new Book #" + bookId);
                try {
                    Log.d(TAG,"new book arrived");
                    onNewBookArrived(newBook);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
//        mBookList.add(book);
        final int N = mListeners.beginBroadcast();
//        Log.e("onNewBookArrived","registener listener size:" + N);
        for (int i = 0; i < N; i++){
            IOnNewBookArrivedListener l = mListeners.getBroadcastItem(i);
            if (l!=null){
                l.OnNewBookArrivedListener(book);
            }
        }
        mListeners.finishBroadcast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG,"onstartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"service destroy");
        mIsServiceDestoryed.set(true);
    }
}
