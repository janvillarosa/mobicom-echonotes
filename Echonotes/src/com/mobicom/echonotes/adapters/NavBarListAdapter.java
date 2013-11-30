//package com.mobicom.echonotes.adapters;
//
//import java.util.ArrayList;
//
//import com.mobicom.echonotes.NavBarListModel;
//import com.mobicom.echonotes.activity.ListOfNotes;
//
//import android.R;
//import android.app.Activity;
//import android.content.Context;
//import android.content.res.Resources;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class NavBarListAdapter extends ArrayAdapter<NavBarListModel>{
//		private Activity activity;
//		private ArrayList<NavBarListModel> data = null;
//		private static LayoutInflater inflater = null;
//	    public Resources res;    
//	    NavBarListModel tempValues = null;
//	    int i = 0;
//	    
//	    public NavBarListAdapter(Activity a, ArrayList<NavBarListModel> d, Resources resLocal) {
//	        
//	    	activity = a;
//	        data = d;
//	    	res = resLocal;
//	    	
//	    	inflater = (LayoutInflater) activity
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	    }
//	    
//	    static class NavBarListHolder
//	    {
//	        ImageView imgIcon;
//	        TextView txtTitle;
//	    }
//	    
//	    @Override
//	    public View getView(int position, View convertView, ViewGroup parent) {
//	        View vi = convertView;
//	        NavBarListHolder holder = null;
//	        
//	        if(vi == null)
//	        {
//	            LayoutInflater inflater = activity.getLayoutInflater();
//	            vi = inflater.inflate(R.layout.navbar_listview_item_row, null);
//	            
//	            holder = new NavBarListHolder();
//	            vi.setTag(holder);
//	        }
//	        else
//	        {
//	            holder = (NavBarListHolder)vi.getTag();
//	        }
//	        
//	        if (data.size() <= 0) {
//				holder.txtTitle.setText("No Data");
//			} else {
//			tempValues = null;
//			tempValues = data.get(position);
//	        holder.txtTitle.setText(tempValues.getTitle());
//	        holder.imgIcon.setImageResource(tempValues.getIcon());
//	        
//	        vi.setOnClickListener(new OnItemClickListener(position));
//			}
//	        return vi;
//		}
//	        
//	        private class OnItemClickListener implements OnClickListener {
//	    		private int mPosition;
//
//	    		OnItemClickListener(int position) {
//	    			mPosition = position;
//	    		}
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					ListOfNotes sct = (ListOfNotes) activity;
//
//					/****
//					 * Call onItemClick Method inside CustomListViewAndroidExample Class
//					 * ( See Below )
//					 ****/
//
//					sct.onItemClick(mPosition);
//				}
//	        }
//	    
//}
