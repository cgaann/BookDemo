package bookdemo.android.com.bookdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import bookdemo.android.com.bookdemo.IBookManager;
import bookdemo.android.com.bookdemo.IOnNewBookArrivedListener;


public class bookClient extends Activity {

    private static final String TAG = "BookClient";
    private IBookManager bookManager;
    private static final int MESSAGE_NEW_BOOK_ARRIVED = 1;
    private Button mAddButton;
    private Button mClearButton;
    private EditText mBookNameText;
    private ListView mBookList;
    private BookAdapter mBookAdapter;
    private List<Book> mListBook;
    private Context mContext;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG, "received new book:" + msg.obj);
                    mListBook.add((Book)msg.obj);
                    Log.d(TAG,"dd is " + mListBook.size());
//                    mBookAdapter.add(mListBook);
                    mBookAdapter.notifyDataSetChanged();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_client);
        mContext = this;
        mAddButton = (Button)findViewById(R.id.add);
        mClearButton = (Button)findViewById(R.id.clear);
        mBookNameText = (EditText)findViewById(R.id.book);
        mBookList = (ListView)findViewById(R.id.bookList);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mBookNameText.getText().toString().equals("")){
                    addBook();
                }
            }
        });
        mClearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mBookAdapter.clear();
            }
        });
        bindservice();
    }

    private ServiceConnection mService = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bookManager= IBookManager.Stub.asInterface(iBinder);

            try {
//                List<Book> list = bookManager.getBookList();
//                Log.d(TAG, "query book list,list type:" + list.getClass().getCanonicalName());
//                Log.d(TAG, "query book list:" + list.toString());
//                Book newBook = new Book(3, "android进阶");
//                bookManager.addBook(newBook);
//                Log.d(TAG, "add book:" + newBook);
//                List<Book> newList = bookManager.getBookList();
//                Log.d(TAG, "query book list:" + newList.toString());
                try{
                    Log.d(TAG,"Bind service");
                    mListBook = bookManager.getBookList();
                    mBookAdapter = new BookAdapter(mListBook,mContext);
                    mBookList.setAdapter(mBookAdapter);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
                bookManager.registerListener(mNewBookArrivedListener);
            } catch (RemoteException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bookManager = null;
            Log.e(TAG, "binder died.");
        }
    };

    private IOnNewBookArrivedListener mNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void OnNewBookArrivedListener(Book book) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED, book).sendToTarget();
        }
    };

    private void bindservice(){
        Intent intent = new Intent();
        Log.d(TAG,"try to bind service");
        intent.setComponent(new ComponentName("bookdemo.android.com.bookdemo", "bookdemo.android.com.bookdemo.bookService"));
        bindService(intent,mService, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (bookManager != null && bookManager.asBinder().isBinderAlive()){
            Log.e(TAG, "unregister listener:" + mNewBookArrivedListener);
            try{
                bookManager.unregisterListener(mNewBookArrivedListener);
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
        unbindService(mService);
        super.onDestroy();
    }

    private void addBook() {
        try{
            List<Book> list = bookManager.getBookList();
            int count = list.size();
//            Log.d(TAG,"the count is " + count);
            Book newBook = new Book(count + 1, mBookNameText.getText().toString());
            bookManager.addBook(newBook);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }
}
