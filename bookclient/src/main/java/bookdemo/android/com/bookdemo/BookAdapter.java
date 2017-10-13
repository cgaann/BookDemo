package bookdemo.android.com.bookdemo;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by Administrator on 2017/10/12 0012.
 */

public class BookAdapter extends BaseAdapter {

    private static final String TAG = "BookAdapter";
    private List<Book> mBookList;
    private Context mContext;

    public BookAdapter(List<Book> bookList, Context context) {
        this.mBookList = bookList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if(mBookList == null){
            return 0;
        }else{
            return mBookList.size();
        }

    }

    @Override
    public Object getItem(int i) {
        if(mBookList == null){
            return null;
        }
        return mBookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        Book book = mBookList.get(i);
        if(view == null || view.getTag() == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.book_list,null);
            holder = new ViewHolder();
            holder.mId = (TextView)view.findViewById(R.id.ID);
            holder.mName= (TextView)view.findViewById(R.id.Name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.mId.setText(book.getBookId()+"");
        holder.mName.setText(book.getBookName());
        return view;
    }

    public void add(List<Book> bookList){

//        mBookList.add(bookList.get(0));
        notifyDataSetChanged();
    }

    private static class ViewHolder{
        TextView mId;
        TextView mName;
    }

    public void clear(){
        mBookList.clear();
        notifyDataSetChanged();
    }
}
