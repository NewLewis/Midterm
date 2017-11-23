package menglulu.midterm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thinkpad on 2017/11/22.
 */

public class ItemListAdapter extends BaseAdapter {
    private Context context;
    private List<Role> list;

    private class ViewHolder{
        public TextView name;
    }

    public ItemListAdapter(Context context,List<Role> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount(){
        if(list == null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int i){
        if(list == null){
            return null;
        }
        return list.get(i);
    }

    @Override
    public long getItemId(int i){
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        View convertView;
        ViewHolder viewHolder;

        if(view == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.brief_role,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        }else{
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(list.get(i).getName());
        viewHolder.name.setBackgroundResource(list.get(i).getBgid());

        return convertView;
    }

    public void removeItem(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

//    public interface OnItemClickListener
//    {
//        void onClick(int position);
//        void onLongClick(int position);
//    }
//
//    public void onBindViewHolder (final menglulu.midterm.ViewHolder holder, int position) {
//        convert(holder, mDatas.get(position));
//        if (mOnItemClickListener != null) {
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onClick(holder.getAdapterPosition());
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
//                    return true;
//                }
//            });
//        }
//    }
//
//    protected abstract void convert(menglulu.midterm.ViewHolder holder, T t);

    private boolean isNULL(){
        return list == null;
    }
}
